/**
 * @date 2014/9/17
 * @author ChenLong
 */
package game.server.logic.vip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.message.SMessage;
import game.core.script.ScriptManager;
import game.data.bean.q_vipBean;
import game.message.VipMessage;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.DBView;
import game.server.logic.support.IJsonConverter;
import game.server.logic.util.ScriptArgs;
import game.server.util.MessageUtils;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class VipManager implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(VipManager.class);
    private final transient Player owner;
    //当前VIP等级
    private int vipLevel = 0;
    //已经领取了奖励的VIP等级
    private Set<Integer> awardVipLvs = new TreeSet<>();

    public VipManager(Player owner)
    {
        this.owner = owner;
    }

    public void clientInitializeOver()
    {
        sendLevelChangeMsg();
        sendRewardMsg();
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("vipLevel", vipLevel);
        {
            JSONArray giftsArray = new JSONArray();
            for (Integer lv : awardVipLvs)
            {
                giftsArray.add(lv);
            }
            jsonObj.put("awardLv", giftsArray);
        }
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
                break;
            if (!(json instanceof JSONObject))
            {
                logger.error("json not instanceof JSONObject");
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            if (jsonObj.containsKey("vipLevel"))
                vipLevel = jsonObj.getIntValue("vipLevel");
            if (jsonObj.containsKey("awardLv"))
            {
                JSONArray giftsArray = jsonObj.getJSONArray("awardLv");
                for (int i = 0; i < giftsArray.size(); ++i)
                {
                    awardVipLvs.add(giftsArray.getIntValue(i));
                }
            }
        } while (false);
    }

    /**
     * 充值触发接口
     *
     * @param accumulativeRecharge 累计充值额
     */
    public void rechargeTrigger(int accumulativeRecharge)
    {
        int toVipLevel = vipLevel;
        List<q_vipBean> beans = BeanTemplet.getAllVipBean();
        for (q_vipBean bean : beans)
        {
            if (accumulativeRecharge >= bean.getQ_exp())
                toVipLevel = bean.getQ_level();
            else
                break;
        }
        if (toVipLevel > vipLevel)
        {
            logger.info("原vip等级: " + vipLevel + ", 升级为: " + toVipLevel);
            setVipLevel(toVipLevel);

            // 功能开启触发
            owner.getFeatureManager().trigger(null);
        }
    }

    public void setVipLevel(int vipLevel)
    {
        int oldLevle = this.vipLevel;
        if (oldLevle >= vipLevel)
        {
            return;
        }
        this.vipLevel = vipLevel;
        sendRewardMsg();
        sendResVipLeve();
        DBView.getInstance().setVipLevel(owner.getRoleId(), vipLevel);
        callSetVipLevelScript();
//        owner.getSkywardManager().vipUp(oldLevle);
        
        //技能点上限会随vip的等级改变
        owner.getBackpackManager().flushResourceList(true);
        //日常任务VIP玩家登陆可能会触发
        owner.getDailyTaskManager().dailyLoginTrigger();
    }

    /**
     * 请求领取Vip等级奖励
     *
     * @param reqMsg
     */
    public void ReqGetVipReward(VipMessage.ReqGetRewards reqMsg)
    {
        int reqVipLv = reqMsg.getVipLevel();
        do
        {
            //请求等级>已经开通等级
            if (reqVipLv > vipLevel)
            {
                logger.error("请求领取未开通VIP等级的奖励,当前等级：" + vipLevel + "请求等级:" + reqVipLv + "玩家：" + owner.getRoleName());
                sendGetRewardMsg(ErrorCode.VIPLV_NOT_OPEN, reqVipLv);
                break;
            }

            //此等级已经领了奖励
            if (awardVipLvs.contains(reqVipLv))
            {
                logger.error("请求领取已经领取的VIP等级奖励，VIP等级：" + reqVipLv + "玩家:" + owner.getRoleName());
                sendGetRewardMsg(ErrorCode.VIP_HAVE_REWARD, reqVipLv);
                break;
            }

            //发放奖励
            giveVipReward(reqVipLv);
            awardVipLvs.add(reqVipLv);

            sendGetRewardMsg(ErrorCode.OK, reqVipLv);
        } while (false);
    }

    /**
     * 发放Vip新等级的奖励
     *
     * @param vipLv
     * @param rewards
     */
    private void giveVipReward(int vipLv)
    {
        q_vipBean vipBean = BeanTemplet.getVipBean(vipLv);
        String strRewards = vipBean.getQ_reward();
        if (strRewards.isEmpty())
        {
            return;
        }

        String[] tbRewards = strRewards.split(";");
        for (String reward : tbRewards)
        {
            String[] strIdCnt = reward.split("_");
            if (strIdCnt.length != 2)
            {
                logger.error("vip等级奖励配置错误，等级:" + vipLv + "玩家:" + owner.getRoleName());
                continue;
            }
            Item item = BeanFactory.createItem(Integer.valueOf(strIdCnt[0]), Integer.valueOf(strIdCnt[1]));
            owner.getBackpackManager().addItem(item, true, Reasons.VIP_REWARD);
        }
    }

    public int getVipLevel()
    {
        return vipLevel;
    }

    /**
     * Vip等级更新消息(set语义)
     */
    private void sendResVipLeve()
    {
        {
            VipMessage.ResVipLevel.Builder resMsg = VipMessage.ResVipLevel.newBuilder();
            resMsg.setVipLevel(vipLevel);
            MessageUtils.send(owner, new SMessage(VipMessage.ResVipLevel.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
        }
        {
            owner.SendAttributeChangeMsg(3, vipLevel);
        }
    }

    private int nextVipLevel()
    {
        return vipLevel + 1;
    }

    //VIP等级变化消息
    public void sendLevelChangeMsg()
    {
        VipMessage.ResVipLevel.Builder lvMsg = VipMessage.ResVipLevel.newBuilder();
        lvMsg.setVipLevel(vipLevel);
        MessageUtils.send(owner, new SMessage(VipMessage.ResVipLevel.MsgID.eMsgID_VALUE, lvMsg.build().toByteArray()));
    }

    //VIP奖励状态消息
    public void sendRewardMsg()
    {
        VipMessage.ResVipRewardsInfo.Builder msg = VipMessage.ResVipRewardsInfo.newBuilder();
        VipMessage.VipRewards.Builder info = VipMessage.VipRewards.newBuilder();
        for (int i = 1; i <= vipLevel; ++i)
        {
            info.clear();
            info.setVipLevel(i);
            if (awardVipLvs.contains(i))
            {
                info.setStatus(2);
            }
            else
            {
                info.setStatus(1);
            }
            msg.addAwards(info.build());
        }
        MessageUtils.send(owner, new SMessage(VipMessage.ResVipRewardsInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
    }

    //发送领取奖励结果
    public void sendGetRewardMsg(ErrorCode code, int reqLv)
    {
        VipMessage.ResGetRewards.Builder msg = VipMessage.ResGetRewards.newBuilder();
        msg.setVipLevel(reqLv);
        msg.setECode(code.getCode());
        MessageUtils.send(owner, new SMessage(VipMessage.ResGetRewards.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
    }

    /**
     * 调用角色VIP等级改变脚本
     *
     * @return
     */
    private void callSetVipLevelScript()
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.ARG1, owner);
        args.put(ScriptArgs.Key.ARG2, owner.getSession());

        ScriptManager.getInstance().call(1015, args);
    }
}
