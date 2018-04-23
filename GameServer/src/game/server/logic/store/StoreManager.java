package game.server.logic.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.core.message.SMessage;
import game.core.util.SimpleRandom;
import game.data.bean.q_buy_costBean;
import game.data.bean.q_itemBean;
import game.data.bean.q_storeConfigBean;
import game.data.bean.q_store_packBean;
import game.server.logic.backpack.BackpackManager;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.thread.BackLogProcessor;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * <b>商店管理类.</b>
 */
public class StoreManager implements IJsonConverter
{
    private final static Logger LOG = Logger.getLogger(StoreManager.class);

    // 商店拥有者
    private final transient Player owner;

    // 普通商店商店
    private final NormalStore normalStore;
    // 竞技场商店
    private final NormalStore arenaStore;
    // 远征商店
    private final NormalStore expeditionStore;

    private final SimpleRandom random;

    public final static String JSON_KEY_NORMAL_STORE = "normalStore";

    public final static String JSON_KEY_ARENA_STORE = "ArenaStore";

    public final static String JSON_KEY__EXPEDITION_STORE = "expeditionStore";

    public StoreManager(Player owner)
    {
        this.owner = owner;
        this.arenaStore = new NormalStore();
        this.expeditionStore = new NormalStore();
        this.normalStore = new NormalStore();
        this.random = new SimpleRandom();
    }

    @Override
    public JSON toJson()
    {
        JSONObject json = new JSONObject();
        // 序列化普通商店
        if (normalStore.isEnable())
        {
            JSONObject normalJson = new JSONObject();
            normalJson.put("isEnable", normalStore.isEnable());
            normalJson.put("flushNum", normalStore.getFlushNum());
            normalJson.put("lastFlushtime", normalStore.getLastFlushTime());
            if (normalStore.getGoodsMap() != null
                    && !normalStore.getGoodsMap().isEmpty())
            {
                JSONArray jsonArr = new JSONArray(normalStore.getGoodsMap().size());
                for (Map.Entry<Integer, Goods> entry : normalStore.getGoodsMap().entrySet())
                {
                    jsonArr.add(entry.getValue().toJson());
                }
                normalJson.put("goods", jsonArr);
            }
            json.put(JSON_KEY_NORMAL_STORE, normalJson);
        }

        // 序列化竞技场商店
        if (arenaStore.isEnable())
        {
            JSONObject arenalJson = new JSONObject();
            arenalJson.put("flushNum", arenaStore.getFlushNum());
            arenalJson.put("lastFlushtime", arenaStore.getLastFlushTime());
            if (arenaStore.getGoodsMap() != null
                    && !arenaStore.getGoodsMap().isEmpty())
            {
                JSONArray jsonArr = new JSONArray(arenaStore.getGoodsMap().size());
                for (Map.Entry<Integer, Goods> entry : arenaStore.getGoodsMap().entrySet())
                {
                    jsonArr.add(entry.getValue().toJson());
                }
                arenalJson.put("goods", jsonArr);
            }
            json.put(JSON_KEY_ARENA_STORE, arenalJson);
        }

        // 序列化远征商店
        if (expeditionStore.isEnable())
        {
            JSONObject expeditionJson = new JSONObject();
            expeditionJson.put("flushNum", expeditionStore.getFlushNum());
            expeditionJson.put("lastFlushtime", expeditionStore.getLastFlushTime());
            if (expeditionStore.getGoodsMap() != null
                    && !expeditionStore.getGoodsMap().isEmpty())
            {
                JSONArray jsonArr = new JSONArray(expeditionStore.getGoodsMap().size());
                for (Map.Entry<Integer, Goods> entry : expeditionStore.getGoodsMap().entrySet())
                {
                    jsonArr.add(entry.getValue().toJson());
                }
                expeditionJson.put("goods", jsonArr);
            }
            json.put(JSON_KEY__EXPEDITION_STORE, expeditionJson);
        }
        return json;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject jsonObj = (JSONObject) json;
        if (jsonObj != null)
        {
            // 反序列化普通商店
            JSONObject normalJson = jsonObj.getJSONObject(JSON_KEY_NORMAL_STORE);
            if (normalJson != null)
            {
                normalStore.setIsEnable(true);
                normalStore.setFlushNum(normalJson.getIntValue("flushNum"));
                normalStore.setLastFlushTime(normalJson.getLong("lastFlushtime"));
                JSONArray jsonArr = normalJson.getJSONArray("goods");
                if (jsonArr != null)
                {
                    for (Object obj : jsonArr)
                    {
                        JSONObject js = (JSONObject) obj;
                        Goods goods = new Goods();
                        goods.fromJson(js);
                        normalStore.getGoodsMap().put(goods.getPos(), goods);
                    }
                }
            }

            // 反序列化竞技场商店
            JSONObject arenaJson = jsonObj.getJSONObject(JSON_KEY_ARENA_STORE);
            if (arenaJson != null)
            {
                arenaStore.setIsEnable(true);
                arenaStore.setFlushNum(arenaJson.getIntValue("flushNum"));
                arenaStore.setLastFlushTime(arenaJson.getLong("lastFlushtime"));
                JSONArray jsonArr = arenaJson.getJSONArray("goods");
                if (jsonArr != null)
                {
                    for (Object obj : jsonArr)
                    {
                        JSONObject js = (JSONObject) obj;
                        Goods goods = new Goods();
                        goods.fromJson(js);
                        arenaStore.getGoodsMap().put(goods.getPos(), goods);
                    }
                }
            }
            // 反序列化远征商店
            JSONObject expeditionJson = jsonObj.getJSONObject(JSON_KEY__EXPEDITION_STORE);
            if (expeditionJson != null)
            {
                expeditionStore.setIsEnable(true);
                expeditionStore.setFlushNum(expeditionJson.getIntValue("flushNum"));
                expeditionStore.setLastFlushTime(expeditionJson.getLong("lastFlushtime"));
                JSONArray jsonArr = expeditionJson.getJSONArray("goods");
                if (jsonArr != null)
                {
                    for (Object obj : jsonArr)
                    {
                        JSONObject js = (JSONObject) obj;
                        Goods goods = new Goods();
                        goods.fromJson(js);
                        expeditionStore.getGoodsMap().put(goods.getPos(), goods);
                    }
                }
            }
        }
    }

