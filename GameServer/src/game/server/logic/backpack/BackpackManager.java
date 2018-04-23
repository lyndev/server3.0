package game.server.logic.backpack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.core.message.SMessage;
import game.core.util.DateUtils;
import game.core.util.SimpleRandom;
import game.core.util.UUIDUtils;
import game.data.GameDataManager;
import game.data.bean.q_itemBean;
import game.data.bean.q_vipBean;
import game.message.BackpackMessage;
import game.server.db.game.bean.ProduceExceptionBean;
import game.server.logic.constant.AchievementType;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.ItemType;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.item.bean.ExpProp;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.thread.BackLogProcessor;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ProduceExceptionHandler;
import game.server.util.MessageUtils;
import game.server.util.UniqueId;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * <b>背包管理类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class BackpackManager implements IJsonConverter
{
    private static final Logger LOG = Logger.getLogger(BackpackManager.class);

    // 背包拥有者
    private final transient Player owner;

    // 背包数据：key = 格子ID
    private final Map<Integer, Grid> gridMap;

    // 道具-格子关联表：key = 物品ID，value = 格子ID
    private final Map<Integer, Integer> propGrids;

    // 玩家资源信息
    private final Map<ResourceType, Resource> resourceMap;

    // 物品产出的监控信息: key = 物品ID, value = 监控信息
    private final Map<Integer, ProduceMonit> itemMonitor;

    // 资源产出的监控信息: key = 资源类型, value = 监控信息
    private final Map<ResourceType, ProduceMonit> resourceMonitor;

    // 监控物品连续产出的有效时间（单位：分钟）
    private final static int itemMonitTime = 5;

    // 监控资源连续产出的有效时间（单位：分钟）
    private final static int resourceMonitTime = 5;

    private final SimpleRandom random;

    public final static String JSON_KEY_ITEM = "item";

    public final static String JSON_KEY_RESOURCE = "resource";

    public final static String JSON_KEY_ITEM_MONITOR = "itemMonitor";

    public final static String JSON_KEY_RESOURCE_MONITOR = "resourceMonitor";

    public final static String JSON_KEY_BOXES_COUNT = "boxesCount";

    /**
     * 体力恢复时间（单位：秒）
     */
    public final static int ENERGY_REVERT_TIME = 6 * 60;
    /**
     * 技能点恢复时间（单位：秒）
     */
    public final static int SKILL_REVERT_TIME = 2 * 60;

    public BackpackManager(Player owner)
    {
        this.owner = owner;
        random = new SimpleRandom();
        gridMap = new HashMap<>();
        propGrids = new HashMap<>();
        resourceMap = new EnumMap<>(ResourceType.class);
        resourceMonitor = new EnumMap<>(ResourceType.class);
        itemMonitor = new HashMap<>();

        // 初始化资源结构
        long nowTime = System.currentTimeMillis();
        for (ResourceType type : ResourceType.values())
        {
            if (ResourceType.ENERGY == type)
            {
                // 设置体力恢复属性（每6分钟+1）
                Resource res = new Resource(type.value(), 0);
                Resource.Producer producer = res.new Producer();
                producer.setInterval(ENERGY_REVERT_TIME); //每次产出的时间间隔
                producer.setAddValue(1);                    //每次生产的产出值
                producer.setLimit(ResourceType.ENERGY.getLimit()); //自动产出的上限值
                producer.setLastTime(-1); // 表示未初始化（将在fromJson中初始化lastTime）
                res.setProducer(producer);
                resourceMap.put(type, res);
            }
            else if (ResourceType.SKILL_POINT == type)
            {
                //设置技能点恢复属性（每2分钟+1)
                Resource res = new Resource(type.value(), 0);
                Resource.Producer producer = res.new Producer();
                producer.setInterval(SKILL_REVERT_TIME);
                producer.setAddValue(1);
                producer.setLimit(ResourceType.SKILL_POINT.getLimit());
                producer.setLastTime(-1); // 表示未初始化（将在fromJson中初始化lastTime）
                res.setProducer(producer);
                resourceMap.put(type, res);
            }
            else
            {
                resourceMap.put(type, new Resource(type.value(), 0));
            }

            ProduceMonit monit = new ProduceMonit();
            monit.setId(type.value());
            monit.setStartTime(nowTime);
            resourceMonitor.put(type, monit);
        }
    }

    public void createInitialize()
    {
        Resource energy = resourceMap.get(ResourceType.ENERGY);
        if (energy != null && energy.getProducer() != null)
        {
            energy.setAmount(energy.getProducer().getLimit());
        }

        Resource skillPoint = resourceMap.get(ResourceType.SKILL_POINT);
        if (skillPoint != null && skillPoint.getProducer() != null)
        {
            skillPoint.setAmount(skillPoint.getProducer().getLimit());
        }
         
        //测试方法:添加所有资源
        //TestInitResource();
    }

    /**
     * 客户端加载完成.
     */
    public void clientInitializeOver()
    {
        sendBackpackList();
        /*
         * 客户端加载完成后, 表示玩家离线好友申请消息已处理完毕.<br> 
         * 刷新资源列表, 以重新设置体力上限, 并计算自增长的点数.
         */
        flushResourceList(false);
        owner.SendPlayerInfoMsg();
    }

    /**
     * 刷新资源列表.
     *
     * @param notify 是否通知客户端
     */
    public void flushResourceList(boolean notify)
    {
        Resource energy = resourceMap.get(ResourceType.ENERGY);
        if (energy != null && energy.getProducer() != null)
        {
            //体力
            int beforLimit = energy.getProducer().getLimit();
            energy.getProducer().setLimit(
                    ResourceType.ENERGY.getLimit()
                    + owner.getRoleLevel() - 1);
            int curLimit = energy.getProducer().getLimit();
            //如果自动产出上线增加，且资源已经产出最大值，那么修改产出时间，下面则make的时候会生成一下
            if (beforLimit < curLimit && energy.getProducer().getLastTime() == 0)
            {
                energy.getProducer().setLastTime(System.currentTimeMillis());
            }
        }

        //vip加技能点上限
        Resource skillPoint = resourceMap.get(ResourceType.SKILL_POINT);
        if (skillPoint != null && skillPoint.getProducer() != null)
        {
            int vipLv = owner.getVipManager().getVipLevel();
            q_vipBean vip = BeanTemplet.getVipBean(vipLv);
            int beforLimit = skillPoint.getProducer().getLimit();
            if (vip != null)
            {
                skillPoint.getProducer().setLimit(vip.getQ_skill_point_ceil());
                int curLimit = skillPoint.getProducer().getLimit();
                //如果自动产出上线增加，且资源已经产出最大值，那么修改产出时间，下面则make的时候会生成一下
                if (beforLimit < curLimit && skillPoint.getProducer().getLastTime() == 0)
                {
                    skillPoint.getProducer().setLastTime(System.currentTimeMillis());
                }
            }
            else
            {
                LOG.error("VIP 配置变数据缺失。id = " + vipLv);
            }
        }

        List<Resource> list = new ArrayList<>(2);
        for (Entry<ResourceType, Resource> entry : resourceMap.entrySet())
        {
            if (entry.getValue().getProducer() != null)
            {
                entry.getValue().getProducer().make();
                LOG.debug("资源（" + entry.getKey().value()
                        + "）最近产出时间：" + entry.getValue().getProducer().getLastTime());
                list.add(entry.getValue());
            }
        }

        if (notify)
        {
            notifyResourceUpdate(list);
        }
    }

    /**
     * 获取资源列表.
     *
     * @return
     */
    public List<Resource> getResourceList()
    {
        List<Resource> list = new ArrayList<>(resourceMap.size());
        for (Entry<ResourceType, Resource> entry : resourceMap.entrySet())
        {
            list.add(entry.getValue());
        }

        return list;
    }

    /**
     * 获取指定的资源信息.
     *
     * @param type 资源类型
     * @return
     */
    public Resource getResource(int type)
    {
        return getResource(ResourceType.getType(type));
    }

    /**
     * 获取指定的资源信息.
     *
     * @param type 资源类型
     * @return
     */
    public Resource getResource(ResourceType type)
    {
        return type != null ? resourceMap.get(type) : null;
    }

    /**
     * 增加指定资源的数量.
     *
     * @param type 资源类型
     * @param amount 新增数量
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @param nowTime 操作时间
     * @return 修改后的资源信息
     */
    public Resource addResource(int type, int amount,
            boolean notify, Reasons reason, Date nowTime)
    {
        return addResource(ResourceType.getType(type), amount, notify, reason, nowTime);
    }

    /**
     * 增加指定资源的数量.
     *
     * @param type 资源类型
     * @param amount 新增数量
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @param nowTime 操作时间
     * @return 修改后的资源信息
     */
    public Resource addResource(ResourceType type, int amount,
            boolean notify, Reasons reason, Date nowTime)
    {
        if (amount <= 0)
        {
            LOG.warn("fuck，添加资源怎么能小于零。reason = " + reason.getFullDesc() + "amount:" + amount);
            return null;
        }
        Resource res = getResource(type);
        if (res != null)
        {
            //每次getAmount都会重新计算资源的数量
            int before = res.getAmount();
            res.addAmount(amount);

            callMonitor(type, amount, nowTime.getTime());
            //添加资源增加日志
            addResourceLog(res.getType(), amount,
                    before, res.getAmount(), Action.ADD, reason, nowTime);


            if (notify)
            {
                List<Resource> list = new ArrayList<>(1);
                list.add(res);
                notifyResourceUpdate(list);
            }
        }

        return res;
    }

    /**
     * 扣除指定资源的数量.
     *
     * @param type 资源类型
     * @param amount 扣除数量
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @param nowTime 操作时间
     * @return 修改后的资源信息
     */
    public Resource subResource(int type, int amount,
            boolean notify, Reasons reason, Date nowTime)
    {
        return subResource(ResourceType.getType(type), amount, notify, reason, nowTime);
    }

    /**
     * 扣除指定资源的数量.
     *
     * @param type 资源类型
     * @param amount 扣除数量
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @param nowTime 操作时间
     * @return 修改后的资源信息
     */
    public Resource subResource(ResourceType type, int amount,
            boolean notify, Reasons reason, Date nowTime)
    {
        Resource res = getResource(type);
        if (res != null)
        {
            int before = res.getAmount();
            res.subAmount(amount);
            //添加扣除资源日志
            addResourceLog(res.getType(), amount,
                    before, res.getAmount(), Action.REMOVE, reason, nowTime);

            if (notify)
            {
                List<Resource> list = new ArrayList<>(1);
                list.add(res);
                notifyResourceUpdate(list);
            }
        }

        return res;
    }

    /**
     * 通知玩家资源变化.
     *
     * @param list 受影响的资源列表
     */
    public void notifyResourceUpdate(List<Resource> list)
    {
        if (list != null && list.size() > 0)
        {
            BackpackMessage.ResResourceUpdate.Builder message = BackpackMessage.ResResourceUpdate.newBuilder();
            for (Resource obj : list)
            {
                message.addResources(obj.getMessageBuilder());
            }

            MessageUtils.send(owner, new SMessage(
                    BackpackMessage.ResResourceUpdate.MsgID.eMsgID_VALUE, message.build().toByteArray()));
        }
    }

    /**
     * 获取背包中指定格子的物品.
     *
     * @param gridId 格子ID
     * @return 返回一份物品拷贝(修改不影响背包数据)，如果没有物品返回null
     */
    public Item getGrid(int gridId)
    {
        Grid grid = gridMap.get(gridId);
        return grid != null && !grid.isEmpty() ? copy(grid.getItem()) : null;
    }

    /**
     * 获取背包中的物品.
     *
     * @param itemId 道具ID
     * @return 返回一份物品拷贝(修改不影响背包数据)，如果没有物品返回null
     */
    public Item getItem(int itemId)
    {
        Integer gridId = propGrids.get(itemId);
        if (gridId != null)
        {
            Grid grid = gridMap.get(gridId);
            if (grid != null && !grid.isEmpty())
            {
                return copy(grid.getItem());
            }
        }
        return null;
    }

    /**
     * 更新背包中指定格子的物品.
     * <p>
     * ps：只能修改同一物品，且不能修改物品数量！
     *
     * @param gridId 格子ID
     * @param item 物品信息
     * @return 更新成功返回true，失败返回false.
     */
    public boolean updateGrid(int gridId, Item item)
    {
        Grid grid = gridMap.get(gridId);
        if (grid == null)
        {
            LOG.error("没有找到指定格子！gridId = " + gridId);
            return false;
        }

        if (grid.isEmpty())
        {
            LOG.error("指定格子已经为空！gridId = " + gridId);
            return false;
        }

        if (item.getId() != grid.getItem().getId())
        {
            LOG.error("不能修改不同物品！gridId = " + gridId + "，itemId = "
                    + item.getId() + "，beforeId = " + grid.getItem().getId());
            return false;
        }

        if (item.getNum() != grid.getItem().getNum())
        {
            LOG.error("不能修改物品数量！gridId = " + gridId + "，itemNum = "
                    + item.getNum() + "，beforeNum = " + grid.getItem().getNum());
            return false;
        }

        grid.setItem(copy(item));
        List<Grid> grids = new ArrayList<>(1);
        grids.add(grid);
        notifyBackpackUpdate(grids);

        return true;
    }

    /**
     * 将指定格子中的物品全数从背包中移除.
     *
     * @param gridId 格子ID
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @return 错误码，没有发生错误返回null
     */
    public ErrorCode removeGrid(int gridId, boolean notify, Reasons reason)
    {
        Grid grid = gridMap.get(gridId);
        if (grid == null)
            return ErrorCode.GRID_NOT_EXIST;

        if (grid.isEmpty())
            return ErrorCode.ITEM_NOT_EXIST;

        remove(grid, grid.getItem().getNum(),
                reason, Calendar.getInstance().getTime());

        if (notify)
        {
            List<Grid> grids = new ArrayList<>(1);
            grids.add(grid);
            notifyBackpackUpdate(grids);
        }

        return null;
    }

    /**
     * 获取背包中指定物品的数量.
     *
     * @param itemId 物品ID
     * @return
     */
    public int getAmount(int itemId)
    {
        Item item = BeanFactory.createItem(itemId);
        if (item == null)
            return 0;

        int amount = 0;
        List<Grid> grids = this.get(item);
        if (grids != null)
        {
            for (Grid grid : grids)
            {
                amount += grid.getItem().getNum();
            }
        }
        return amount;
    }

    /**
     * 将物品添加到背包.
     * <p>
     * ps：添加数量取决于Item.getNum()，该值必须大于0；如果格子已满，将自动丢弃超出部分.
     *
     * @param item 待添加的物品
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @return 错误码，没有发生错误返回null
     */
    public ErrorCode addItem(Item item, boolean notify, Reasons reason)
    {
        List<Item> list = new ArrayList<>(1);
        list.add(item);
        return addItem(list, notify, reason);
    }

    /**
     * 将物品添加到背包.
     * <p>
     * ps：添加数量取决于Item.getNum()，该值必须大于0；如果格子已满，将自动丢弃超出部分.
     *
     * @param items 待添加的物品列表
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @return 错误码，没有发生错误返回null
     */
    public ErrorCode addItem(List<Item> items, boolean notify, Reasons reason)
    {
        if (items == null || items.isEmpty())
        {
            return ErrorCode.ITEM_NOT_EXIST;
        }

        List<Item> list = new ArrayList<>();
        for (Item item : items)
        {
            if (item == null)
                return ErrorCode.ITEM_NOT_EXIST;
            if (item.getNum() < 1)
                return ErrorCode.ITEM_NUM_ERROR;
            list.add(copy(item));
        }

        Date nowTime = Calendar.getInstance().getTime();
        List<Grid> grids = new ArrayList<>(items.size());
        for (Item obj : list)
        {
            Grid grid = this.add(obj, reason, nowTime);

            if (grid != null)
            {
                grids.add(grid);
            }
        }

        if (notify)
        {
            notifyBackpackUpdate(grids);
        }

        return null;
    }

    /**
     * 将物品从背包中移除.
     * <p>
     * ps：移除数量取决于Item.getNum()，该值必须大于0；如果物品的持有数量不足，则不能进行扣除.
     *
     * @param item 要移除的物品
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @return 错误码，没有发生错误返回null
     */
    public ErrorCode removeItem(Item item, boolean notify, Reasons reason)
    {
        List<Item> list = new ArrayList<>(1);
        list.add(item);
        return removeItem(list, notify, reason);
    }

    /**
     * 将物品从背包中移除.
     * <p>
     * ps：移除数量取决于Item.getNum()，该值必须大于0；如果任一物品的持有数量不足，都不能进行扣除.
     *
     * @param items 要移除的物品列表
     * @param notify 是否通知客户端
     * @param reason 操作原因
     * @return 错误码，没有发生错误返回null
     */
    public ErrorCode removeItem(List<Item> items, boolean notify, Reasons reason)
    {
        if (items == null || items.isEmpty())
        {
            return ErrorCode.ITEM_NOT_EXIST;
        }

        //先进行一次检测，如果任意一物品不足都不能扣除掉
        List<DynamicGrid> list = new ArrayList<>(items.size());
        for (Item item : items)
        {
            if (item == null)
                return ErrorCode.ITEM_NOT_EXIST;
            if (item.getNum() < 1)
                return ErrorCode.ITEM_NUM_ERROR;

            Grid grid = getProp(item.getId());
            if (grid == null || grid.isEmpty()
                    || grid.getItem().getNum() < item.getNum())
            {
                return ErrorCode.ITEM_NOT_ENOUGH;
            }
            list.add(new DynamicGrid(grid, item.getNum()));
        }

        List<Grid> grids = new ArrayList<>(list.size());
        Date nowTime = Calendar.getInstance().getTime();
        for (DynamicGrid obj : list)
        {
            grids.add(obj.getGrid());
            remove(obj.getGrid(), obj.getAmount(), reason, nowTime);
        }

        if (notify)
        {
            notifyBackpackUpdate(grids);
        }

        return null;
    }

    /**
     * 向客户端发送玩家的背包列表.
     */
    public void sendBackpackList()
    {
        BackpackMessage.ResBackpackList.Builder message = BackpackMessage.ResBackpackList.newBuilder();

        if (gridMap.size() > 0)
        {
            for (Entry<Integer, Grid> entry : gridMap.entrySet())
            {
                if (!entry.getValue().isEmpty())
                {
                    Grid obj = entry.getValue();
                    BackpackMessage.Grid.Builder grid = BackpackMessage.Grid.newBuilder();
                    grid.setId(obj.getId());
                    grid.setItem(createItemMsg(obj.getItem()));
                    message.addGrids(grid);
                }
            }
        }

        MessageUtils.send(owner, new SMessage(BackpackMessage.ResBackpackList.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 使用背包中的指定物品.
     *
     * @param request 客户端发送的请求消息
     */
    public void onItemUse(BackpackMessage.ReqItemUse request)
    {
        // 判断使用数量是否正确
        if (request.getNum() < 1)
        {
            notifyItemUseResult(ErrorCode.ITEM_NUM_ERROR, null);
            return;
        }

        // 判断物品数量是否足够
        Grid grid = gridMap.get(request.getGridId());
        if (grid == null || grid.isEmpty()
                || grid.getItem().getNum() < request.getNum())
        {
            notifyItemUseResult(ErrorCode.ITEM_NOT_ENOUGH, null);
            return;
        }

        // 尝试获取卡牌实例对象

        // 判断是否可以使用物品
        Date nowTime = Calendar.getInstance().getTime();
        List<Item> backList = new ArrayList<>(0);
        ErrorCode error = checkItemUse(grid.getItem(),
                request.getNum(), owner, nowTime, backList);
        if (error != null)
        {
            notifyItemUseResult(error, null);
            return;
        }

        // 执行具体逻辑前先拷贝物品数据
        Item item = copy(grid.getItem());
        item.setNum(request.getNum());

        // 扣除物品后通知客户端操作成功
        remove(grid, request.getNum(), Reasons.ITEM_USE, nowTime);
        notifyItemUseResult(null, item.getId());

        // 通知客户端发生变更的格子信息
        List<Grid> list = new ArrayList<>(1);
        list.add(grid);
        notifyBackpackUpdate(list);

        // 自动回收解锁物品并通知客户端（徐能强：backList指使用后需要扣除的道具列表，如：宝箱需要钥匙）
        if (backList.size() > 0)
        {
            error = removeItem(backList, true, Reasons.ITEM_RECOVERY);
            if (error != null)
            {
                LOG.error("物品回收失败！error：" + error.name() + "(" + error.getCode() + ")");
            }
        }

        // 计算物品使用效果并通知客户端
        boolean successful = item.doUse(owner);

        if (!successful)
        {
            LOG.error("物品使用无效！itemId = " + item.getId() + "，type = " + item.getType() + "，name = " + item.getName());
        }
    }

    public void onTestAddAllItem(BackpackMessage.ReqTestAddAllItem request)
    {
        List<q_itemBean> allItems = GameDataManager.getInstance().q_itemContainer.getList();
        List<Item> list = new ArrayList<>();
        for (q_itemBean itemBean : allItems)
        {
            if (itemBean != null)
            {
                Item item = BeanFactory.createItem(itemBean.getQ_id(), 500);
                list.add(item);
            }
        }
        addItem(list, true, Reasons.ITEM_BUY);
    }

    /**
     * 使用背包中的经验道具.
     *
     * @param request 客户端发送的请求消息
     */
    public void onExpPropUse(BackpackMessage.ReqExpPropUse request)
    {
        // 判断道具个数是否正确
        if (request.getItemsCount() == 0)
        {
            notifyExpPropUseResult(ErrorCode.ITEM_NUM_ERROR);
            return;
        }

        // 判断道具种类是否重复
        Set<Integer> checkItems = new HashSet<>();
        for (BackpackMessage.Item item : request.getItemsList())
        {
            if (item.getNum() < 1)
            {
                notifyExpPropUseResult(ErrorCode.ITEM_NUM_ERROR);
                checkItems.clear();
                return;
            }

            if (!checkItems.add(item.getId()))
            {
                notifyExpPropUseResult(ErrorCode.PROPS_DOPLICATE);
                checkItems.clear();
                return;
            }
        }
        checkItems.clear();

        // 从背包中获取道具列表
        int expTotal = 0;
        List<Grid> grids = new ArrayList<>(request.getItemsCount());
        for (BackpackMessage.Item obj : request.getItemsList())
        {
            Grid g = getProp(obj.getId());
            if (g == null)
            {
                notifyExpPropUseResult(ErrorCode.ITEM_NOT_EXIST);
                return;
            }
            if (g.getItem().getNum() < obj.getNum())
            {
                notifyExpPropUseResult(ErrorCode.ITEM_NOT_ENOUGH);
                return;
            }
            if (!ItemType.EXP.compare(g.getItem().getType()))
            {
                notifyExpPropUseResult(ErrorCode.OPERATION_FAILED);
                return;
            }

            grids.add(g);
            expTotal = expTotal + ((ExpProp) g.getItem()).getAddExp() * obj.getNum();
        }

        // 扣除物品后通知客户端操作成功
        Date nowTime = Calendar.getInstance().getTime();
        for (int i = 0; i < grids.size() && i < request.getItemsCount(); i++)
        {
            remove(grids.get(i), request.getItems(i).getNum(), Reasons.ITEM_USE, nowTime);
        }
        notifyExpPropUseResult(null);

        // 通知客户端发生变更的格子信息
        notifyBackpackUpdate(grids);
    }

    /**
     * 出售背包中的指定物品.
     *
     * @param request 客户端发送的请求消息
     */
    public void onItemSell(BackpackMessage.ReqItemSell request)
    {
        // 判断使用数量是否正确
        if (request.getNum() < 1)
        {
            notifyItemSellResult(ErrorCode.ITEM_NUM_ERROR, null);
            return;
        }

        // 判断物品数量是否足够
        Grid grid = gridMap.get(request.getGridId());
        if (grid == null || grid.isEmpty()
                || grid.getItem().getNum() < request.getNum())
        {
            notifyItemSellResult(ErrorCode.ITEM_NOT_ENOUGH, null);
            return;
        }

        int price = grid.getItem().getSellPrice();

        // 扣除物品前先计算可获得的金币
        int gold = price * request.getNum();

        // 扣除物品后通知客户端操作成功
        Date nowTime = Calendar.getInstance().getTime();
        remove(grid, request.getNum(), Reasons.ITEM_SELL, nowTime);
        notifyItemSellResult(null, gold);

        // 通知客户端发生变更的格子信息
        List<Grid> list = new ArrayList<>(1);
        list.add(grid);
        notifyBackpackUpdate(list);

        // 计算获得的金币并返回更新消息
        addResource(ResourceType.GOLD, gold, true, Reasons.ITEM_SELL, nowTime);
    }

    /**
     * 分解物品.
     *
     * @param request 客户端发送的请求消息
     */
    public void onItemCell(BackpackMessage.ReqItemCell request)
    {
        // 判断使用数量是否正确
        if (request.getNum() < 1)
        {
            notifyItemCellResult(ErrorCode.ITEM_NUM_ERROR, request, null);
            return;
        }

        // 判断物品数量是否足够
        Grid grid = gridMap.get(request.getGridId());
        if (grid == null || grid.isEmpty()
                || grid.getItem().getNum() < request.getNum())
        {
            notifyItemCellResult(ErrorCode.ITEM_NOT_ENOUGH, request, null);
            return;
        }
    }

    /**
     * 合成物品.
     *
     * @param request 客户端发送的请求消息
     */
    public void onItemCombine(BackpackMessage.ReqItemCombine request)
    {
        // 判断使用数量是否正确
        if (request.getNum() < 1)
        {
            notifyItemCombineResult(ErrorCode.ITEM_NUM_ERROR, null, request.getNum(), null);
            return;
        }

        // 判断物品数量是否足够
        Grid grid = gridMap.get(request.getGridId());
        if (grid == null || grid.isEmpty())
        {
            notifyItemCombineResult(ErrorCode.ITEM_NOT_ENOUGH, null, request.getNum(), null);
            return;
        }

        DynamicItem target = null; // 要合成的目标物品
        List<DynamicItem> reqList = null; // 合成所需要的材料

        Integer type;
        if (ItemType.DRAWING.compare(grid.getItem().getType()))
        {
            type = 1;
        }
        else if (ItemType.DEBRIS.compare(grid.getItem().getType()))
        {

            type = 2;
        }
        else
        {

            type = 3;
        }

        if (target == null || reqList == null || reqList.isEmpty())
        {
            // 不能合成
            notifyItemCombineResult(ErrorCode.OPERATION_FAILED, type, request.getNum(), null);
        }
        else
        {
            // 开始合成
            doCombine(type, request.getNum(), target, reqList);
        }
    }

    /**
     * 碎片融合.
     *
     * @param request
     */
    public void onDebrisCombine(BackpackMessage.ReqDebrisCombine request)
    {
        // 判断使用数量是否正确
        if (request.getNum() < 1)
        {
            notifyItemCombineResult(ErrorCode.ITEM_NUM_ERROR, 2, request.getNum(), null);
            return;
        }

        DynamicItem target = null; // 要合成的目标物品
        List<DynamicItem> reqList = null; // 合成所需要的材料
        if (target == null || reqList == null || reqList.isEmpty())
        {
            // 不能合成
            notifyItemCombineResult(ErrorCode.OPERATION_FAILED, 2, request.getNum(), null);
        }
        else
        {
            // 开始合成
            doCombine(2, request.getNum(), target, reqList);
        }
    }

    @Override
    public JSON toJson()
    {
        JSONObject data = new JSONObject();

        // 序列化资源信息
        JSONArray jsonArr1 = new JSONArray();
        for (Entry<ResourceType, Resource> entry : resourceMap.entrySet())
        {
            jsonArr1.add(entry.getValue().toJson());
        }
        data.put(JSON_KEY_RESOURCE, jsonArr1);

        // 序列化物品信息
        JSONArray jsonArr2 = new JSONArray();
        for (Entry<Integer, Grid> entry : gridMap.entrySet())
        {
            if (!entry.getValue().isEmpty())
            {
                jsonArr2.add(entry.getValue().getItem().toJson());
            }
        }
        data.put(JSON_KEY_ITEM, jsonArr2);

        // 序列化资源监控数据
        JSONArray jsonArr3 = new JSONArray();
        for (Entry<ResourceType, ProduceMonit> entry : resourceMonitor.entrySet())
        {
            jsonArr3.add(entry.getValue().toJson());
        }
        data.put(JSON_KEY_RESOURCE_MONITOR, jsonArr3);

        // 序列化物品监控数据
        JSONArray jsonArr4 = new JSONArray();
        for (Entry<Integer, ProduceMonit> entry : itemMonitor.entrySet())
        {
            // 丢弃背包中已经没有的物品的监控数据
            if (getAmount(entry.getValue().getId()) > 0)
            {
                jsonArr4.add(entry.getValue().toJson());
            }
        }
        data.put(JSON_KEY_ITEM_MONITOR, jsonArr4);

        return data;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject data = (JSONObject) json;

        // 反序列化资源信息
        JSONArray jsonArr1 = data.getJSONArray(JSON_KEY_RESOURCE);
        for (Object obj : jsonArr1)
        {
            JSONObject jsonObj = (JSONObject) obj;

            ResourceType type = ResourceType.getType(jsonObj.getIntValue("type"));
            if (type == null)
            {
                throw new JSONException("Not found resource type! type = " + type);
            }

            Resource resource = resourceMap.get(type);
            if (resource == null)
            {
                throw new JSONException("Not found resource type! type = " + type);
            }

            resource.setAmount(jsonObj.getIntValue("num"));

            //不等于空的情况：只有体力和精力包含Producer
            if (resource.getProducer() != null)
            {
                // 读取并设置最近一次资源产出时间
                if (jsonObj.containsKey("time"))
                {
                    resource.getProducer().setLastTime(jsonObj.getLong("time"));
                    LOG.debug("读取资源（" + type.value()
                            + "）最近产出时间：" + resource.getProducer().getLastTime());
                }
                else
                {
                    if (resource.getAmount() < resource.getProducer().getLimit())
                    {
                        resource.getProducer().setLastTime(System.currentTimeMillis());
                        LOG.debug("设置资源（" + type.value()
                                + "）最近产出时间：" + resource.getProducer().getLastTime());
                    }
                    else
                    {
                        resource.getProducer().setLastTime(0);
                        LOG.debug("设置资源（" + type.value()
                                + "）最近产出时间：" + resource.getProducer().getLastTime());
                    }
                }
            }
        }

        // 反序列化物品信息
        JSONArray jsonArr2 = data.getJSONArray(JSON_KEY_ITEM);
        for (Object obj : jsonArr2)
        {
            JSONObject jsonObj = (JSONObject) obj;
            Item item = BeanFactory.createItem(jsonObj);
            if (item != null)
            {
                //在创建Grid的时候，就已经向gridMap添加了Gird对象，这里只需对该引用赋值就行
                Grid grid = newGridEmpty();
                grid.setItem(item);
                propGrids.put(item.getId(), grid.getId());
            }
        }

        // 反序列化资源监控数据
        JSONArray jsonArr3 = data.getJSONArray(JSON_KEY_RESOURCE_MONITOR);
        if (jsonArr3 != null && jsonArr3.size() > 0)
        {
            for (Object obj : jsonArr3)
            {
                JSONObject jsonObj = (JSONObject) obj;

                ResourceType type = ResourceType.getType(jsonObj.getIntValue("id"));
                if (type == null)
                {
                    throw new JSONException("Not found resource type! type = " + type);
                }

                ProduceMonit monit = resourceMonitor.get(type);
                if (monit == null)
                {
                    throw new JSONException("Not found resource type! type = " + type);
                }

                monit.setCount(jsonObj.getIntValue("count"));
                monit.setStartTime(jsonObj.getLongValue("startTime"));
            }
        }

        // 反序列化物品监控数据
        JSONArray jsonArr4 = data.getJSONArray(JSON_KEY_ITEM_MONITOR);
        if (jsonArr4 != null && jsonArr4.size() > 0)
        {
            for (Object obj : jsonArr4)
            {
                JSONObject jsonObj = (JSONObject) obj;

                ProduceMonit monit = new ProduceMonit();
                monit.fromJson(jsonObj);
                itemMonitor.put(monit.getId(), monit);
            }
        }
    }

    /**
     * 获取指定物品.
     *
     * @param item 物品ID
     * @return 物品存放到背包中的格子信息，如果背包中没有指定物品则返回null
     */
    private List<Grid> get(int itemId)
    {
        Item item = BeanFactory.createItem(itemId);
        return item != null ? get(item) : null;

    }

    /**
     * 获取指定物品.
     *
     * @param item 指定物品
     * @return 物品存放到背包中的格子信息，如果背包中没有指定物品则返回null
     */
    private List<Grid> get(Item item)
    {
        List<Grid> list = null;
        // 非装备类物品可以堆叠，从关联表中快速查找
        Integer gridId = propGrids.get(item.getId());
        if (gridId != null)
        {
            Grid grid = gridMap.get(gridId);
            if (grid != null)
            {
                list = new ArrayList<>(1);
                list.add(grid);
            }
        }
        return list;
    }

    /**
     * 获取指定道具（非装备类物品）.
     *
     * @param itemId 物品ID
     * @return 道具：是一个gridMap中的引用
     */
    private Grid getProp(int itemId)
    {
        // 非装备类物品可以堆叠，从关联表中快速查找
        Integer gridId = propGrids.get(itemId);
        return gridId != null ? gridMap.get(gridId) : null;
    }

    public int getGridId(int itemId)
    {
        return propGrids.get(itemId);
    }

    /**
     * 将指定物品添加到背包.
     *
     * @param item 指定物品
     * @param reason 操作原因
     * @param date 操作时间
     * @return 物品添加到的目标格子：<br>
     * 如果返回null，表示目标格子已满，此次添加失败；<br>
     * 如果返回了目标格子，但isFull为true，表示全部或部分添加成功，且格子已满（可能需要告诉客户端提醒玩家）
     */
    private Grid add(Item item, Reasons reason, Date date)
    {
        Grid grid;
        Item itemBefore = null;

        // 非装备类物品可以进行叠加，寻找是否已包含该物品
        Integer gridId = propGrids.get(item.getId());
        if (gridId != null)
        {
            // 已找到存放该物品的格子
            grid = gridMap.get(gridId);
            // 如果格子已满，放弃添加
            if (grid.isFull())
            {
                return null;
            }

            // 修改前先拷贝
            itemBefore = copy(grid.getItem());
            // 修改物品数量
            grid.getItem().addNum(item.getNum());
        }
        else
        {
            // 背包中尚没有该物品，获取一个空格子来存放
            grid = getGridEmpty();
            // 没有空的格子则新建
            if (grid == null)
            {
                grid = newGridEmpty();
            }
            grid.setItem(copy(item));
            // 关联叠加物品与格子
            propGrids.put(item.getId(), grid.getId());
        }
        callMonitor(item, item.getNum(), date.getTime());

        // 添加物品的变更日志
        if (item.isLog())
        {
            addItemLog(item.getId(), item.getNum(), itemBefore, grid.getItem(), Action.ADD, reason, date);
        }

        return grid;
    }

    /**
     * 扣除指定数量的物品，如果数量被扣光，格子将被重置为空.
     *
     * @param grid 物品所在的格子
     * @param amount 要扣除的物品数量
     * @param reason 操作原因
     * @param date 操作时间
     */
    private void remove(Grid grid, int amount, Reasons reason, Date date)
    {
        if (!grid.isEmpty())
        {
            int itemId = grid.getItem().getId();
            boolean isLog = grid.getItem().isLog();
            Item itemBefore = copy(grid.getItem());

            // 修改物品数量
            grid.getItem().setNum(grid.getItem().getNum() - amount);
            if (grid.getItem().getNum() <= 0)
            {
                // 将指定格子置为空
                propGrids.remove(grid.getItem().getId());
                grid.setItem(null);
            }

            // 添加物品的删除日志
            if (isLog)
            {
                addItemLog(itemId, amount, itemBefore, grid.getItem(), Action.REMOVE, reason, date);
            }
        }
    }

    /**
     * 获取一个空格子.
     *
     * @return
     */
    private Grid getGridEmpty()
    {
        for (Entry<Integer, Grid> entry : gridMap.entrySet())
        {
            if (entry.getValue().isEmpty())
                return entry.getValue();
        }

        return null;
    }

    /**
     * 新建一个空格子.
     *
     * @return
     */
    private Grid newGridEmpty()
    {
        Grid grid = new Grid();
        grid.setId(gridMap.size() + 1);
        gridMap.put(grid.getId(), grid);

        return grid;
    }

    /**
     * 拷贝一个物品副本.
     *
     * @param item 指定物品
     * @return 拷贝失败返回null
     */
    private Item copy(Item item)
    {
        try
        {
            return (Item) item.clone();
        }
        catch (CloneNotSupportedException e)
        {
            LOG.error("复制物品失败！cause: ", e);
            return null;
        }
    }

    /**
     * 添加物品变更日志.
     *
     * @param itemId 物品ID
     * @param amount 变更数量
     * @param itemBefore 变更之前的物品信息（可以为空，比如添加时首次获得该物品）
     * @param itemAfter 变更之后的物品信息（可以为空，比如删除后没有剩余物品）
     * @param action 操作动作
     * @param reason 操作原因
     * @param time 操作时间
     */
    private void addItemLog(int itemId, int amount,
            Item itemBefore, Item itemAfter, Action action, Reasons reason, Date time)
    {
        int modify = (Action.REMOVE == action) ? (amount * -1) : amount;
        int type = -1;
        if (itemBefore != null)
            type = itemBefore.getType();
        else if (itemAfter != null)
            type = itemAfter.getType();

        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String reasonType;
            private String reasonGroup;
            private String reason;
            private String itemType;
            private String itemId;
            private String uniqueId;
            private String itemCnt;
            private Date date;

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String reasonType, String reasonGroup, String reason,
                    String itemType, String itemId, String uniqueId, String itemCnt,
                    Date date)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.reasonType = reasonType;
                this.reasonGroup = reasonGroup;
                this.reason = reason;
                this.itemType = itemType;
                this.itemId = itemId;
                this.uniqueId = uniqueId;
                this.itemCnt = itemCnt;
                this.date = date;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogPropsChange(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        reasonType,
                        reasonGroup,
                        reason,
                        itemType,
                        itemId,
                        uniqueId,
                        itemCnt,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(owner.getFgi(), Integer.toString(owner.getServerId()), UniqueId.toBase36(owner.getRoleId()), owner.getFedId(),
                reason.getFullDesc(), reason.getFullDesc(), reason.getFullDesc(),
                Integer.toString(type), Integer.toString(itemId), Integer.toString(itemId), Integer.toString(modify), time));
    }

    /**
     * 添加资源变更日志.
     *
     * @param type 资源类型
     * @param amount 变更数量
     * @param before 资源变更之前的数量
     * @param after 资源变更之后的数量
     * @param action 操作动作
     * @param reason 操作原因
     * @param time 操作时间
     */
    private void addResourceLog(int type, int amount,
            int before, int after, Action action, Reasons reason, Date time)
    {
        int modify = (Action.REMOVE == action) ? (amount * -1) : amount;

        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String reasonType;
            private String reasonGroup;
            private String reason;
            private String coinType;
            private String coinNum;
            private String left;
            private Date date;

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String reasonType, String reasonGroup, String reason,
                    String coinType, String coinNum, String left, Date date)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.reasonType = reasonType;
                this.reasonGroup = reasonGroup;
                this.reason = reason;
                this.coinType = coinType;
                this.coinNum = coinNum;
                this.left = left;
                this.date = date;
                if (this.date == null)
                    this.date = new Date();

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogCoinChange(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        reasonType,
                        reasonGroup,
                        reason,
                        coinType,
                        coinNum,
                        left,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(owner.getFgi(), Integer.toString(owner.getServerId()), UniqueId.toBase36(owner.getRoleId()), owner.getFedId(),
                reason.getFullDesc(), reason.getFullDesc(), reason.getFullDesc(),
                Integer.toString(type), Integer.toString(modify), Integer.toString(after), time));
    }

    /**
     * 将动态物品类添加到列表.
     *
     * @param list 目标列表，符合条件的物品将被添加到此
     * @param item 指定的动态物品类
     */
    private void addDynamicItem(List<DynamicItem> list, DynamicItem item)
    {
        if (item.getAmount() > 0)
        {
            if (item.getProbability() != -1)
            {
                if (item.getProbability() > 0)
                {
                    list.add(item);
                }
            }
            else
            {
                list.add(item);
            }
        }
    }

    /**
     * 检查指定物品是否能被使用（但不包含背包中现有数量的判断）.
     *
     * @param item 指定物品
     * @param useNum 使用数量
     * @param player 目标玩家
     * @param nowTime 当前时间
     * @param backList 物品回收列表，验证通过后将需要系统回收的解锁物品填充到该列表，null表示不进行解锁物品的验证
     * @return 验证通过返回null，否则返回对应的错误码
     */
    private ErrorCode checkItemUse(Item item, int useNum,
            Player player, Date nowTime, List<Item> backList)
    {
        // 物品不能使用
        if (!item.isCanUse())
        {
            return ErrorCode.ITEM_CAN_NOT_USE;
        }

        // 角色等级不够
        if (player.getRoleLevel() < item.getLevel())
        {
            return ErrorCode.ROLE_LEVEL_NOT_ENOUGH;
        }

        // 物品已经过期
        Date[] useTime = item.getUseTimeLimit();
        if (useTime != null)
        {
            if (DateUtils.before(nowTime, useTime[0]))
            {
                return ErrorCode.ITEM_NOT_USE_TIME;
            }
            if (DateUtils.after(nowTime, useTime[1]))
            {
                return ErrorCode.ITEM_IS_EXPIRED;
            }
        }


        // 判断使用物品时是否需要解锁物品(宝箱)
        if (backList != null)
        {
            Item[] keyItems = item.getRequiredItems();
            if (keyItems != null && keyItems.length > 0)
            {
                for (Item key : keyItems)
                {
                    Grid grid = getProp(key.getId());
                    // 没有解锁物品或者数量不够
                    if (grid == null || grid.isEmpty()
                            || grid.getItem().getNum() < key.getNum() * useNum)
                    {
                        return ErrorCode.NOT_UNLOCK_ITEM;
                    }
                    key.setNum(key.getNum() * useNum);
                    // 将解锁物品添加到回收列表
                    backList.add(key);
                }
            }
        }

        return null;
    }

    /**
     * 物品合成的具体算法.
     *
     * @param type 合成类型
     * @param num 合成数量
     * @param target 要合成的目标物品，包含合成数量、价格、成功率等
     * @param reqList 合成所需要的物品列表，包含每个材料所需的总数等
     */
    private void doCombine(Integer type, int num, DynamicItem target, List<DynamicItem> reqList)
    {
        // 合成所需要的金币不够
        if (owner.getGold() < target.getPrice())
        {
            notifyItemCombineResult(ErrorCode.GOLD_NOT_ENOUGH, type, num, null);
            return;
        }

        // 获取合成所需要的物品
        List<Grid> grids = new ArrayList<>(reqList.size());
        for (DynamicItem req : reqList)
        {
            Grid g = getProp(req.getItemId());
            if (g != null && g.getItem().getNum() >= req.getAmount())
            {
                grids.add(g);
            }
            else
            {
                notifyItemCombineResult(ErrorCode.ITEM_NOT_ENOUGH, type, num, null);
                return;
            }
        }

        // 计算物品合成是否成功
        Item subItem = null;
        if (random.probability(target.getProbability()))
        {
            subItem = BeanFactory.createItem(target.getItemId(), target.getAmount());
        }

        // 扣除材料后通知客户端操作成功（合成失败也扣除，下面的金币同理）
        Date nowTime = Calendar.getInstance().getTime();
        for (byte i = 0; i < grids.size() && i < reqList.size(); i++)
        {
            remove(grids.get(i), reqList.get(i).getAmount(), Reasons.ITEM_COMBINE, nowTime);
        }

        // 通知客户端发生变更的格子信息
        notifyBackpackUpdate(grids);

        // 计算扣除的金币并返回更新消息
        subResource(ResourceType.GOLD,
                target.getPrice(), true, Reasons.ITEM_COMBINE, nowTime);

        // 将合成出的物品放入玩家背包中
        List<Grid> newGrilds = new ArrayList<>(1);
        if (subItem != null)
        {
            for (int i = 0; i < subItem.getNum(); i++)
            {
                Item item = copy(subItem);
                if (item != null)
                {
                    item.setNum(1);
                    Grid g = add(item, Reasons.ITEM_COMBINE, nowTime);
                    if (g != null)
                    {
                        newGrilds.add(g);
                    }
                }
            }
        }

        // 通知客户端发生变更的格子信息
        notifyBackpackUpdate(newGrilds);
        notifyItemCombineResult(null, type, num, subItem);
    }

    /**
     * 通知玩家背包变化.
     *
     * @param list 受影响的格子列表
     */
    private void notifyBackpackUpdate(List<Grid> list)
    {
        BackpackMessage.ResBackpackUpdate.Builder message = BackpackMessage.ResBackpackUpdate.newBuilder();
        for (Grid obj : list)
        {
            BackpackMessage.Grid.Builder grid = BackpackMessage.Grid.newBuilder();
            grid.setId(obj.getId());

            if (!obj.isEmpty())
            {
                grid.setItem(createItemMsg(obj.getItem()));
            }

            message.addGrids(grid);
        }

        MessageUtils.send(owner, new SMessage(BackpackMessage.ResBackpackUpdate.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 通知玩家物品使用结果是否成功.
     *
     * @param error 错误码，null表示使用成功
     * @param itemId 使用的物品ID
     */
    private void notifyItemUseResult(ErrorCode error, Integer itemId)
    {
        BackpackMessage.ResItemUse.Builder message = BackpackMessage.ResItemUse.newBuilder();
        if (error != null)
        {
            message.setError(error.getCode());
        }
        if (itemId != null)
        {
            message.setItemId(itemId);
        }

        MessageUtils.send(owner, new SMessage(
                BackpackMessage.ResItemUse.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 通知玩家经验道具使用结果是否成功.
     *
     * @param error 错误码，null表示使用成功
     */
    private void notifyExpPropUseResult(ErrorCode error)
    {
        BackpackMessage.ResExpPropUse.Builder message = BackpackMessage.ResExpPropUse.newBuilder();
        if (error != null)
        {
            message.setError(error.getCode());
        }

        MessageUtils.send(owner, new SMessage(
                BackpackMessage.ResExpPropUse.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 通知玩家出售物品是否成功.
     *
     * @param error 错误码，null表示使用成功
     */
    private void notifyItemSellResult(ErrorCode error, Integer gold)
    {
        BackpackMessage.ResItemSell.Builder message = BackpackMessage.ResItemSell.newBuilder();
        if (error != null)
        {
            message.setError(error.getCode());
        }
        else
        {
            message.setGold(gold);
        }

        MessageUtils.send(owner, new SMessage(
                BackpackMessage.ResItemSell.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 通知玩家分解物品是否成功.
     *
     * @param error 错误码，null表示使用成功
     * @param request 客户端发送的请求消息
     * @param list 成功分解出的物品列表
     */
    private void notifyItemCellResult(ErrorCode error, BackpackMessage.ReqItemCell request, List<Item> list)
    {
        BackpackMessage.ResItemCell.Builder message = BackpackMessage.ResItemCell.newBuilder();
        message.setNum(request.getNum());
        if (error != null)
        {
            message.setError(error.getCode());
        }
        if (list != null)
        {
            for (Item obj : list)
            {
                message.addItems(createItemMsg(obj));
            }
        }

        MessageUtils.send(owner, new SMessage(
                BackpackMessage.ResItemCell.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 通知玩家合成物品是否成功.
     *
     * @param error 错误码，null表示使用成功
     * @param type 合成类型：1 = 图纸合成；2 = 碎片融合；3 = 一般合成
     * @param num 合成数量
     * @param obj 成功合成出的物品信息
     */
    private void notifyItemCombineResult(ErrorCode error, Integer type, int num, Item obj)
    {
        BackpackMessage.ResItemCombine.Builder message = BackpackMessage.ResItemCombine.newBuilder();
        message.setNum(num);
        if (type != null)
        {
            message.setType(type);
        }
        if (error != null)
        {
            message.setError(error.getCode());
        }
        if (obj != null)
        {
            message.setItem(createItemMsg(obj));
        }

        MessageUtils.send(owner, new SMessage(
                BackpackMessage.ResItemCombine.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 创建物品消息对象.
     *
     * @param item 指定物品
     */
    private BackpackMessage.Item.Builder createItemMsg(Item item)
    {
        BackpackMessage.Item.Builder msg = BackpackMessage.Item.newBuilder();
        msg.setId(item.getId());
        msg.setNum(item.getNum());
        msg.setType(item.getType());

        return msg;
    }

    /**
     * 背包格子类
     */
    public class Grid
    {

        private int id; // 格子ID

        private Item item; // 格子存放的物品，可以为null

        /**
         * 获取格子ID.
         *
         * @return
         */
        public int getId()
        {
            return id;
        }

        void setId(int id)
        {
            this.id = id;
        }

        /**
         * 获取格子存放的物品，可能为null.
         *
         * @return
         */
        public Item getItem()
        {
            return item;
        }

        void setItem(Item item)
        {
            this.item = item;
        }

        /**
         * 判断格子是否为空.
         * <p>
         * 为空的情况：道具不存在或者道具数量为0
         *
         * @return
         */
        public boolean isEmpty()
        {
            return null == item || item.getNum() == 0;
        }

        /**
         * 判断格子是否已满.
         *
         * @return
         */
        public boolean isFull()
        {
            if (item != null)
            {
                return item.getNum() == item.getNumMax();
            }
            else
            {
                return false;
            }
        }

    }

    /**
     * 背包操作的动作定义.
     */
    public enum Action
    {
        /**
         * 添加
         */
        ADD("add"),
        /**
         * 删除
         */
        REMOVE("remove");

        private final String value;

        Action(String value)
        {
            this.value = value;
        }

        public String value()
        {
            return value;
        }

    }

    /**
     * 物品使用目标定义.
     */
    public enum UseTarget
    {
        /**
         * 无目标（目前无用，留作以后扩展）
         */
        NONE((byte) 0),
        /**
         * 玩家自己
         */
        ROLE((byte) 1),
        /**
         * 玩家卡牌
         */
        CARD((byte) 2);

        private final byte value;

        UseTarget(byte value)
        {
            this.value = value;
        }

        public byte value()
        {
            return value;
        }

    }

    /**
     * 动态格子类.
     */
    class DynamicGrid
    {

        private final Grid grid;

        private final int amount;

        DynamicGrid(Grid grid, int amount)
        {
            this.grid = grid;
            this.amount = amount;
        }

        Grid getGrid()
        {
            return grid;
        }

        int getAmount()
        {
            return amount;
        }

    }

    /**
     * 动态物品类.
     */
    class DynamicItem
    {

        private final int itemId;

        private final int amount;

        private final int price;

        private final int probability;

        DynamicItem(int itemId, int amount)
        {
            this.itemId = itemId;
            this.amount = amount;
            price = 0;
            probability = -1;
        }

        DynamicItem(int itemId, int amount, int price)
        {
            this.itemId = itemId;
            this.amount = amount;
            this.price = price;
            probability = -1;
        }

        DynamicItem(int itemId, int amount, int price, int probability)
        {
            this.itemId = itemId;
            this.amount = amount;
            this.price = price;
            this.probability = probability;
        }

        int getItemId()
        {
            return itemId;
        }

        int getAmount()
        {
            return amount;
        }

        int getPrice()
        {
            return price;
        }

        int getProbability()
        {
            return probability;
        }

    }

    /**
     * 资源或者物品产出的监控信息.
     */
    class ProduceMonit implements IJsonConverter
    {

        private int id; // 监控的资源类型或物品ID

        private int count; // 统计监控期获得的数量

        private long startTime; // 监控的起始时间

        public ProduceMonit()
        {
        }

        @Override
        public JSON toJson()
        {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", id);
            jsonObj.put("count", count);
            jsonObj.put("startTime", startTime);

            return jsonObj;
        }

        @Override
        public void fromJson(JSON json)
        {
            JSONObject jsonObj = (JSONObject) json;
            if (jsonObj != null)
            {
                id = jsonObj.getIntValue("id");
                count = jsonObj.getIntValue("count");
                startTime = jsonObj.getLongValue("startTime");
            }
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getCount()
        {
            return count;
        }

        public void setCount(int count)
        {
            this.count = count;
        }

        public long getStartTime()
        {
            return startTime;
        }

        public void setStartTime(long startTime)
        {
            this.startTime = startTime;
        }

    }

    /**
     * 调用资源产出的监测器.
     *
     * @param type 资源类型
     * @param amount 产出数量
     * @param nowTime 产出时间
     * @return 如果产出异常返回true, 产出正常返回false
     */
    private boolean callMonitor(ResourceType type, int amount, long nowTime)
    {
        //配置表中资源产出的最大值
        int limit = BeanTemplet.getResourceProduceLimit(type.value());
        if (limit > 0)
        {
            ProduceMonit monit = resourceMonitor.get(type);

            if (DateUtils.before(monit.getStartTime(), nowTime)
                    && DateUtils.getDiffMinutes(
                            monit.getStartTime(), nowTime) <= resourceMonitTime)
            {
                // 统计时间段内获得的资源
                monit.setCount(monit.getCount() + amount);
                return checkException(type, monit, limit, nowTime);
            }
            else
            {
                // 不在时间段内则重置数据
                monit.setStartTime(nowTime);
                monit.setCount(amount);
                LOG.info("重置资源(" + type.getName() + ")监测数据！"
                        + "count = " + monit.getCount() + "，startTime = "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(monit.getStartTime())));
                return checkException(type, monit, limit, nowTime);
            }
        }

        return false;
    }

    /**
     * 调用物品产出的监测器.
     *
     * @param type 资源类型
     * @param amount 产出数量
     * @param nowTime 产出时间
     * @return 如果产出异常返回true, 产出正常返回false
     */
    private boolean callMonitor(Item item, int amount, long nowTime)
    {
        int limit = BeanTemplet.getItemProduceLimit(item.getId());
        if (limit > 0)
        {
            ProduceMonit monit = itemMonitor.get(item.getId());
            if (monit == null)
            {
                // 没有监测数据则新建并存储
                monit = new ProduceMonit();
                monit.setId(item.getId());
                monit.setCount(amount);
                monit.setStartTime(nowTime);
                itemMonitor.put(item.getId(), monit);
                LOG.info("创建物品监测数据！itemId = " + monit.getId()
                        + "，count = " + monit.getCount() + "，startTime = "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(monit.getStartTime())));
                return checkException(item, monit, limit, nowTime);
            }
            else
            {
                if (DateUtils.before(monit.getStartTime(), nowTime)
                        && DateUtils.getDiffMinutes(
                                monit.getStartTime(), nowTime) <= itemMonitTime)
                {
                    // 统计时间段内获得的资源
                    monit.setCount(monit.getCount() + amount);
                    return checkException(item, monit, limit, nowTime);
                }
                else
                {
                    // 不在时间段内则重置数据
                    monit.setStartTime(nowTime);
                    monit.setCount(amount);
                    LOG.info("重置物品监测数据！itemId = " + monit.getId()
                            + "，count = " + monit.getCount() + "，startTime = "
                            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(monit.getStartTime())));
                    return checkException(item, monit, limit, nowTime);
                }
            }
        }

        return false;
    }

    /**
     * 检查资源是否产出异常.
     *
     * @param type 资源类型
     * @param monit 监控信息
     * @param limit 正常上限
     * @param nowTime 当前时间
     * @return 如果产出异常返回true, 产出正常返回false
     */
    private boolean checkException(ResourceType type, ProduceMonit monit, int limit, long nowTime)
    {
        // 超出上限则说明产出异常
        if (monit.getCount() > limit)
        {
            LOG.warn("资源(" + type.getName() + ")产出异常！roleName = "
                    + owner.getRoleName() + "，limit = " + limit + "，count = " + monit.getCount());

            ProduceExceptionBean bean = new ProduceExceptionBean();
            bean.setType(1);
            bean.setTarget(monit.getId());
            bean.setCount(monit.getCount());
            bean.setStartTime(new Date(monit.getStartTime()));
            bean.setEndTime(new Date(nowTime));
            bean.setRoleId(UniqueId.toBase36(owner.getRoleId()));

            // 重置监控数据
            monit.setStartTime(nowTime);
            monit.setCount(0);

            GameDBOperator.getInstance().submitRequest(
                    new ProduceExceptionHandler(owner.getLineId(), bean));
            return true;
        }

        return false;
    }

    /**
     * 检查物品是否产出异常.
     *
     * @param item 指定物品
     * @param monit 监控信息
     * @param limit 正常上限
     * @param nowTime 当前时间
     * @return 如果产出异常返回true, 产出正常返回false
     */
    private boolean checkException(Item item, ProduceMonit monit, int limit, long nowTime)
    {
        // 超出上限则说明产出异常
        if (monit.getCount() > limit)
        {
            LOG.warn("物品产出异常！roleName = " + owner.getRoleName() + "，itemId = "
                    + item.getId() + "，limit = " + limit + "，count = " + monit.getCount());

            ProduceExceptionBean bean = new ProduceExceptionBean();
            bean.setType(2);
            bean.setTarget(monit.getId());
            bean.setCount(monit.getCount());
            bean.setStartTime(new Date(monit.getStartTime()));
            bean.setEndTime(new Date(nowTime));
            bean.setRoleId(UniqueId.toBase36(owner.getRoleId()));

            // 重置监控数据
            monit.setStartTime(nowTime);
            monit.setCount(0);

            GameDBOperator.getInstance().submitRequest(
                    new ProduceExceptionHandler(owner.getLineId(), bean));
            return true;
        }

        return false;
    }

    private void TestInitResource()
    {
        for (ResourceType type : ResourceType.values())
        {

        }
    }
}
