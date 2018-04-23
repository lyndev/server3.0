/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.operActivity.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.message.OperActivityMessage;
import game.server.logic.support.IJsonConverter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class OperRechargeBean  implements IJsonConverter
{
    //活动期间充值额度
    int rechargeTotal;
    //充值活动数据<K:活动Id, V:领奖状态(1.可以领取, 2.已领取)>
    Map<Integer, Integer> rewardInfo;
    
    
    public OperRechargeBean(){
        rewardInfo = new HashMap<>();
    }
    
    /**
     * 充值金额
     * @param value 
     */
    public void recharge(int value){
        rechargeTotal += value;
    }
    
    /**
     * 是否存在奖励信息
     * @param activityId
     * @return 
     */
    public boolean isHaveRewardInfo(int activityId){
        if (rewardInfo.containsKey(activityId)){
            return true;
        }
        return false;
    }
    
    /**
     * 更新奖励信息
     * @param activityId
     * @param status 
     */
    public void updateRewardInfo(int activityId, int status){
        rewardInfo.put(activityId, status);
    }

    public int getRechargeTotal()
    {
        return rechargeTotal;
    }
    
    /**
     * 获取领奖状态
     * @param activityId
     * @return 
     */
    public int getStatus(int activityId){
        if (rewardInfo.containsKey(activityId)){
            return rewardInfo.get(activityId);
        }
        return 0;
    }
    

    @Override
    public JSON toJson()
    {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        for (Map.Entry<Integer, Integer> iter:rewardInfo.entrySet()){
            obj.put(String.valueOf(iter.getKey()), iter.getValue());
            array.add(iter.getKey());
        }
        if (!array.isEmpty()){
            obj.put("rewardInfo", array);
        }
        obj.put("rechargeTotal", rechargeTotal);
        
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null){
            return;
        }
        
        JSONObject obj = (JSONObject)json;
        if (obj.containsKey("rewardInfo")){
            JSONArray arry = obj.getJSONArray("rewardInfo");
            for (Object id:arry){
                int status = obj.getIntValue(String.valueOf(id));
                rewardInfo.put((Integer)id, status);
            }
        }
        rechargeTotal = obj.getIntValue("rechargeTotal");
    }
    
    public OperActivityMessage.RechargeInfo.Builder getRechargeInfo(){
        OperActivityMessage.RechargeInfo.Builder msg = OperActivityMessage.RechargeInfo.newBuilder();
        OperActivityMessage.RewardInfo.Builder infos = OperActivityMessage.RewardInfo.newBuilder();
        for (Map.Entry<Integer, Integer> iter:rewardInfo.entrySet()){
            infos.clear();
            infos.setActivityId(iter.getKey());
            infos.setStatus(iter.getValue());
            msg.addInfo(infos.build());
        }
        msg.setRechargeTotal(rechargeTotal);
        return msg;
    }
}