    public void clientInitializeOver()
    {
        //这里需要判断并重新刷新各种商店
        long now = System.currentTimeMillis();
        if (MiscUtils.across5(arenaStore.getLastFlushTime(), now))
        {
            arenaStore.setFlushNum(0);
            flushArenaGoods(true);
        }
        if (MiscUtils.across5(normalStore.getLastFlushTime(), now))
        {
            normalStore.setFlushNum(0);
            flushNormalGoods(true);
        }
        if (MiscUtils.across5(expeditionStore.getLastFlushTime(), now))
        {
            expeditionStore.setFlushNum(0);
            flushExpeditionGoods(true);
        }

        notifyStoreInfo();
    }


    /**
     * 客户端请求购买商品
     *
     * @param request
     */
    public void onBuyGoods()
    {

    }

    /**
     * 检测是否解锁商店.
     */
    public void checkUnlockStore()
    {
        
    }

    public void unlockArenaStore()
    {

    }

    private ErrorCode buyGodds(NormalStore store, int pos, String storeName)
    {
        //从商店中获取商品
        //判断商品是否存在
        //判断商品是否已经售罄
        //根据消耗货币类别，从道具表中获取购买单价
        //判断玩家货币是否足够
        //修改商店中商品的数量为0（表示售罄）
        //扣除货币
        //添加道具到背包
        //添加日志
        Map<Integer, Goods> goodsMap = store.getGoodsMap();
        Goods goods = goodsMap.get(pos);
        if (goods == null)
        {
            LOG.error("玩家[" + owner.getRoleName() + "],在商店[" + storeName + "],购买的商品不存在。pos = " + pos);
            return ErrorCode.WRONG_CONFIG;
        }

        if (goods.getAmount() <= 0)
        {
            LOG.error("玩家[" + owner.getRoleName() + "],在商店[" + storeName + "],该商品已经卖完了，客户端端怎么不判断。pos = " + pos);
        }

        q_itemBean bean = BeanTemplet.getItemBean(goods.getItem().getId());
        if (bean == null)
        {
            LOG.error("物品表配置错误,道具不存在：itemId = " + goods.getItem().getId());
            return ErrorCode.WRONG_CONFIG;
        }
        
        //这里直接配置的是总价，所以直接获取就ok
        int cost = goods.getPrice();
        if (cost > owner.getBackpackManager().getResource(goods.getCurrency()).getAmount())
        {
            LOG.error("玩家[" + owner.getRoleName() + "]，购买道具：itemId = " + goods.getItem().getId() + ", "
                    + goods.getCurrency().getName() + "数量不够，客户端为什么不判断。");
            return ErrorCode.RESOURCE_NOT_ENOUGH;
        }
        
        //购买逻辑
        owner.getBackpackManager().subResource(goods.getCurrency(), cost, true, Reasons.BUY_GOODS, new Date());
        owner.getBackpackManager().addItem(goods.getItem(), true, Reasons.BUY_GOODS);
        addLogStoreBuy(goods.getItem().getType(), goods.getItem().getId(), goods.getItem().getNum(), goods.getCurrency().value(), cost);
        return null;
    }

