package game.server.logic.item.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.script.ScriptManager;
import game.core.util.ArrayUtils;
import game.data.bean.q_globalBean;
import game.data.bean.q_languageBean;
import game.message.BackpackMessage;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.logic.util.ScriptArgs;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * <b>物品基类.</b>
 * <p>
 * 所有物品类都继承至该类，并由该类派生出去.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class Item implements Cloneable, IJsonConverter
{

    private int id; // 物品ID

    private int num; // 物品数量

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("itemId", getId());
        jsonObj.put("num", getNum());

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        throw new UnsupportedOperationException(
                "Please use game.server.logic.support.BeanFactory.createItem(JSONObject json)");
    }

    /**
     * 获取对应的BackpackMessage.Item消息的Builder
     *
     * @return
     */
    public BackpackMessage.Item.Builder getMessageBuilder()
    {
        BackpackMessage.Item.Builder builder = BackpackMessage.Item.newBuilder();
        builder.setId(id);
        builder.setNum(num);
        builder.setType(getType());
//        if (this instanceof Equipment)
//        {
//            Equipment eq = (Equipment) this;
//            builder.setQualityExp(eq.getQualityExp());
//            builder.setEquipLevel(eq.getEquipLevel());
//        }
        return builder;
    }

    /**
     * 使用物品并计算使用效果.
     *
     * @param target 使用目标：玩家或卡牌
     * @return 使用成功返回true，否则返回false
     */
    public boolean doUse(Object target)
    {
        // 如果物品设置了脚本，则优先调用脚本
        int scriptId = getScriptId();
        if (scriptId != 0)
        {
            if (target instanceof Player)
            {
                ScriptArgs args = new ScriptArgs();
                args.put(ScriptArgs.Key.PLAYER, target);
                ScriptManager.getInstance().call(scriptId, args);
                return true;
            }
        }

        boolean result = false;
        // 如果物品对玩家使用，则执行对应效果
        if (target instanceof Player)
        {
            Player player = (Player) target;
            //经验卡
            if (id == 10014)
            {
                q_globalBean vipExpBean = BeanTemplet.getGlobalBean(1007);
                int vipExp = 100;
                if(vipExpBean != null){
                    vipExp = vipExpBean.getQ_int_value();
                }
                player.getRechargeManager().rechargeTrigger(vipExp);
                q_globalBean vipGemBean = BeanTemplet.getGlobalBean(1008);
                int vipGem = 100;
                if(vipGemBean != null){
                    vipGem = vipGemBean.getQ_int_value();
                }
                player.getBackpackManager().addResource(ResourceType.GOLD_BULLION, vipGem, true, Reasons.ITEM_USE, Calendar.getInstance().getTime());
                return true;
            }

            // 添加资源
            Resource[] resources = getAddResources();
            if (resources != null && resources.length > 0)
            {
                Date nowTime = Calendar.getInstance().getTime();
                List<Resource> changeList = new ArrayList<>(resources.length);

                for (Resource res : resources)
                {
                    //徐能强：这里不需要通知玩家资源发生变化，在下面统一通知
                    Resource r = player.getBackpackManager().addResource(
                            res.getType(), res.getAmount(), false, Reasons.ITEM_USE, nowTime);
                    if (r != null)
                    {
                        changeList.add(r);
                    }
                    if (ResourceType.GOLD_BULLION.compare(res.getType())
                            && (id == 10006 || id == 10007))
                    {//这两个礼包获得的元宝当充值处理
                        player.getRechargeManager().rechargeTrigger(res.getAmount());
                    }
                }

                // 通知玩家变更的资源列表
                player.getBackpackManager().notifyResourceUpdate(changeList);

                result = true;
            }

            // 添加物品
            List<Item> items = getAddItems();
            if (items != null && items.size() > 0)
            {
                ErrorCode error = ((Player) target).getBackpackManager().addItem(
                        items, true, Reasons.ITEM_USE);
                if (error != null)
                {
                    System.err.println("物品(" + getId()
                            + ")使用时增加物品失败(q_add_items)! errorCode = " + error.getCode());
                }

                result = true;
            }
        }

        return result;
    }

    /**
     * set 物品ID
     *
     * @param id 物品ID
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * get 物品ID
     *
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * 设置物品的叠加数量
     *
     * @param num 物品的叠加数量
     */
    public void setNum(int num)
    {
        this.num = num;
    }

    /**
     * get 物品的叠加数量
     *
     * @return
     */
    public int getNum()
    {
        return num;
    }

    /**
     * 添加物品的叠加数量
     *
     * @param num 要添加的数量
     * @return 添加后的物品数量
     */
    public int addNum(int num)
    {
        this.num += num;
        if (this.num > getNumMax())
            this.num = getNumMax();

        return this.num;
    }

    /**
     * get 物品名称
     *
     * @return
     */
    public String getName()
    {
        int name = BeanTemplet.getItemBean(id).getQ_name();

        q_languageBean bean = BeanTemplet.getLanguage(name);
        if (bean != null)
            return bean.getQ_lgtext();
        return null;
    }

    /**
     * get 物品類型
     *
     * @return
     */
    public int getType()
    {
        return BeanTemplet.getItemBean(id).getQ_type();
    }

    /**
     * get 物品品质：1=白色；2=绿色；3=蓝色；4=紫色；5=橙色
     *
     * @return
     */
    public int getDefault()
    {
        return BeanTemplet.getItemBean(id).getQ_default();
    }

    /**
     * 判断物品是否可以使用
     *
     * @return ture表示可以使用；false表示不能使用
     */
    public boolean isCanUse()
    {
        return BeanTemplet.getItemBean(id).getQ_can_use() == 1;
    }

    /**
     * 物品产出或者操作后是否要记录日志
     *
     * @return true表示需要记录；false表示不用记录
     */
    public boolean isLog()
    {
        return BeanTemplet.getItemBean(id).getQ_log() == 1;
    }

    /**
     * get 物品使用对象：0=无对象；1=玩家；2=英雄
     *
     * @return
     */
    public int getUseTarget()
    {
        return BeanTemplet.getItemBean(id).getQ_role_object();
    }

    /**
     * get 使用等级需求
     *
     * @return
     */
    public int getLevel()
    {
        return BeanTemplet.getItemBean(id).getQ_level();
    }

    //徐能强注释：配置表中已无该字段
