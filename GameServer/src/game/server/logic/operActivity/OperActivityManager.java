/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.operActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.message.SMessage;
import game.core.util.ArrayUtils;
import game.data.bean.q_globalBean;
import game.data.bean.q_oper_activityBean;
import game.message.OperActivityMessage;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.item.bean.Item;
import game.server.logic.operActivity.bean.OperActivityType;
import game.server.logic.operActivity.bean.OperLoginTimesBean;
import game.server.logic.operActivity.bean.OperRechargeBean;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.util.MessageUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 运营活动
 * @author Administrator
 */
public class OperActivityManager implements IJsonConverter
{  
    private static final Logger log = Logger.getLogger(OperActivityManager.class);
    
    Player owner;
    //充值奖励
    OperRechargeBean rechargeBean;
    //登陆次数奖励
    OperLoginTimesBean loginTimesBean;

    public OperActivityManager(Player owner){
        rechargeBean = new OperRechargeBean();
        loginTimesBean = new OperLoginTimesBean();
        this.owner = owner;
    }
    
    public void clientInitializeOver(){
        sendResActivityInfo();
    }
    
    /**
     * 通过此接口发布新活动
     * @param activitys 
     */
    public void onNewActivityPublished(String activitys){
        if (activitys == null || activitys.isEmpty()){
            log.error("onNewActivityPublished 发布新活动时，参数activitys 为空");
            return;
        }
        
        try
        {
            JSONObject json = JSON.parseObject(activitys);   
            
        }
        catch (Exception e)
        {
            log.error("活动参数 activitys 转为json异常" + e);
        }
        
        
    }
    
    
    /**
     * 请求领取奖励
     * @param reqMsg
     * @return 
     */
    public void onReqGetReward(OperActivityMessage.ReqReward reqMsg){
        ErrorCode eCode = ErrorCode.OK;
        int activityId = reqMsg.getActivityId();
        if (rechargeBean.getStatus(activityId) == 1){
            giveReward(activityId);
            rechargeBean.updateRewardInfo(activityId, 2);
        }else if (loginTimesBean.getStatus(activityId) == 1){
            giveReward(activityId);
            loginTimesBean.updateRewardInfo(activityId, 2);
        }else{
            eCode = ErrorCode.OPER_CANT_REWARD;
        }

        sendResReward(eCode);
    }
    
    /**
     * 发放奖励
     * @param activityId 
     */
    private void giveReward(int activityId){
        q_oper_activityBean actiBean = BeanTemplet.getOperActivityBeans(activityId);
        if (actiBean == null){
            log.error("运营活动配置表，找不到Id:" + activityId + "的数据玩家：" + owner.getRoleName());
            return;
        }
        
        //资源奖励
        String strResReward = actiBean.getQ_res_reward();
        if (!strResReward.isEmpty()){
            String[] arrResReward = strResReward.split(";");
            for (String strIdCnt:arrResReward){
                String[] arrIdCnt =  strIdCnt.split("_");
                if (arrIdCnt.length != 2){
                    log.error("运营活动配置表，资源奖励错误，配置表Id:" + activityId);
                }else{
                    owner.getBackpackManager().addResource(Integer.valueOf(arrIdCnt[0]), Integer.valueOf(arrIdCnt[1]), true,
                        Reasons.OPER_ACTIVITY, new Date());
                }
            }
        }
        
        //物品奖励
        String strItemReward = actiBean.getQ_item_reward();
        if (!strItemReward.isEmpty()){
            List<Item> lstItems = new LinkedList<>();
            String[] arrItemReward = strItemReward.split(";");
                for (String strIdCnt:arrItemReward){
                String[] arrIdCnt =  strIdCnt.split("_");
                if (arrIdCnt.length != 2){
                    log.error("运营活动配置表，物品奖励错误，配置表Id:" + activityId);
                }else{
                    Item item = BeanFactory.createItem(Integer.valueOf(arrIdCnt[0], Integer.valueOf(arrIdCnt[1])));
                    lstItems.add(item);
                    
                }
            }
            owner.getBackpackManager().addItem(lstItems, true, Reasons.OPER_ACTIVITY);
        }
        
    }
    