    /**
     * 刷新普通商店
     *
     * @param isAutoFlush 是否是自动刷新
     * <p>
     * 竞技场刷新有两种方式：21点自动刷新，玩家手动消耗货币刷新
     */
    private void flushNormalGoods(boolean isAutoFlush)
    {
        if (normalStore != null && normalStore.isEnable())
        {
            long nowTime = System.currentTimeMillis();
            if (normalStore != null)
            {
                List<q_storeConfigBean> list = getStoreConfig(StoreType.NORMAL_STORE.value());
                if (list == null || list.size() <= 0)
                {
                    LOG.error("普通商店商店信息不存在，请检查商店配置表。");
                    return;
                }
                Map<Integer, Goods> goods = getGoodses(list, StoreType.NORMAL_STORE);
                if (goods == null || goods.size() <= 0)
                {
                    LOG.error("商店配置表中配置的普通商店的商品信息有错。");
                    return;
                }
                normalStore.getGoodsMap().clear();
                normalStore.getGoodsMap().putAll(goods);
                normalStore.setLastFlushTime(nowTime);
                if (!isAutoFlush)
                {
                    normalStore.setFlushNum(normalStore.getFlushNum() + 1);
                }
            }
        }
        else
        {
            if (!isAutoFlush)
                LOG.error("普通商店还未解锁，客户端怎么不判断。");
        }
    }

    /**
     * 刷新竞技场商店
     *
     * @param isAutoFlush 是否是自动刷新
     * <p>
     * 竞技场刷新有两种方式：21点自动刷新，玩家手动消耗货币刷新
     */
    private void flushArenaGoods(boolean isAutoFlush)
    {
        if (arenaStore != null && arenaStore.isEnable())
        {
            long nowTime = System.currentTimeMillis();
            if (arenaStore != null)
            {
                List<q_storeConfigBean> list = getStoreConfig(StoreType.ARENA_STORE.value());
                if (list == null || list.size() <= 0)
                {
                    LOG.error("竞技场商店信息不存在，请检查商店配置表。");
                    return;
                }
                Map<Integer, Goods> goods = getGoodses(list, StoreType.ARENA_STORE);
                if (goods == null || goods.size() <= 0)
                {
                    LOG.error("商店配置表中配置的竞技场的商品信息有错。");
                    return;
                }
                arenaStore.getGoodsMap().clear();
                arenaStore.getGoodsMap().putAll(goods);
                arenaStore.setLastFlushTime(nowTime);
                if (!isAutoFlush)
                {
                    arenaStore.setFlushNum(arenaStore.getFlushNum() + 1);
                }
            }
            else
            {
                if (!isAutoFlush)
                    LOG.error("竞技场商店还未解锁，客户端怎么不判断。");
                LOG.info("竞技场商店未开启。");
            }
        }
    }

