/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.room.bean;

import game.message.PlayerMessage;
import game.server.util.UniqueId;

/**
 *
 * @author Administrator
 */
public abstract  class FightPlayerBase
{
    private long roleId;
    private String strRoleId;
    private int location;
    private boolean isReady;
    private int coin = 0;
    private long userId;
    private PlayerMessage.PlayerInfo.Builder playerInfo;
    private int oneTimesCoin = 0;
    private StatusType m_status = StatusType.NONE;

    public enum StatusType {
        NONE("无状态", 0),
        PLAYING("对战中", 1), 
        OVER("结束", 2);
        
        private String name;
        private int index ;
         
        private StatusType( String name , int index ){
            this.name = name ;
            this.index = index ;
        }
         
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
    
    public long getRoleId(){
        return this.roleId;
    }
    
    public void setRoleId(long roleId){
        strRoleId = UniqueId.toBase36(roleId);
        this.roleId = roleId;
    }
        
    public String getStrRoleId(){
       return strRoleId;
    }
    
    public long getUserId(){
        return this.userId;
    }
    
    public void setUserId(long roleId){
        this.userId = roleId;
    }
    
    public int getLocation(){
        return this.location;
    }
    
    public void setLocation(int location){
        this.location = location;
    }
    
    public void setCoin(int coin){
        this.coin = coin;
    }
    
    public int getCoin(){
        return this.coin;
    }
    
    public void setOneTimesCoin(int coin){
        this.oneTimesCoin = coin;
        int totalCoin = this.getCoin() + coin;
        this.setCoin(totalCoin);
    }
    
    public int getOneTimesCoin(){
        return this.oneTimesCoin;
    }
    
    public PlayerMessage.PlayerInfo.Builder getPlayerInfo(){
        return this.playerInfo;
    }
    
    public void setPlayerInfo(PlayerMessage.PlayerInfo.Builder playerInfo){
        this.playerInfo = playerInfo;
    }
    
    public void setReady(boolean bReady){
        this.isReady = bReady;
    }
    
    public boolean isReady(){
        return this.isReady;
    }   
    
        
    // 设置玩家状态
    public void setPlayerStatus(StatusType statusType){
        this.m_status = statusType;
    }

    // 玩家状态
    public StatusType getPlayerStatus(){
        return this.m_status;
    }
}
