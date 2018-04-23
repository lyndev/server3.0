/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.operActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.logic.constant.GlobalTableKey;
import game.server.logic.operActivity.bean.OperActivityType;
import game.server.logic.operActivity.handler.WLPublishedNewActivitysHandler;
import game.server.util.MessageUtils;
import game.server.world.GameWorld;
import game.server.world.operActivity.struct.OperActivityWorldBean;
import game.server.world.wplayer.WPlayer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * GM发出运营活动时，玩家可能不在线，上线后到此类中检测是否有新活动
 * @author Administrator
 */
public class OperActivityWorldService
{
    private static final Logger log = Logger.getLogger(OperActivityWorldService.class);
    private transient String lastMd5 = StringUtils.EMPTY;
    //最近一次保存时间
    private long lastSaveTime;
    //保存间隔(秒)
    private final int saveInterval = 60;
    
    //活动信息
    Map<Integer, OperActivityWorldBean> activityInfo;
    
    public OperActivityWorldService(){
        activityInfo =  new HashMap<>();
    }
     
    public void tick(long curTimeMillis){
        
        if (curTimeMillis - lastSaveTime >= saveInterval * 1000){
            save();
            lastSaveTime = curTimeMillis;
        }
    }
    
    public static OperActivityWorldService getInstance(){
        return Singleton.INSTANCE.getService();
    }
    
    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;

        OperActivityWorldService service;

        Singleton(){
            this.service = new OperActivityWorldService();
        }

        OperActivityWorldService getService(){
            return service;
        }
    }
    
    /**
     * 更新活动
     * @param activitysJson 
     */
    public void updateActivity(JSONObject activitysJson){
        if (activitysJson == null){
            log.error("GameWorld更新活动时，参数activityJson为空");
            return;
        }
        
        //把活动存在结构中
        long curTimeMillis = System.currentTimeMillis();
        OperActivityType[] arrNodeTypes = OperActivityType.values();
        for (OperActivityType type:arrNodeTypes){
            JSONObject jsonObj = activitysJson.getJSONObject(String.valueOf(type.getCode()));
            if (jsonObj != null){
                OperActivityWorldBean worldBean = new OperActivityWorldBean();
                worldBean.setType(type.getCode());
                worldBean.setLastPublishTime(curTimeMillis);
                worldBean.setInfo(jsonObj);
                activityInfo.put(type.getCode(), worldBean);
            }
        }      
    }
    
    /**
     * 检查是否有新活动
     * @param userId 
     */
    public void checkHaveNewActivity(long userId){
        WPlayer player = GameWorld.getInstance().getPlayer(userId);
        if (player == null){
            log.error("检测新活动时，获取WPlayer不存在，userId:" + userId);
            return;
        }
        
        JSONObject jsonObj = new JSONObject();
        for (OperActivityWorldBean worldBean:activityInfo.values()){
            if (worldBean.getLastPublishTime() > player.getLastConnectTime()){
                jsonObj.put(String.valueOf(worldBean.getType()), worldBean.getInfo());
            }
        }
        
        if (jsonObj.isEmpty()){
            return;
        }
        
        // 向GameLine回复处理结果
        MessageUtils.sendToGameLine(player.getLineId(),
                new WLPublishedNewActivitysHandler(userId, jsonObj));
    }

    //保存到数据库
    public void save(){
        //比较MD5码
        JSONArray jsonArray = new JSONArray();
        for (OperActivityWorldBean worldBean:activityInfo.values()){
            jsonArray.add(worldBean);
        }
        
        String curMd5 = DigestUtils.md5Hex(jsonArray.toJSONString());
        if (curMd5.equals(lastMd5)){
            return;
        }
        lastMd5 = curMd5;
        
        //存入数据库
        GlobalBean bean = new GlobalBean();
        bean.setId(GlobalTableKey.OPER_ACTIVITY.getKey());
        bean.setValue(jsonArray.toJSONString());
        if (GlobalDao.update(bean) <= 0)
            GlobalDao.insert(bean);
    }
    
    //从数据库读
    public void load(){
        GlobalBean bean = GlobalDao.select(GlobalTableKey.OPER_ACTIVITY.getKey());
        if (bean != null){
            JSONArray jsonArray = JSON.parseArray(bean.getValue());
            for (int i = 0; i < jsonArray.size(); ++i){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                OperActivityWorldBean worldBean = new OperActivityWorldBean();
                worldBean.fromJson(jsonObj);
                activityInfo.put(worldBean.getType(), worldBean);
            }
        }
    }
    
}