    /**
     * 双倍掉落是否开放
     * @param missionId
     * @return 
     */
    public boolean isDoubleDropOpen(int missionId){
//        //是否在活动期间
//        if (!isInOpenDate(2120)){
//            return false;
//        }
//        
//        List<q_oper_activityBean> lstBean = getBeanByType(OperActivityType.DOUBLE_DROP);
//        if (lstBean.isEmpty()){
//            log.error("运营活动配置表，没有配置【掉落翻倍】的数据");
//            return false;
//        }
//        
//        //当前关卡是否开放活动
//        if (!isMissionOpenActivity(lstBean.get(0).getQ_cdt(), missionId)){
//            return false;
//        }
        return false;
    }
    
    /**
     * 额外掉落
     * @param missionId
     * @return 
     */
    public boolean  isExtraDropOpen(int missionId){
//        //是否在活动期间
//        if (!isInOpenDate(2121)){
//            return false;
//        }
//        
//        List<q_oper_activityBean> lstBean = getBeanByType(OperActivityType.EXTRA_DROP);
//        if (lstBean.isEmpty()){
//            log.error("运营活动配置表，没有配置【额外掉落】的数据");
//            return false;
//        }
//        
//        //当前关卡是否开放活动
//        if (!isMissionOpenActivity(lstBean.get(0).getQ_cdt(), missionId)){
//            return false;
//        }
        return false;
    }
    
    /**
     * 充值活动
     * @param value
     * @return 
     */
    public boolean isRechargeAccumulateOpen(int value){
        //是否在活动期间
//        if (!isInOpenDate(2122)){
//            return false;
//        }
//        
//        List<q_oper_activityBean> lstBean = getBeanByType(OperActivityType.RECHARGE_CNT);
//        if (lstBean.isEmpty()){
//            log.error("运营活动配置表，没有配置【累计充值】的数据");
//            return false;
//        }
//        
//        rechargeBean.recharge(value);
//        int curRecharge = rechargeBean.getRechargeTotal();
//        for (q_oper_activityBean actiBean:lstBean){
//            int needRecharge = Integer.valueOf(actiBean.getQ_cdt());
//            if (curRecharge >= needRecharge && !rechargeBean.isHaveRewardInfo(actiBean.getQ_id())){
//                rechargeBean.updateRewardInfo(actiBean.getQ_id(), 1);
//            }
//        }
        return false;
    }
    
    /**
     * 累积登陆
     */
    public boolean isLoginAccumulateOpen(){
        //是否在活动期间
//        if (!isInOpenDate(2123)){
//            return false;
//        }
//        
//        List<q_oper_activityBean> lstBean = getBeanByType(OperActivityType.LOGIN_CNT);
//        if (lstBean.isEmpty()){
//            log.error("运营活动配置表，没有配置【累计登录】的数据");
//            return false;
//        }
//        
//        loginTimesBean.upTimesTotal();
//        int curTotal = loginTimesBean.getTimesTotal();
//        for (q_oper_activityBean actiBean:lstBean){
//            int needTotal= Integer.valueOf(actiBean.getQ_cdt());
//            if (curTotal >= needTotal && !loginTimesBean.isHaveRewardInfo(actiBean.getQ_id())){
//                loginTimesBean.updateRewardInfo(actiBean.getQ_id(), 1);
//            }
//        }
        return false;
    }
    
    /**
     * 当前服是否开放此活动
     * @param activityId
     * @return 
     */
    private boolean isCurServerOpen(int activityId){
     
        return true;
    }
    
    
    /**
     * 当前日期是否开放此活动
     * @param strDate
     * @param globalId
     * @return 
     */
    private boolean isInOpenDate(int globalId){
        q_globalBean gBean = BeanTemplet.getGlobalBean(globalId);
        if (gBean == null){
            log.error("全局配置表，" + globalId + "不存在");
            return false;
        }
        
        String date = gBean.getQ_string_value();
        if (date.isEmpty()){
            log.error("运营活动, 日期配置错误, 全局配置表Id:" + globalId);
            return false;
        }
        
        int serverId = owner.getServerId();
        String[] arrDate = date.split(";");
        if (arrDate.length < serverId){
            log.error("运营活动, 日期配置错误,有遗漏的服务器配置, 全局配置表Id:" + globalId);
            return false;            
        }
        
        String curDate = arrDate[serverId - 1];
        String[] numDate = curDate.split(".");
        if (numDate.length != 2 || Integer.valueOf(numDate[0]) != serverId - 1){
            log.error("运营活动, 日期配置错误, 全局配置表Id:" + globalId + "服务器Id:" + serverId);
            return false;
        }
        String[] arrStarEnd = numDate[1].split("/");
        if (arrStarEnd.length != 2){
            log.error("运营活动, 日期配置错误, 全局配置表Id:" + globalId + "服务器Id:" + serverId);
        }
        
        long timeStart = parseTime(arrStarEnd[0]);
        long timeEnd = parseTime(arrStarEnd[1]);
        long curTimeMill = System.currentTimeMillis();
        if (curTimeMill >= timeStart && curTimeMill <= timeEnd){
            return true;
        }
        
        //清除活动数据
        if (curTimeMill > timeEnd){
            
        }
        
        return false;
    }
    
