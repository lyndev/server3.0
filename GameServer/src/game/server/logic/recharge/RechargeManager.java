/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.core.message.SMessage;
import game.data.bean.q_rechargeBean;
import game.message.RechargeMessage;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
//import game.server.logic.guide.GuideManager;
import game.server.logic.item.bean.Item;
import game.server.logic.recharge.bean.FirstRechargeBean;
import game.server.logic.recharge.bean.ItemCode;
import game.server.logic.recharge.bean.OrderBean;
import game.server.logic.recharge.bean.RechargeBean;
import game.server.logic.recharge.bean.ResVerifyBean;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.thread.BackLogProcessor;
import game.server.util.MessageUtils;
import game.server.util.UniqueId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 充值相关, 注意: 因为涉及到钱, 所以该类的代码必须严格检查各种输入数据, 防止盗刷
 *
 * @author ChenLong
 */
public class RechargeManager implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(RechargeManager.class);
    private final transient Player owner;

    private final Map<String, RechargeBean> rechargeRecord = new HashMap<>(); // 充值记录, 用来验证充值单是否已充值过
    private int accumulativeRecharge = 0; // 累计充值(元宝)
    private final Map<Integer, FirstRechargeBean> firstRecharges = new HashMap<>(); // 首充信息

    public RechargeManager(Player owner)
    {
        this.owner = owner;
    }

    public void clientInitializeOver()
    {
        fillAllFitstRechargeBeans();
        sendResRechargeInfo(new LinkedList<>(firstRecharges.values()));
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("accuRecharge", accumulativeRecharge);
        { // rechargeRecord
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, RechargeBean> entry : rechargeRecord.entrySet())
            {
                RechargeBean bean = entry.getValue();
                jsonArray.add(bean.toJson());
            }
            jsonObj.put("rechargeRecord", jsonArray);
        }
        { // fitstRecharges
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<Integer, FirstRechargeBean> entry : firstRecharges.entrySet())
            {
                FirstRechargeBean bean = entry.getValue();
                jsonArray.add(bean.toJson());
            }
            jsonObj.put("fitstRecharges", jsonArray);
        }
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
            {
                // logger.err...
                break;
            }
            if (!(json instanceof JSONObject))
            {
                logger.error("json not instanceof JSONObject");
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            if (jsonObj.containsKey("accuRecharge"))
                accumulativeRecharge = jsonObj.getIntValue("accuRecharge");
            { // rechargeRecord
                if (jsonObj.containsKey("rechargeRecord"))
                {
                    JSONArray jsonArray = jsonObj.getJSONArray("rechargeRecord");
                    for (int i = 0; i < jsonArray.size(); ++i)
                    {
                        JSONObject beanJsonObj = jsonArray.getJSONObject(i);
                        RechargeBean bean = new RechargeBean();
                        bean.fromJson(beanJsonObj);
                        rechargeRecord.put(bean.getOrder(), bean);
                    }
                }
            }
            { // fitstRecharges
                if (jsonObj.containsKey("fitstRecharges"))
                {
                    JSONArray jsonArray = jsonObj.getJSONArray("fitstRecharges");
                    for (int i = 0; i < jsonArray.size(); ++i)
                    {
                        JSONObject beanJsonObj = jsonArray.getJSONObject(i);
                        FirstRechargeBean bean = new FirstRechargeBean();
                        bean.fromJson(beanJsonObj);
                        firstRecharges.put(bean.getId(), bean);
                    }
                }
            }
        } while (false);
    }

    public void reqGetFirstRechargeReward(RechargeMessage.ReqGetFirstRechargeReward reqMsg)
    {
        reqGetFirstRechargeReward(reqMsg.getNID());
    }

    public void reqGetFirstRechargeReward(int id)
    {
        do
        {
            FirstRechargeBean firstRechargeBean = firstRecharges.get(id);
            if (firstRechargeBean == null)
            {
                logger.error("reqGetFirstRechargeReward cannot get FirstRechargeBean, id = " + id);
                sendResGetFirstRewardResult(id, 1);
                break;
            }
            if (firstRechargeBean.getState() != FirstRechargeState.CAN_GET)
            {
                logger.error("reqGetFirstRechargeReward cannot get award");
                sendResGetFirstRewardResult(id, 2);
                break;
            }
            q_rechargeBean qBean = BeanTemplet.getRechargeBean(id);
            if (qBean == null)
            {
                logger.error("reqGetFirstRechargeReward cannot get config, id = " + id);
                sendResGetFirstRewardResult(id, 3);
                break;
            }
            firstRechargeBean.setState(FirstRechargeState.HAS_GOT);
            sendResRechargeInfo(firstRechargeBean);
            sendResGetFirstRewardResult(id, 0); // 0 成功

            // 发奖励
            { // 奖励元宝数量
            
                int q_awardgold = 0;
//                int q_awardgold = qBean.getQ_awardgold();
                if (q_awardgold > 0)
                {
                    owner.getBackpackManager().addResource(ResourceType.GOLD_BULLION, q_awardgold, true, Reasons.FIRST_RECHARGE_AWARD, new Date());
                }
            }

            { // 奖励金币数量
                int q_awardmoney = 0;
//                int q_awardmoney = qBean.getQ_awardmoney();
                if (q_awardmoney > 0)
                {
                    owner.getBackpackManager().addResource(ResourceType.GOLD, q_awardmoney, true, Reasons.FIRST_RECHARGE_AWARD, new Date());
                }
            }

            { // 奖励物品ID
                String q_items = "";
//                String q_items = qBean.getQ_items();
                if (!q_items.isEmpty())
                {
                    List<Item> items = new LinkedList<>();
                    String[] tokens1 = q_items.split(";");
                    for (String token1 : tokens1)
                    {
                        String[] tokens2 = token1.split("_");
                        if (tokens2.length > 2)
                        {
                            try
                            {
                                int itemId = Integer.parseInt(tokens2[0]);
                                int num = Integer.parseInt(tokens2[1]);
                                Item item = BeanFactory.createItem(itemId, num);
                                if (item != null)
                                {
                                    items.add(item);
                                }
                                else
                                {
                                    logger.error("reqGetFirstRechargeReward id = " + id + ", cannot create item, itemId = " + itemId + " num = " + num);
                                }
                            }
                            catch (Exception ex)
                            {
                                logger.error("Exception", ex);
                            }
                        }
                        else
                        {
                            logger.error("reqGetFirstRechargeReward item config error, id = " + id + ", token1 = " + token1);
                        }
                    }
                    if (!items.isEmpty())
                    {
                        owner.getBackpackManager().addItem(items, true, Reasons.FIRST_RECHARGE_AWARD);
                    }
                }
            }
        } while (false);
    }

    public int getAccumulativeRecharge()
    {
        return accumulativeRecharge;
    }

    /**
     * 充值触发
     *
     * @param rechargeAmount 充值元宝数
     */
    public void rechargeTrigger(int rechargeAmount)
    {
        try
        {
            setAccumulativeRecharge(accumulativeRecharge + rechargeAmount);
        }
        catch (Throwable t)
        {
            logger.error("Throwable 1", t);
        }
        try
        {
            owner.getRebateManager().rechargeTrigger(rechargeAmount, System.currentTimeMillis());
        }
        catch (Throwable t)
        {
            logger.error("Throwable 2", t);
        }
    }

    private void setAccumulativeRecharge(int accumulativeRecharge)
    {
        this.accumulativeRecharge = accumulativeRecharge;
        logger.info("累计充值为: " + this.accumulativeRecharge);
        sendResRechargeInfo(new LinkedList<FirstRechargeBean>());
        try
        {
            owner.getVipManager().rechargeTrigger(accumulativeRecharge);
        }
        catch (Exception ex)
        {
            logger.error("setAccumulativeRecharge vip rechargeTrigger exception", ex);
        }
        try
        {
            firstRechargeTrigger();
        }
        catch (Exception ex)
        {
            logger.error("setAccumulativeRecharge firstRechargeTrigger exception", ex);
        }
    }

    private void firstRechargeTrigger()
    {
        List<FirstRechargeBean> canGetBeans = new LinkedList<>();
        for (FirstRechargeBean bean : firstRecharges.values())
        {
            if (bean.getState() == FirstRechargeState.CAN_NOT_GET)
            {
                q_rechargeBean qBean = BeanTemplet.getRechargeBean(bean.getId());
                if (qBean != null)
                {
                    int q_gold = 0;
//                    int q_gold = qBean.getQ_gold(); // 需要充值到的元宝
                    if (this.accumulativeRecharge >= q_gold)
                    {
                        bean.setState(FirstRechargeState.CAN_GET);
                        canGetBeans.add(bean);
                        logger.info("达成首充: " + bean.getId());
                    }
                }
                else
                {
                    logger.error("firstRechargeTrigger cannot get config, id = " + bean.getId());
                }
            }
        }
        if (!canGetBeans.isEmpty())
        {
            sendResRechargeInfo(canGetBeans);
        }
    }

    public void reqRecharge(RechargeMessage.ReqRechargeVerify reqMsg)
    {
        RechargeService.getInstance().addVerifyOrderCommand(owner, reqMsg);
    }

    public void verifyResult(OrderBean bean)
    {
        do
        {
            String order = bean.getOrder();
            String result = bean.getResult();
            if (result == null || result.trim().isEmpty())
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", result exception");
                sendResVerifyResult(order, -1);
                break;
            }
            JSONObject jsonObj = JSON.parseObject(result);
            String errorCode = jsonObj.getString("error_code");
            if (errorCode != null) // 失败返回
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", result errorCode: [" + errorCode + "]");
                sendResVerifyResult(order, -2, errorCode);
                break;
            }
            String orderId = jsonObj.getString("orderid");
            if (orderId == null)
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", jsonObj not contain \"orderid\" key");
                sendResVerifyResult(order, -3);
                break;
            }
            // 判断返回的充值单号和发送的充值单号是否一致
            if (!orderId.equals(order))
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", orderId: [" + orderId + "], order: [" + order + "]");
                sendResVerifyResult(order, -4);
                break;
            }
            // 判断该充值单是否已经充值过
            if (rechargeRecord.containsKey(order))
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", orderId: [" + orderId + "], has recharged");
                sendResVerifyResult(order, -5);
                break;
            }
            // 艹, 平台返回的结果是不区分Android和IOS的
            // 也就是说我拿个android机用zs01帐号创建一个角色,
            // 你拿个iphone也有一个叫zs01的帐号创建个角色,
            // 我充了10块钱，你拿你的iphone会获取到我充的10块钱的单子,
            // 你拿着我的单子到GameServer，GameServer判断不是你的不给冲。。。
            // 什么jb2b设计!
            String serverId = jsonObj.getString("server_id");
            String character_id = jsonObj.getString("character_id");
            if (!serverId.equals(Integer.toString(owner.getServerId())))
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", serverId = " + serverId + ", player serverId = " + owner.getServerId());
                sendResVerifyResult(order, -11);
                break;
            }
            if (character_id == null)
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId() + ", character_id = null ");
                sendResVerifyResult(order, -12);
                break;
            }
            if (!character_id.equals(UniqueId.toBase36(owner.getRoleId())))
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId() + ", character_id = " + character_id);
                sendResVerifyResult(order, -12);
                break;
            }

            ResVerifyBean resVerifyBean = null;
            ItemCode itemCode = null;
            try
            {
                resVerifyBean = ResVerifyBean.parseFromJson(result);
                if (resVerifyBean == null || !validateResVerifyBean(order, resVerifyBean))
                {
                    logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                            + ", resVerifyBean failure");
                    sendResVerifyResult(order, -6);
                    break;
                }
                itemCode = ItemCode.parseFromString(resVerifyBean.getItem_code());
                if (itemCode == null)
                {
                    logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                            + ", itemCode failure");
                    sendResVerifyResult(order, -7);
                    break;
                }
            }
            catch (Exception ex)
            {
                logger.error("verifyResult roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId() + ", resVerifyBean failure", ex);
                sendResVerifyResult(order, -9);
                break;
            }

            // 至此判断应该是成功结果
            logger.info("verifyResult record RechargeBean, result: " + result);
            RechargeBean rechargeBean = new RechargeBean(order, System.currentTimeMillis());
            rechargeRecord.put(order, rechargeBean);

            // 从平台返回的数据全部检测完毕, 执行充值动作
            logger.info("Recharge roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId() + ", currencyAmount = " + resVerifyBean.getCurrency_amount());
            recharge(order, resVerifyBean, itemCode);
        } while (false);
    }

    /**
     * 购买月卡
     *
     * @param order
     * @param resVerifyBean
     * @param itemCode
     */
    private void rechargeMonthCard(String order, ResVerifyBean resVerifyBean, ItemCode itemCode)
    {
        if (resVerifyBean.getCurrency_amount() < resVerifyBean.getDiscount_price()) // 修改支付金额小于商品价格的处理, 直接充成元宝
        {
            rechargeGold(order, resVerifyBean, itemCode);
        }
        else
        {
            owner.buyMonthCard();
            sendResVerifyResult(order); // 充值完成
        }
    }

    /**
     * 元宝充值
     *
     * @param order
     * @param resVerifyBean
     * @param itemCode
     */
    private void rechargeGold(String order, ResVerifyBean resVerifyBean, ItemCode itemCode)
    {
        int amount = (int) resVerifyBean.getAmount();
        if (resVerifyBean.getGift() > 0)
            amount += resVerifyBean.getGift();
        owner.getBackpackManager().addResource(ResourceType.GOLD_BULLION, amount, true, Reasons.RECHARGE, new Date());
        sendResVerifyResult(order); // 充值完成
    }

    private void recharge(String order, ResVerifyBean resVerifyBean, ItemCode itemCode)
    {
        do
        {
            try
            {
                GoodsType goodsType = GoodsType.getType(itemCode.getGoodsType());
                if (goodsType == null)
                {
                    logger.error("recharge roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                            + ", goodsType == null, itemCode.getGoodsType() = " + itemCode.getGoodsType());
                    break;
                }
                switch (goodsType)
                {
                    case MonthCard:
                        rechargeMonthCard(order, resVerifyBean, itemCode);
                        break;
                    case QuarterCard:
                        logger.error("recharge unsupport QuarterCard");
                        break;
                    case YearCard:
                        logger.error("recharge unsupport YearCard");
                        break;
                    case Gold:
                        rechargeGold(order, resVerifyBean, itemCode);
                        break;
                    default:
                        logger.error("recharge unknow goodsType: " + goodsType);
                        break;
                }
            }
            catch (Throwable t) // 必须hold住所有exception, 否则下面的充值触发将无法触发
            {
                logger.error("Throwable 1", t);
            }
            try
            {
                rechargeTrigger((int) resVerifyBean.getAmount());
            }
            catch (Throwable t)
            {
                logger.error("Throwable 2", t);
            }
            try
            {
                addLogRechargeSuccess(resVerifyBean.getOrderid(), resVerifyBean.getDiscount_price(), new Date());
            }
            catch (Throwable t)
            {
                logger.error("Throwable 3", t);
            }
        } while (false);
    }

    /**
     * 验证返回成功的充值信息是否有异常
     *
     * @param order
     * @param bean
     * @return
     */
    private boolean validateResVerifyBean(String order, ResVerifyBean bean)
    {
        boolean success = false;
        do
        {
            if (bean.getAmount() <= 0.0)
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", amount <= 0.0, amount = " + bean.getAmount());
                sendResVerifyResult(order, -20);
                break;
            }
            if (bean.getCategory().trim().isEmpty())
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", category isEmpty, category: " + bean.getCategory());
                sendResVerifyResult(order, -21);
                break;
            }
            if (bean.getCurrency_amount() <= 0.0)
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", currencyAmount <= 0.0, currencyAmount = " + bean.getCurrency_amount());
                sendResVerifyResult(order, -22);
                break;
            }

            if (bean.getGift() < 0)
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", gift <= 0, gift = " + bean.getGift());
                sendResVerifyResult(order, -23);
                break;
            }
            if (bean.getItem_amount() <= 0.0)
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", item_amount <= 0, item_amount = " + bean.getItem_amount());
                sendResVerifyResult(order, -24);
                break;
            }
            if (bean.getItem_code().trim().isEmpty())
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", item_code isEmpty, item_code: " + bean.getItem_code());
                sendResVerifyResult(order, -25);
                break;
            }
            if (bean.getServer_id().trim().isEmpty())
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", server_id isEmpty, server_id: " + bean.getServer_id());
                sendResVerifyResult(order, -26);
                break;
            }
            if (bean.getCharacter_id().trim().isEmpty())
            {
                logger.error("validateResVerifyBean roleName: [" + owner.getRoleName() + "], roleId = " + owner.getRoleId()
                        + ", character_id isEmpty, character_id: " + bean.getCharacter_id());
                sendResVerifyResult(order, -27);
                break;
            }
            success = true;
        } while (false);
        return success;
    }

    /**
     * 根据配置填充所有首充类型
     */
    private void fillAllFitstRechargeBeans()
    {
        List<q_rechargeBean> list = BeanTemplet.getAllRechargeBean();
        for (q_rechargeBean bean : list)
        {
            int id = bean.getQ_id();
            if (!firstRecharges.containsKey(id))
            {
                FirstRechargeBean firstRechargeBean = new FirstRechargeBean();
                firstRechargeBean.setId(id);
                firstRecharges.put(id, firstRechargeBean);
            }
        }
    }

    private void sendResVerifyResult(String order)
    {
        sendResVerifyResult(order, 0, StringUtils.EMPTY);
    }

    private void sendResVerifyResult(String order, int error)
    {
        sendResVerifyResult(order, error, StringUtils.EMPTY);
    }

    private void sendResVerifyResult(String order, int error, String errorCode)
    {
        RechargeMessage.ResVerifyResult.Builder resMsg = RechargeMessage.ResVerifyResult.newBuilder();
        resMsg.setStrBillNO(order);
        if (error != 0)
            resMsg.setNError(error);
        if (!errorCode.trim().isEmpty())
            resMsg.setErrorCode(errorCode);
        MessageUtils.send(owner, new SMessage(RechargeMessage.ResVerifyResult.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    private void sendResRechargeInfo(FirstRechargeBean bean)
    {
        List<FirstRechargeBean> list = new LinkedList<>();
        list.add(bean);
        sendResRechargeInfo(list);
    }

    private void sendResRechargeInfo(List<FirstRechargeBean> beans)
    {
        RechargeMessage.ResRechargeInfo.Builder resMsg = RechargeMessage.ResRechargeInfo.newBuilder();
        resMsg.setNRechargeGold(accumulativeRecharge);
        if (!beans.isEmpty())
        {
            RechargeMessage.FirstRecharge.Builder firstRechargeMsg = RechargeMessage.FirstRecharge.newBuilder();
            for (FirstRechargeBean bean : beans)
            {
                firstRechargeMsg.clear();
                firstRechargeMsg.setNID(bean.getId());
                firstRechargeMsg.setNState(bean.getState().getValue());
                resMsg.addFirstRecharge(firstRechargeMsg.build());
            }
        }
        MessageUtils.send(owner, new SMessage(RechargeMessage.ResRechargeInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    private void sendResGetFirstRewardResult(int id, int error)
    {
        RechargeMessage.ResGetFirstRewardResult.Builder resMsg = RechargeMessage.ResGetFirstRewardResult.newBuilder();
        resMsg.setNID(id);
        if (error != 0)
            resMsg.setNError(error);
        MessageUtils.send(owner, new SMessage(RechargeMessage.ResGetFirstRewardResult.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 采集用户充值购买日志
     *
     * @param fgi
     * @param serverId
     * @param playerId
     * @param fedId
     * @param orderId
     * @param amount 充值RMB
     * @param time
     */
    private void addLogRechargeSuccess(String orderId, double amount, Date time)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String orderId;
            private String amount;
            private Date date;

            public ICommand set(String fgi, String serverId, String playerId, String fedId, String orderId, String amount, Date time)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = playerId;
                this.fedId = fedId;
                this.orderId = orderId;
                this.amount = amount;
                this.date = time;
                if (this.date == null)
                    this.date = new Date();
                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogRechargeSuccess(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        orderId,
                        amount,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(owner.getFgi(), Integer.toString(owner.getServerId()), UniqueId.toBase36(owner.getRoleId()), owner.getFedId(),
                orderId, Double.toString(amount), new Date()));
    }

    public void testAddAccuRecharge(int value)
    {
        this.setAccumulativeRecharge(accumulativeRecharge + value);
    }

    public void testAddLogRechargeSuccess()
    {
        this.addLogRechargeSuccess("chenlong", 188.8, new Date());
    }
}