    /**
     * 刷新远征商店
     *
     * @param isAutoFlush 是否是自动刷新
     * <p>
     * 竞技场刷新有两种方式：21点自动刷新，玩家手动消耗货币刷新
     */
    private void flushExpeditionGoods(boolean isAutoFlush)
    {
        if (expeditionStore != null && expeditionStore.isEnable())
        {
            long nowTime = System.currentTimeMillis();
            if (expeditionStore != null)
            {
                List<q_storeConfigBean> list = getStoreConfig(StoreType.EXPEDITION_STORE.value());
                if (list == null || list.size() <= 0)
                {
                    LOG.error("远征商店信息不存在，请检查商店配置表。");
                    return;
                }
                Map<Integer, Goods> goods = getGoodses(list, StoreType.EXPEDITION_STORE);
                if (goods == null || goods.size() <= 0)
                {
                    LOG.error("商店配置表中配置的远征商店的商品信息有错。");
                    return;
                }
                expeditionStore.getGoodsMap().clear();
                expeditionStore.getGoodsMap().putAll(goods);
                expeditionStore.setLastFlushTime(nowTime);
                if (!isAutoFlush)
                {
                    expeditionStore.setFlushNum(expeditionStore.getFlushNum() + 1);
                }
            }
            else
            {
                if (!isAutoFlush)
                    LOG.error("远征商店还未解锁，客户端怎么不判断。");
                LOG.error("远征商店的还未开启。");
            }
        }
    }

    /**
     * 获取商店的配置数据
     *
     * @param type 商店的类型：1普通商店，2竞技场商店，3远征商店
     * @return
     */
    private List<q_storeConfigBean> getStoreConfig(int type)
    {
        List<q_storeConfigBean> list = BeanTemplet.getAllStoreConfig();
        List<q_storeConfigBean> result = new ArrayList<>();
        for (q_storeConfigBean obj : list)
        {
            if (obj.getQ_type() == type)
                result.add(obj);
        }
        return result;
    }

    /**
     * 获取商品列表
     *
     * @param beanList
     * @return
     */
    private Map<Integer, Goods> getGoodses(List<q_storeConfigBean> beanList, StoreType storeType)
    {
        Map<Integer, Goods> goodsMap = new HashMap<>();
        List<q_storeConfigBean> flagList = new ArrayList<>(); // 不是固定位置的商品数据
        for (q_storeConfigBean obj : beanList)
        {
            if (obj.getQ_pos() > 0)
            {
                String[] strItem = obj.getQ_item_id().split("_");
                int itemId = Integer.valueOf(strItem[0]);
                int amount = Integer.valueOf(strItem[1]);
                int price = Integer.valueOf(strItem[2]);
                Goods goods = new Goods(obj.getQ_pos(), amount, price, itemId, ResourceType.getType(storeType.getCurrencyType()));
                goodsMap.put(obj.getQ_pos(), goods);
            }
            else
            {
                flagList.add(obj);
            }
        }
        if (flagList.isEmpty() || flagList.size() <= 0)
        {
            return goodsMap;
        }
        //
        Collections.sort(flagList, new Comparator<q_storeConfigBean>()
        {
            // 将奖励按名次从小到大排列
            @Override
            public int compare(q_storeConfigBean o1, q_storeConfigBean o2)
            {
                if (o1.getQ_level() < o2.getQ_level())
                    return -1;
                else if (o1.getQ_level() > o2.getQ_level())
                    return 1;
                else
                    return 0;
            }
        });

        q_storeConfigBean storeBean = null;
        for (q_storeConfigBean bean : flagList)
        {
            if (owner.getRoleLevel() <= bean.getQ_level())
            {
                storeBean = bean;
                break;
            }
        }

        if (storeBean == null)
        {
            //做个保底
            storeBean = flagList.get(flagList.size() - 1);
        }
        
        String strPack = storeBean.getQ_pack();
        if(strPack.equals("")){
            LOG.error("商店配置表物品掉落包有错，。Id = "+storeBean.getQ_id());
            return goodsMap;
        }
        String[] arrPack = strPack.split(";");
        for (String packData : arrPack)
        {
            String[] id_num = packData.split("_");
            createGoodsWithPack(goodsMap, Integer.valueOf(id_num[0]), Integer.valueOf(id_num[1]), storeType);
        }
        return goodsMap;
    }

