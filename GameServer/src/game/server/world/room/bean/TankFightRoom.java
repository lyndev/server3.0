/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.room.bean;

import game.core.message.SMessage;
import game.message.RoomMessage;
import game.server.logic.player.PlayerManager;
import game.server.util.MessageUtils;
import game.server.world.room.handler.ReqUDPEnterRoomHandler;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Administrator
 */
public class TankFightRoom extends Room
{
    private static final Logger log = Logger.getLogger(ReqUDPEnterRoomHandler.class);
    private Map<String, IoSession> playerUdpSessionMap;
    
                // 逻辑帧            // 角色id     // 帧数据
    private Map<Integer, Map<String, Object>> keyFrameDataMap;
    public final int GAME_PLAYER = 6;
    private final int frameCount = 1;
    public TankFightRoom(){
        playerUdpSessionMap = new HashMap<>();
        this.m_seat = new boolean[GAME_PLAYER];
        for(int i = 0; i<GAME_PLAYER; i++ ){
            this.m_seat[i] = false;
        }
    }
        
    // 游戏开始
    @Override
    public boolean onGameStart()
    {
        Room.State _roomState = this.getState();
        if (_roomState != Room.State.FIGHT)
        {
            return false;
        }
        
         
                        
        return false;
    }

    
    public boolean OnFrameMoveMessage(RoomMessage.ReqMoveKeyData moveMsg, IoSession udpSession)
    {
        //Room.State _roomState = this.getState();
        //if (_roomState != Room.State.FIGHT)
        //{
            //return false;
        //}
        log.info("同步战斗房间中的玩家位移。");
        for (Map.Entry<String, IoSession> entry : playerUdpSessionMap.entrySet())
        {
            // 同步位移
            RoomMessage.ResMoveKeyDatas.Builder _syncMoveMsg = RoomMessage.ResMoveKeyDatas.newBuilder();
            RoomMessage.KeyMoveData.Builder _moveData = RoomMessage.KeyMoveData.newBuilder();
            _moveData.setAngle(moveMsg.getMoveData().getAngle());
            _moveData.setPosX(moveMsg.getMoveData().getPosX());
            _moveData.setPosY(moveMsg.getMoveData().getPosY());
            _moveData.setRoleId(moveMsg.getMoveData().getRoleId());
            _syncMoveMsg.setMoveData(_moveData);
            if(entry.getValue() == udpSession){
                log.info("udp 的session 和上一次回话相同");
            } else{
                log.info("udp 的session 和上一次回话不相同");
            }
            
           MessageUtils.send(entry.getValue(), new SMessage(RoomMessage.ResMoveKeyDatas.MsgID.eMsgID_VALUE, _syncMoveMsg.build().toByteArray())); 
        }
                  
        return true;
    }
    
    //游戏消息处理
    @Override
    public boolean OnGameMessage(RoomMessage.ReqAction reqMsg)
    {
        return true;
    }
    
    @Override
    public void ResetRoom(){
    }
    
    @Override
    public void OnPlayerReady(String roleId, boolean bReady){
        
    }
    
    @Override 
    public void onPlayerOnOffline(String roleId, boolean bOnline){
    
    }
    
    public void onPlayerUDPReady(String roleId, IoSession session){

        this.playerUdpSessionMap.put(roleId, session);
        log.info("一个玩家准备战斗，udp消息");
    }

}
