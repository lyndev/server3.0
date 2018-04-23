package game.server.world.room.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.message.PlayerMessage;
import game.message.RoomMessage;
import game.server.logic.struct.Player;
import game.server.logic.support.IJsonConverter;
import game.server.util.UniqueId;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Room implements IJsonConverter
{
    private final static Logger logger                    = Logger.getLogger(Room.class);
    private long m_room_id                                = 0;                          // 房间唯一id
    private int m_room_number                             = 0;                          // 放号
    private long timestamp                                = System.currentTimeMillis(); // 房间创建时间戳
    private Room.State room_state                         = Room.State.NONE;            // 战斗状态
    private long m_creator_id                             = 0;                          // 创建者id
    private final Map<Long, FightPlayerBase> fightPlayers = new HashMap<>();        
    private Room.Type roomType                            = Room.Type.NONE;             // 房间类型
    private Room.GameType gameType                        = Room.GameType.NONE;          // 游戏类型
    private int location                                  = 0;      
    protected boolean[] m_seat                            = null;                       
    private int roomReadyCountRef                         = 0;      
    private int playTimes                                 = 1;                          // 局数
    private int alreadyPlayTimes                          = 0;                          // 已经玩的局数
    private int baseScore                                 = 0;                          // 底分
    private int multiple                                  = 1;                          // 倍数
    private int m_validPlayerCount                        = 0;      // 当前加入比赛的有效玩家
        
    public enum State {
     
        NONE("无状态", 0), READY("准备", 1), FIGHT("战斗", 2), END("结束", 3);
     
        private String name ;
        private int index ;
         
        private State( String name , int index ){
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

    public enum Type {
     
        NONE("无类型", 0), CUSTOM("自建房", 1), MATCH("比赛房", 2), MATCHING("匹配", 3);
     
        private String name ;
        private int index ;
         
        private Type( String name , int index ){
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
    
    public enum PlayerType {
        NONE("无类型", 0), Self("自己", 1), Others("其他玩家", 2), All("所有人", 3);
        private String name ;
        private int index ;
         
        private PlayerType( String name , int index ){
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

    public enum GameType {
     
        NONE("无类型", 0), NN("牛牛", 1), MJ("麻将", 2), DDZ("斗地主", 3), ZJH("扎金花", 4), TANK("坦克对战", 5);
     
        private String name ;
        private int index ;
         
        private GameType( String name , int index ){
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
   
    public enum ActionType {
        NONE("无类型", 0), 
        A_READY("准备", 1), 
        A_UNREADY("取消准备", 2),
        A_ROBOT("托管", 3),
        A_UNROBOT("取消托管", 4),
        A_WAIT("等待", 5),
        A_UNWAIT("取消等待", 6),
        A_GIVE_UP("放弃", 10), 
        A_LOOK_CARD("看牌", 11), 
        A_OPEN_CARD("开牌", 12), 
        A_COMPARE_CARD("比牌", 13),
        A_ADD_SCORE("加注", 14),
        A_WAIT_COMPARE("等待比牌", 15),
        A_THINKING("思考中", 16),
        A_FOLLOW_SCORE("跟注", 17),
        A_ONLINE("上线", 100),
        A_OFFLINE("下线", 101);

        private String name ;
        private int index ;
         
        private ActionType( String name , int index ){
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
        
        public static ActionType getTypeByValue(int index)
        {
            for (ActionType t : ActionType.values())
            {
                if (t.index == index)
                    return t;
            }
            throw new IllegalStateException("未知的成就类型值: " + index);
        }
    } 
    
    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
/*        jsonObj.put("userId", UniqueId.toBase36(userId));
        jsonObj.put("timestamp", timestamp);
        jsonObj.put("type", type);*/
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        /*
        do
        {
            if (json == null)
            {
                logger.error("Friend fromJson json == null");
                break;
            }
            if (!(json instanceof JSONObject))
            {
                logger.error("Friend fromJson json not instanceof JSONObject");
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            String userIdStr = jsonObj.getString("userId");
            userId = UniqueId.toBase10(userIdStr);
            timestamp = jsonObj.getLongValue("timestamp");
            type = jsonObj.getIntValue("type");
        } while (false);
*/
    }
    
        
    public void msgFightPlayeActionHandler(RoomMessage.ReqAction.Builder msg){        
    }
    
    public void increaseReadyCountRef(){
        this.roomReadyCountRef = this.roomReadyCountRef + 1;
    }
    
    public void decreaseReadyCountRef(){
        this.roomReadyCountRef = this.roomReadyCountRef - 1;
        if(this.roomReadyCountRef < 0 ){
            this.roomReadyCountRef = 0;
        }
    }
    
    public int getReadyCount(){
        return this.roomReadyCountRef;
    }
    
    public void resetReadyCount(){
        this.roomReadyCountRef = 0;
    }
    
    
    public void setSeat(int seatIndex, boolean bSeat){
        this.m_seat[seatIndex] = bSeat;
    }
    
    public int GetEmptySeatIndex(){
        for(int i = 0; i < this.m_seat.length; i++){
            if(!this.m_seat[i]){
                return i;
            }
        }
        return -1;
    }
    
    public boolean isCanFightStart(){        
        return this.isFullAllReady() || this.isAllReady();
    }
    
    public boolean isFullAllReady(){
        if(this.roomReadyCountRef == this.m_seat.length){
            return true;
        }
        return false;
    }
    
    public boolean isAllReady(){
        if(this.roomReadyCountRef > 1 && (this.roomReadyCountRef == this.fightPlayers.size())){
            return true;
        }
        return false;
    }
            
    public void resPlayerGameFightData(){
        
    }
    
    public void resWillExcuteAction(RoomMessage.Action willAction){
        
    }
    
    public void resActionResult(RoomMessage.Action resultAction, PlayerType playerType, Long playerId ){
        
    }
    
    public void resFightResult(RoomMessage.ResFightResult fightResult){
        
    }
    
    public void resFinalResult(){
        
    }
    
    public void resOneTimesResult(){
        
    }
    
    public void onPlayerOnOffline(String playerId, boolean bOnline)
    {
        logger.error("房间子类没有实现 onPlayerOnOffline");
    }
    
    public void OnPlayerReady(String playerId, boolean bReady){
        logger.error("房间子类没有实现 OnPlayerReady");
    }
    
    public boolean onGameStart(){
       return false; 
    }
   
    //游戏消息处理  
    public boolean OnGameMessage(RoomMessage.ReqAction reqMsg)
    {
        return false;
    }
    
    public void setBaseScore(int score){
        this.baseScore = score;
    }
    public int getBaseScore(){
        return this.baseScore;
    }
    
    public void setMultiple(int multiple){
        this.multiple = multiple;
    }
    
    public int getMultiple(){
        return this.multiple;
    }
    public void setPlayTimes(int times){
        this.playTimes = times;
    }
    
    public int getPlayTimes(){
        return this.playTimes;
    }
    
    public void setAlreadyPlayTimes(int times){
        this.alreadyPlayTimes = times;
    }
    
    public int getAlreadyPlayTimes(){
        return this.alreadyPlayTimes;
    }
    
    public boolean isFinishTimes(){
        return this.getAlreadyPlayTimes() == this.getPlayTimes();
    }
    
    public long getCreatorId()
    {
        return this.m_creator_id;
    }

    public void setCreatorId(long userId)
    {
        this.m_creator_id = userId;
    }

    public long getRoomId()
    {
        return this.m_room_id;
    }

    public void setRoomId(long roomId)
    {
        this.m_room_id = roomId;
    }

    public int getRoomNumber()
    {
        return this.m_room_number;
    }

    public void setRoomNumber(int roomNum)
    {
        this.m_room_number = roomNum;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public Room.State getState()
    {
        return this.room_state;
    }

    public void setState(Room.State state)
    {
        this.room_state = state;
    }
    
    public Room.Type getRoomType()
    {
        return this.roomType;
    }

    public void setRoomType(Room.Type roomType)
    {
        this.roomType = roomType;
    }
     
    public Room.GameType getGameType()
    {
        return this.gameType;
    }

    public void setGameType(Room.GameType gameType)
    {
        this.gameType = gameType;
    }
    
    public boolean isFull(){
       for(int i = 0; i < this.m_seat.length; i++){
            if(!this.m_seat[i]){
                return false;
            }
        }
        return true;
    }
    
    public boolean isEmpty(){
        return this.fightPlayers.isEmpty();
    }
        
    public boolean enterRoom(Player player){
        if(!this.isFull()){
            boolean _isEnterSucces = false;
            switch(this.gameType){
                case NONE:
                    break;
                case DDZ:
                    break;
                case TANK:      
                case ZJH:
                    
                    int _seatIndex = this.GetEmptySeatIndex();
                    if(_seatIndex == -1){
                        return false;
                    }
                    FightPlayerZJH _fightPlayer = new FightPlayerZJH();
                    _fightPlayer.setRoleId(player.getRoleId());
                    _fightPlayer.setLocation(_seatIndex);
                     this.setSeat(_seatIndex, true);
                    _fightPlayer.setCoin(player.getGold());
                    _fightPlayer.setUserId(player.getUserId());
                    
                    PlayerMessage.PlayerInfo.Builder _playerInfo = PlayerMessage.PlayerInfo.newBuilder();
                    _playerInfo.setRoleId(UniqueId.toBase36(player.getRoleId()));
                    _playerInfo.setRoleName(player.getRoleName());
                    _playerInfo.setVipRemainDay(player.getVipManager().getVipLevel());
                    _playerInfo.setRoleLevel(player.getRoleLevel());
                    _playerInfo.setUserId(UniqueId.toBase36(player.getUserId()));
                    _playerInfo.setCoin(player.getGold());
                    _fightPlayer.setPlayerInfo(_playerInfo);
                    this.fightPlayers.put(player.getRoleId(), _fightPlayer);
                    _isEnterSucces = true;
                    break;
                case MJ:
                   break;
                default:
                    break;   
            }
           return _isEnterSucces;
        } else{
            logger.info(this.getRoomNumber() + " :房间满员了，进入失败！");
            return false;
        }
    }
    
    public boolean leaveRoom(Long roleId){
        if(this.fightPlayers.containsKey(roleId)){
            
            FightPlayerBase _player = this.getFightPlayer(roleId);
            if(_player != null){
                if(_player.isReady()){
                    // 准备计数器-1
                    this.decreaseReadyCountRef();
                }
                int _seatIndex = _player.getLocation();
                this.setSeat(_seatIndex, false);
            }
          
            this.fightPlayers.remove(roleId);
             
            if(this.isEmpty()){
                logger.info("所有玩家离开，清理房间，再次利用");
                this.ClearRoomData();
            }
        }
        return true;
    }
    
    public void ResetRoom(){
        logger.info("father class reset room data");
        this.setBaseScore(0);
        this.setState(Room.State.NONE);
        this.ResetPlayerReadyState();
        this.resetReadyCount();
    }
    
    public void ResetPlayerReadyState(){
        Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerBase _player =  entry.getValue();
            _player.setReady(false);
        }
    }
    
    public void ClearRoomData(){
        this.ResetRoom();
        this.setAlreadyPlayTimes(0);
        this.setCreatorId(0);
        this.setMultiple(0);
    }
    
    public Map<Long, FightPlayerBase> getRoomFightPlayers(){
        return this.fightPlayers;
    }
    
    public FightPlayerBase getFightPlayer(long roleId){
        return this.fightPlayers.get(roleId);
    }
    
    public FightPlayerBase getFightPlayerByIndex(int idx){
        for(Map.Entry<Long, FightPlayerBase> entry: fightPlayers.entrySet()){
            FightPlayerBase _player = entry.getValue();
            if(_player.getLocation() == idx){
                return _player;
            }
        }
        return null;
    }
}