    /**
     * 当前关卡是否开放此活动
     * @param strMissionIds
     * @param missionId
     * @return 
     */
    private boolean isMissionOpenActivity(String strMissionIds, int missionId){
        if (strMissionIds.isEmpty()){
            return true;
        }
        
        String[] arrMissionIds = strMissionIds.split("_");
        for (String strMissionId:arrMissionIds){
            if (Integer.valueOf(strMissionId) == missionId){
                return  true;
            }
        }
        return false;
    }

    
    /**
     * 根据活动类型获取配置数据
     * @param type
     * @return 
     */
    private List<q_oper_activityBean> getBeanByType(OperActivityType type){
        List<q_oper_activityBean> beanLst = BeanTemplet.getOperActivityBeansList();
        List<q_oper_activityBean> operBeans = new LinkedList<>();
        
        for (q_oper_activityBean bean:beanLst){
            if (bean.getQ_type() == type.getCode()){
                operBeans.add(bean);
            }
        }
        return operBeans;
    }
    
    /**
     * 日期转换
     * @param config
     * @return 
     */
    private long parseTime(String config){
        if (config == null){
            log.error("运营活动，开放时间配置有误");
            return 0;
        }

        int[] time = ArrayUtils.parseInt(config.split("_"));
        if (time == null || time.length != 5){
            log.error("运营活动, 时间为空或者长度不对, 错误的配置为");
            return 0;
        }

        if (time[0] < 0
                || time[1] < 1 || time[1] > 12
                || time[2] < 1 || time[2] > 31
                || time[3] < 0 || time[3] > 23
                || time[4] < 0 || time[4] > 59)
        {
            log.error("运营活动, 时间格式不正确");
            return 0;
        }

        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, time[0]);
        date.set(Calendar.MONTH, time[1] - 1); //注意, 日历中的月份是0-11
        date.set(Calendar.DAY_OF_MONTH, time[2]);
        date.set(Calendar.HOUR_OF_DAY, time[3]);
        date.set(Calendar.MINUTE, time[4]);

        return date.getTimeInMillis();
    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("rechargeBean", rechargeBean.toJson());
        obj.put("loginTimesBean", loginTimesBean.toJson());
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null){
            return;
        }
        
        JSONObject obj = (JSONObject)json;
        if (obj.containsKey("rechargeBean")){
            rechargeBean.fromJson(obj.getJSONObject("rechargeBean"));
        }
        if (obj.containsKey("loginTimesBean")){
            loginTimesBean.fromJson(obj.getJSONObject("loginTimesBean"));
        }
    }
    
    //发送初始化消给到客户端
    private void sendResActivityInfo(){
        OperActivityMessage.ResActivityInfo.Builder msg = OperActivityMessage.ResActivityInfo.newBuilder();
        msg.setRechargeInfo(rechargeBean.getRechargeInfo());
        msg.setLoginInfo(loginTimesBean.getLoginInfo());
        
        //回复客户端
        MessageUtils.send(owner, new SMessage(OperActivityMessage.ResActivityInfo.MsgID.eMsgID_VALUE,
                msg.build().toByteArray()));
    }
    
    
    //发送领取结果给客户端
    private void sendResReward(ErrorCode code){
        OperActivityMessage.ResReward.Builder msg = OperActivityMessage.ResReward.newBuilder();
        msg.setECode(code.getCode());
        
        //回复客户端
        MessageUtils.send(owner, new SMessage(OperActivityMessage.ResReward.MsgID.eMsgID_VALUE,
                msg.build().toByteArray()));
    }
    
    //有新活动通知消息
    private void sendResNewActivity(String activitys){
        if (activitys == null && activitys.isEmpty()){
            return;
        }
        
        OperActivityMessage.ResNewActivity.Builder msg = OperActivityMessage.ResNewActivity.newBuilder();
        msg.setContextJson(activitys);

        //回复客户端
        MessageUtils.send(owner, new SMessage(OperActivityMessage.ResNewActivity.MsgID.eMsgID_VALUE,
                msg.build().toByteArray()));
    }
    
}
