/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.operActivity.handler;

import com.alibaba.fastjson.JSONObject;
import game.core.command.Handler;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 有新活动
 * @author Administrator
 */
public class WLPublishedNewActivitysHandler extends Handler
{
    private final static Logger log = Logger.getLogger(WLPublishedNewActivitysHandler.class);
    private final long userId;
    private final JSONObject jsonObj;
    
    public WLPublishedNewActivitysHandler(long userId, JSONObject jsonObj){
        this.userId = userId;
        this.jsonObj = jsonObj;
    }
    
    @Override
    public void action(){
        Player player = PlayerManager.getPlayerByUserId(userId);
        if (player == null){
            log.error("PlayerManager 中没有此玩家数据, userId:" + userId);
            return;
        }
        
//        player.getOperActivityManager().onNewActivityPublished(jsonObj);
    }
}