    private void createGoodsWithPack( Map<Integer, Goods> goodsMap,int packId, int num, StoreType storeType )
    {
        
        q_store_packBean packBean = BeanTemplet.getStorePack(packId);
        if (packBean == null)
        {
            LOG.error("商品包配置表数据错误：id = " + packId);
            return;
        }
        if (null == packBean.getQ_items() || packBean.getQ_items().isEmpty())
        {
            LOG.error("商品包配置表数据错误：id = " + packId);
            return;
        }
        String[] strItem = packBean.getQ_items().split(";");
        int[] index = random.getArrays(0, strItem.length - 1, num);
        for (int i = 0; i < index.length; i++)
        {
            String id_num_price_curr = strItem[index[i]];
            String[] itemData = id_num_price_curr.split("_");
            int itemId = Integer.valueOf(itemData[0]);
            int itemNum = Integer.valueOf(itemData[1]);
            int itemPrice = Integer.valueOf(itemData[2]);
            int itemCurr = Integer.valueOf(itemData[3]);
            int pos = goodsMap.size() + 1;
            //做个检测
            q_itemBean itemBean = BeanTemplet.getItemBean(itemId);
            if (itemBean == null)
            {
                LOG.error("配置有问题，物品表中找不到数据。itemId = " + itemId);
                break;
            }
            if(storeType.value() != StoreType.NORMAL_STORE.value()){
                itemCurr = storeType.getCurrencyType();
            }
            Goods goods = new Goods(pos, itemNum, itemPrice, itemId, ResourceType.getType(itemCurr));
            goodsMap.put(goods.getPos(), goods);
        }
    }


    /**
     * 返回客户端商店系统消息
     */
    private void notifyStoreInfo()
    {

    }

    /**
     * 返回客户端购买商品结果消息
     *
     * @param error
     * @param source
     * @param pos
     */
    private void notifyBuyGoods(ErrorCode error, int source, int pos)
    {
       
    }

    private void notifyFlushNormalStore(ErrorCode error)
    {

    }

    private void notifyFlushArenaStore(ErrorCode error)
    {

    }

    private void notifyFlushExpeditionStore(ErrorCode error)
    {

    }

    /**
     * 添加购买日志.
     *
     * @param itemType 道具类型
     * @param itemId 道具id
     * @param itemCount 道具数量
     * @param coinType 购买道具使用的货币类型
     * @param coinNum 购买道具消耗的货币数量
     */
    private void addLogStoreBuy(int itemType, int itemId, int itemCount, int coinType, int coinNum)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;

            private String itemType;
            private String itemId;
            private String itemCnt;
            private String coinType;
            private String coinNum;