//    /**
//     * get 使用基地等级需求
//     *
//     * @return
//     */
//    public int getBaseLevel()
//    {
//        return BeanTemplet.getItemBean(id).getQ_baselevel();
//    }
    /**
     * get 使用卡牌等级需求
     *
     * @return
     */
    public int getCardLevel()
    {
        return BeanTemplet.getItemBean(id).getQ_cardlevel();
    }

    /**
     * get 物品使用的必需物品（物品ID和数量）
     *
     * @return 如果没有必需物品，则返回null
     */
    public Item[] getRequiredItems()
    {
        // 解析物品使用的需求物品（物品ID和数量）
        String items = BeanTemplet.getItemBean(id).getQ_goods();
        if (null == items || items.isEmpty())
        {
            return null;
        }
        else
        {
            String[] arr = items.split(";");
            Item[] result = new Item[arr.length];
            for (byte i = 0; i < arr.length; i++)
            {
                String[] param = arr[i].split("_");
                result[i] = BeanFactory.createItem(
                        Integer.parseInt(param[0]), Integer.parseInt(param[1]));
            }

            return result;
        }
    }

    //徐能强注释：配置表中无该字段
//    /**
//     * get 物品使用的职业要求：1=战士；2=法师；3=坦克；4=辅助；5=射手；6=刺客
//     *
//     * @return 如果没有职业限制，则返回null
//     */
//    public int[] getProfessions()
//    {
//        // 解析物品使用的职业要求（职业编号）
//        String professions = BeanTemplet.getItemBean(id).getQ_occupation();
//        if (null == professions || professions.isEmpty())
//        {
//            return null;
//        }
//        else
//        {
//            return ArrayUtils.parseInt(professions.split(","));
//        }
//    }
    /**
     * get 调用的脚本ID
     *
     * @return
     */
    public int getScriptId()
    {
        return BeanTemplet.getItemBean(getId()).getQ_script();
    }

    /**
     * get 使用后增加的资源
     *
     * @return
     */
    public Resource[] getAddResources()
    {
        // 解析物品使用后增加的资源
        String resources = BeanTemplet.getItemBean(id).getQ_add_resources();
        if (null == resources || resources.isEmpty())
        {
            return null;
        }
        else
        {
            String[] arr = resources.split(";");
            Resource[] result = new Resource[arr.length];
            for (byte i = 0; i < arr.length; i++)
            {
                String[] param = arr[i].split("_");
                result[i] = new Resource(Integer.parseInt(param[0]), Integer.parseInt(param[1]));
            }

            return result;
        }
    }

    /**
     * get 使用后增加的物品
     *
     * @return
     */
    public List<Item> getAddItems()
    {
        // 解析物品使用的需求物品（物品ID和数量）
        String items = BeanTemplet.getItemBean(id).getQ_add_items();
        if (null == items || items.isEmpty())
        {
            return null;
        }
        else
        {
            String[] arr = items.split(";");
            List<Item> result = new ArrayList<>(arr.length);
            for (String str : arr)
            {
                String[] param = str.split("_");
                result.add(BeanFactory.createItem(
                        Integer.parseInt(param[0]), Integer.parseInt(param[1])));
            }

            return result;
        }
    }

    /**
     * get 物品叠加数量上限
     *
     * @return
     */
    public int getNumMax()
    {
        return BeanTemplet.getItemBean(id).getQ_max();
    }

    /**
     * get 物品购买价格
     *
     * @return 多种资源的购买价格，如果没有设置则返回null
     */
    public Resource[] getBuyPrices()
    {
//        // 解析物品购买价格
//        String prices = BeanTemplet.getItemBean(id).getQ_buy_price();
//        if (null == prices || prices.isEmpty())
//        {
//            return null;
//        }
//        else
//        {
//            String[] arr = prices.split(";");
//            Resource[] result = new Resource[arr.length];
//            for (byte i = 0; i < arr.length; i++)
//            {
//                String[] param = arr[i].split(",");
//                result[i] = new Resource(Integer.parseInt(param[0]), Integer.parseInt(param[1]));
//            }
//
//            return result;
//        }
        return null;
    }

    /**
     * get 物品出售价格
     *
     * @return 金币
     */
    public int getSellPrice()
    {
        return BeanTemplet.getItemBean(id).getQ_sell_price();
    }

    /**
     * get 物品获取途径信息（关卡ID）
     *
     * @return 如果物品目前不能通过关卡获得，则返回null
     */
    public int[] getPaths()
    {
        // 解析物品获取途径的关卡ID
        String paths = BeanTemplet.getItemBean(id).getQ_get_path();
        if (null == paths || paths.isEmpty())
        {
            return null;
        }
        else
        {
            return ArrayUtils.parseInt(paths.split(","));
        }
    }

    /**
     * get 每日使用次数
     *
     * @return
     */
    public int getDailyNum()
    {
        return BeanTemplet.getItemBean(id).getQ_daily_num();
    }

    /**
     * get 使用期限（开始和结束时间）
     *
     * @return 第1位表示开始时间，第2位表示结束时间，没有使用期限则返回null
     */
    public Date[] getUseTimeLimit()
    {
        Date[] result = null;
        String timeStr = BeanTemplet.getItemBean(id).getQ_usetime_limit();
        // sample: 2014,5,15,0,0;2014,5,15,23,59

        if (timeStr != null && !timeStr.isEmpty())
        {
            String[] arr = timeStr.split(";");
            if (arr != null && arr.length == 2)
            {
                result = new Date[2];
                for (byte i = 0; i < arr.length; i++)
                {
                    System.out.println(arr[i]);
                    int[] param = ArrayUtils.parseInt(arr[i].split(","));
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, param[0]);
                    cal.set(Calendar.MONTH, param[1] - 1);
                    cal.set(Calendar.DAY_OF_MONTH, param[2]);
                    cal.set(Calendar.HOUR_OF_DAY, param[3]);
                    cal.set(Calendar.MINUTE, param[4]);
                    cal.set(Calendar.SECOND, 0);
                    result[i] = cal.getTime();
                }
            }
        }

        return result;
    }

    /**
     * 如果物品可以在仙府掠夺战中掉落，返回可能在哪些星级的仙府掠夺战中被敌人掠夺.
     *
     * @return 仙府星级
     */
    public int[] getXFStarsOfPlunderDrop()
    {
        //徐能强注释：配置表中无该字段
        String xfStars = null; //BeanTemplet.getItemBean(id).getQ_plunder_drop();
        if (null == xfStars || xfStars.isEmpty())
        {
            return null;
        }
        else
        {
            return ArrayUtils.parseInt(xfStars.split(","));
        }
    }

}
