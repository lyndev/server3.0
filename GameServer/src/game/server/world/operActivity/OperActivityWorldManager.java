/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.operActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.operActivity.bean.OperActivityType;
import game.server.world.operActivity.struct.OperActivityWorldBean;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * GM发出运营活动时，玩家可能不在线，上线后到此类中检测是否有新活动
 * @author Administrator
 */
public class OperActivityWorldManager
{
    private static final Logger log = Logger.getLogger(OperActivityWorldManager.class);
    private transient String md5sum = StringUtils.EMPTY;
    
    //活动信息
    Map<Integer, OperActivityWorldBean> activityInfo;
    
    public OperActivityWorldManager(){
        activityInfo =  new HashMap<>();
    }
    
    /**
     * 更新活动
     * @param activitys 
     */
    public void onUpdateActivity(String activitys){
        if (activitys == null || activitys.isEmpty()){
            log.error("GameWorld更新活动时，参数activitys为空");
            return;
        }
        
        //把活动存在结构中
        long curTimeMillis = System.currentTimeMillis();
        JSONObject activityJson = JSON.parseObject(activitys);
        OperActivityType[] arrNodeTypes = OperActivityType.values();
        for (OperActivityType type:arrNodeTypes){
            JSONObject jsonObj = activityJson.getJSONObject(String.valueOf(type.getCode()));
            if (jsonObj != null){
                OperActivityWorldBean worldBean = new OperActivityWorldBean();
                worldBean.setLastPublishTime(curTimeMillis);
                worldBean.setInfo(jsonObj);
                activityInfo.put(type.getCode(), worldBean);
            }
        }      
    }

    private JSON toJson(){
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (OperActivityWorldBean worldBean:activityInfo.values()){
            jsonArray.add(worldBean);
        }
        jsonObj.put("activityInfo", jsonArray);
        return jsonObj;
    }
    
    private void fromJson(){
        
    }
    
}