            private Date date;

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String itemType, String itemId, String itemCnt,
                    String coinType, String coinNum,
                    Date date)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.itemType = itemType;
                this.itemId = itemId;
                this.itemCnt = itemCnt;
                this.coinType = coinType;
                this.coinNum = coinNum;
                this.date = date;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogStoreBuy(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        itemType,
                        itemId,
                        itemCnt,
                        coinType,
                        coinNum,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(owner.getFgi(), Integer.toString(owner.getServerId()), UniqueId.toBase36(owner.getRoleId()), owner.getFedId(),
                Integer.toString(itemType), Integer.toString(itemId), Integer.toString(itemCount),
                Integer.toString(coinType), Integer.toString(coinNum),
                new Date()));
    }

    /**
     * 商品.
     */
    public class Goods implements IJsonConverter
    {
        private int pos; // 商品ID

        private int amount; // 商品数量

        private int price;

        private ResourceType currency; // 商品购买币种，购买时才设置，以保证获取最新币种

        private Item item; // 商品包装的物品对象，购买时才设置，以保证获取物品最新数据

        public Goods()
        {
        }

        public Goods(int pos, int amount, int price, int itemId, ResourceType currency)
        {
            this.pos = pos;
            this.amount = amount;
            this.price = price;
            this.item = BeanFactory.createItem(itemId, amount);
            this.currency = currency;
        }


        /**
         * 是否售罄
         *
         * @return
         */
        public boolean isEmpty()
        {
            return amount <= 0;
        }

        public int getPos()
        {
            return pos;
        }

        public void setPos(int pos)
        {
            this.pos = pos;
        }

        public int getAmount()
        {
            return amount;
        }

        public void setAmount(int amount)
        {
            this.amount = amount;
        }

        public int getPrice()
        {
            return price;
        }

        public void setPrice(int price)
        {
            this.price = price;
        }

        public ResourceType getCurrency()
        {
            return currency;
        }

        public void setCurrency(ResourceType currency)
        {
            this.currency = currency;
        }

        public Item getItem()
        {
            return item;
        }

        public void setItem(Item item)
        {
            this.item = item;
        }

        @Override
        public JSON toJson()
        {
            JSONObject js = new JSONObject();
            js.put("pos", pos);
            js.put("amount", amount);
            js.put("price", price);
            js.put("currency", currency.value());
            js.put("item", item.toJson());
            return js;
        }

        @Override
        public void fromJson(JSON json)
        {
            JSONObject jsonObj = (JSONObject) json;
            pos = jsonObj.getIntValue("pos");
            amount = jsonObj.getIntValue("amount");
            price = jsonObj.getIntValue("price");
            currency = ResourceType.getType(jsonObj.getIntValue("currency"));
            item = BeanFactory.createItem(jsonObj.getJSONObject("item"));
        }
    }

    class NormalStore
    {

        private boolean isEnable;

        private int flushNum;  //竞技场刷新次数

        private long lastFlushTime; // 最后一次刷新重置的时间

        Map<Integer, Goods> goodsMap;  //商品列表

        NormalStore()
        {
            this.goodsMap = new HashMap<>();
        }


        public boolean isEnable()
        {
            return isEnable;
        }

        public void setIsEnable(boolean isEnable)
        {
            this.isEnable = isEnable;
        }

        public int getFlushNum()
        {
            return flushNum;
        }

        public void setFlushNum(int flushNum)
        {
            this.flushNum = flushNum;
        }

        public long getLastFlushTime()
        {
            return lastFlushTime;
        }

        public void setLastFlushTime(long lastFlushTime)
        {
            this.lastFlushTime = lastFlushTime;
        }

        public Map<Integer, Goods> getGoodsMap()
        {
            return goodsMap;
        }

        public void setGoodsMap(Map<Integer, Goods> goodsMap)
        {
            this.goodsMap = goodsMap;
        }
    }

    public enum StoreType
    {

        NORMAL_STORE(1, 1, "普通商店"), //普通商店先默认设置货币类型为金币，具体货币再刷新的时候重新设置
        ARENA_STORE(2, 6, "竞技场商店"),
        EXPEDITION_STORE(3, 7, "远征商店");

        private final int value;
        private final int currencyType;
        private final String name;

        StoreType(int value, int currencyType, String name)
        {
            this.value = value;
            this.name = name;
            this.currencyType = currencyType;
        }

        public int value()
        {
            return value;
        }

        public int getCurrencyType()
        {
            return currencyType;
        }

        public String getName()
        {
            return name;
        }

        public static StoreType getType(int value)
        {
            for (StoreType type : StoreType.values())
            {
                if (type.value() == value)
                    return type;
            }
            return null;
        }
    }

}
