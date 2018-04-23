package game.server.logic.rebate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.message.SMessage;
//import game.data.bean.q_rebateBean;
import game.message.RebateMessage;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.rebate.bean.RebateBean;
import game.server.logic.rebate.bean.RebateRewardBean;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.util.MessageUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * 回馈活动管理器
 *
 * @author ZouZhaopeng
 */
public class RebateManager implements IJsonConverter
{
    private static final Logger log = Logger.getLogger(RebateManager.class);

    private final Player owner;

    private final Map<Integer, RebateBean> map;

    public RebateManager(Player owner)
    {
        this.owner = owner;
        map = new HashMap<>();
    }

    public void putRebate(RebateBean rebate)
    {
        map.put(rebate.getId(), rebate);
    }

    public RebateBean getRebate(int id)
    {
        return map.get(id);
    }

    public void createInitialize()
    {
//        List<q_rebateBean> list = BeanTemplet.getRebateBeanList();
//        if (list == null)
//        {
//            log.error("[" + owner.getRoleName() + "], 返利活动列表为空");
//            return;
//        }
//
//        for (q_rebateBean bean : list)
//        {
//            RebateBean rebate = new RebateBean();
//            rebate.init(bean);
//            putRebate(rebate);
//        }

    }

    public void clientInitializeOver()
    {
        reload();
    }

    public void reload()
    {
//        List<q_rebateBean> list = BeanTemplet.getRebateBeanList();
//        if (list == null)
//        {
//            log.error("[" + owner.getRoleName() + "], 返利活动列表为空");
//            return;
//        }
//
//        for (q_rebateBean bean : list)
//        {
//            if (getRebate(bean.getQ_ID()) == null)
//            {//如果不存在, 则添加, 不影响已有的活动
//                RebateBean rebate = new RebateBean();
//                rebate.init(bean);
//                putRebate(rebate);
//            }
//        }
    }

    /**
     * 领取奖励
     *
     * @param rebateId
     * @param rebateRewardId
     */
    public void getReward(int rebateId, int rebateRewardId)
    {
        //返利活动
        RebateBean rebate = getRebate(rebateId);
        if (rebate == null)
        {
            log.error("[" + owner.getRoleName() + "], 没有找到对应的返利活动配置, 活动id = " + rebateId);
            sendErrorMessage(ErrorCode.REBATE_NOT_FOUND);
            return;
        }

        if (!rebate.isOpening(System.currentTimeMillis()))
        {
            log.error("[" + owner.getRoleName() + "], 返利活动没有开启或已经结束, 活动id = " + rebateId);
            sendErrorMessage(ErrorCode.REBATE_NOT_OPEN);
            return;
        }

        //活动奖励
        RebateRewardBean rebateReward = rebate.getRebateReward(rebateRewardId);
        if (rebateReward == null)
        {
            log.error("[" + owner.getRoleName() + "], 没有找到对应的返利活动奖励配置, 活动id = " + rebateRewardId);
            sendErrorMessage(ErrorCode.REBATE_REWARD_NOT_FOUND);
            return;
        }

        //未达到奖励需要的额度, 还不能领取
//        if (rebate.getAmount() < rebateReward.getAmount())
//        {
//            log.error("[" + owner.getRoleName() + "], 未达到奖励需要的额度, 还不能领取: " + rebate.getAmount() + " / " + rebateReward.getAmount());
//            sendErrorMessage(ErrorCode.AMOUNT_NOT_ENOUGH);
//            return;
//        }
//
//        if (owner.getRoleLevel() < rebate.getLevel())
//        {
//            log.error("[" + owner.getRoleName() + "], 未达到奖励需要的导演等级: " + rebate.getLevel() + " / " + owner.getRoleLevel());
//            sendErrorMessage(ErrorCode.LEVEL_NOT_ENOUGH);
//            return;
//        }
//
//        if (owner.getVipManager().getVipLevel() < rebate.getVip())
//        {
//            log.error("[" + owner.getRoleName() + "], 未达到奖励需要的VIP等级: " + rebate.getVip() + " / " + owner.getVipManager().getVipLevel());
//            sendErrorMessage(ErrorCode.VIP_NOT_ENOUGH);
//            return;
//        }

        if (rebateReward.isAlreadyGet())
        {
            log.error("[" + owner.getRoleName() + "], 奖励已领取, 重复领取");
            sendErrorMessage(ErrorCode.REWARD_ALREADY_GET);
            return;
        }

        //领取逻辑
        rebateReward.setAlreadyGet(true);
        sendGetRewardMessage(rebateId, rebateRewardId);
//        owner.getBackpackManager().addResource(ResourceType.GOLD, rebateReward.getMoney(), true, Reasons.REBATE, new Date());
//        owner.getBackpackManager().addResource(ResourceType.GOLD_BULLION, rebateReward.getBullion(), true, Reasons.REBATE, new Date());
//        owner.getBackpackManager().addItem(rebateReward.getItems(), true, Reasons.REBATE);
    }

