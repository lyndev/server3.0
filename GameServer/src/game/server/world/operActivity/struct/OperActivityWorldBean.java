/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.operActivity.struct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;

/**
 * 活动信息
 * @author Administrator
 */
public class OperActivityWorldBean implements IJsonConverter
{
    //活动类型
    int type;
    //活动发布时间
    long lastPublishTime;
    //活动内容
    JSONObject info;

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", type);
        jsonObj.put("lastPublishTime", lastPublishTime);
        jsonObj.put("info", info);
        
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null){
            return;
        }
        
        JSONObject obj = (JSONObject)json;
        if (obj.containsKey("type")){
            type = obj.getIntValue("type");
        }
        if (obj.containsKey("lastPublishTime")){
            lastPublishTime = obj.getIntValue("lastPublishTime");
        }
        if (obj.containsKey("info")){
            info = obj.getJSONObject("info");
        }
    }   

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
    
    public long getLastPublishTime()
    {
        return lastPublishTime;
    }

    public void setLastPublishTime(long lastPublishTime)
    {
        this.lastPublishTime = lastPublishTime;
    }

    public JSONObject getInfo()
    {
        return info;
    }

    public void setInfo(JSONObject info)
    {
        this.info = info;
    }
}