    public void sendErrorMessage(ErrorCode err)
    {
        RebateMessage.ResError.Builder builder = RebateMessage.ResError.newBuilder();
        builder.setCode(err.getCode());
        MessageUtils.send(owner, new SMessage(RebateMessage.ResError.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    public void sendGetRewardMessage(int rebateId, int rewardId)
    {
        RebateMessage.ResGetReward.Builder builder = RebateMessage.ResGetReward.newBuilder();
        builder.setRebateId(rebateId);
        builder.setRebateRewardId(rewardId);
        MessageUtils.send(owner, new SMessage(RebateMessage.ResGetReward.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    public void sendRebateInfo()
    {
        RebateMessage.ResRebateInfo.Builder builder = RebateMessage.ResRebateInfo.newBuilder();
        if (map != null)
        {
            for (RebateBean rebate : map.values())
            {
                if (!rebate.isOverdue())
                {
                    builder.addRebates(rebate.getMessageBuilder());
                }
            }
        }
        MessageUtils.send(owner, new SMessage(RebateMessage.ResRebateInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    public void onGetReward(RebateMessage.ReqGetReward msg)
    {
        Validate.notNull(msg);

        getReward(msg.getRebateId(), msg.getRebateRewardId());
    }

    public void onRebateInfo(RebateMessage.ReqRebateInfo msg)
    {
        Validate.notNull(msg);

        sendRebateInfo();
    }

    public void rechargeTrigger(int amount, long date)
    {
        trigger(amount, date, 1); //充值返利
    }

    public void consumeTrigger(int amount, long date)
    {
        trigger(amount, date, 2); //消费返利
    }

    private void trigger(int amount, long date, int type)
    {
        if (map == null)
        {
            return;
        }

        for (RebateBean rebate : map.values())
        {
//            if (rebate.isOpening(date) && rebate.getType() == type)
//            {
//                rebate.setAmount(rebate.getAmount() + amount);
//            }
        }

    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        if (map != null)
        {
            JSONArray arr = new JSONArray();
            for (RebateBean rebate : map.values())
            {
                arr.add(rebate.toJson());
            }
            obj.put("map", arr);
        }
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
            {
                log.error("[" + owner.getRoleName() + "], RebateManager.fromJson, json == null");
                break;
            }

            if (!(json instanceof JSONObject))
            {
                log.error("[" + owner.getRoleName() + "], RebateManager.fromJson, json is not a JSONObject");
                break;
            }

            JSONObject obj = (JSONObject) json;
            JSONArray arr = obj.getJSONArray("map");
            if (arr != null)
            {
                for (Object each : arr)
                {
                    RebateBean rebate = new RebateBean();
                    rebate.fromJson((JSON) each);
                    putRebate(rebate);
                }
            }

        } while (false);

    }
}
