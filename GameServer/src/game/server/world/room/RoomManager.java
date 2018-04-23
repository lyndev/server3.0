package game.server.world.room;

import game.core.message.SMessage;
import game.core.timer.TimerEvent;
import game.message.RoomMessage;
import game.server.logic.backpack.BackpackManager;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.util.MessageUtils;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.room.bean.FightPlayerBase;
import game.server.world.room.bean.Room;
import game.server.world.room.bean.TankFightRoom;
import game.server.world.room.bean.ZJHRoom;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import game.server.world.wplayer.WPlayer;
import java.util.EnumMap;

public class RoomManager
{
     public final static int ROOM_FUNCTION = 151;
    private final static Logger logger = Logger.getLogger(RoomManager.class);
    private final Map<Room.GameType, Map<Integer, Room>> customRooms;
    private final Map<Room.GameType, Map<Integer, Room>> matchingRooms;
    private int roomNumber = 1000;
    private final int ROOM_CREATE_ITEM_ID = 1000; //TODO:读表
    private final int ROOM_CRAATE_ITEM_NUM = 0; //TODO:读表
    private final int ROOM_REMOVE_SECONDS = 20 * 60; // 房间移除的一个定时器时间（秒）
    private final int ROOM_POOL_COUNT = 200; // 房间池默认初始化200个

    public RoomManager(){
        this.matchingRooms = new EnumMap<>(Room.GameType.class);
        this.customRooms = new EnumMap<>(Room.GameType.class);
        for(Room.GameType gameType : Room.GameType.values()){
            this.matchingRooms.put(gameType, new HashMap<Integer, Room>());
            this.customRooms.put(gameType, new HashMap<Integer, Room>());
        }
        
        this.CreateZJHRoomPool();
        this.CreateTankRoomPool();
    }
            
    // 房间一次性计时器
    private class RemoveRoomTimer extends TimerEvent
    {
        private final int roomNumber;
        private final Room.GameType gameType;
        private final Room.Type  roomType;

        public RemoveRoomTimer(Room.GameType gameType,  Room.Type  roomType, int roomNumber, int interval)
        {
            super(interval * 1000);
            this.roomNumber = roomNumber;
            this.gameType = gameType;
            this.roomType = roomType;
        }

        @Override
        public void run()
        {
            removeOneRoomByNumber(this.gameType,  this.roomType, this.roomNumber);
        }
    }

    public void load()
    {

    }

    public void save()
    {

    }
    
    private void CreateZJHRoomPool(){
        for(int i = 0; i < ROOM_POOL_COUNT; i++){
            Room _customRoom  = this.createOneRoom(Room.GameType.ZJH, Room.Type.CUSTOM);
            Room _matchingRoom = this.createOneRoom(Room.GameType.ZJH, Room.Type.MATCHING);
            if(_customRoom!=null){
                this.customRooms.get(Room.GameType.ZJH).put(_customRoom.getRoomNumber(), _customRoom);
            }
            if(_matchingRoom!=null){
                this.matchingRooms.get(Room.GameType.ZJH).put(_matchingRoom.getRoomNumber(), _matchingRoom);
            }
        }
        logger.info("创建扎金花-200个【自建房间】和200个【匹配房间】");
    }
    
    private void CreateTankRoomPool(){
        for(int i = 0; i < ROOM_POOL_COUNT; i++){
            Room _customRoom  = this.createOneRoom(Room.GameType.TANK, Room.Type.CUSTOM);
            Room _matchingRoom = this.createOneRoom(Room.GameType.TANK, Room.Type.MATCHING);
            if(_customRoom!=null){
                this.customRooms.get(Room.GameType.TANK).put(_customRoom.getRoomNumber(), _customRoom);
            }
            if(_matchingRoom!=null){
                this.matchingRooms.get(Room.GameType.TANK).put(_matchingRoom.getRoomNumber(), _matchingRoom);
            }
        }
        logger.info("创建坦克战斗房间-200个【自建房间】和200个【匹配房间】");
    }
        
    private void AddRoomInMap(Room.GameType gameType,  Room.Type  roomType, Room room)
    {
        if(room == null){
            return;
        }
        
        Map<Room.GameType, Map<Integer, Room>> _roomMap = this.getRoomMap(roomType);
        if(_roomMap != null){
            Map<Integer, Room> _rooms;
            _rooms = _roomMap.get(gameType);
            if(_rooms == null){
                _roomMap.put(gameType, new HashMap<Integer, Room>());
            }
            _rooms = _roomMap.get(gameType);
            _rooms.put(room.getRoomNumber(), room);
        } else {
            logger.info("添加房间失败.");
        }
    }
    
    public Map<Room.GameType, Map<Integer, Room>> getRoomMap(Room.Type roomType){
        if(roomType == Room.Type.CUSTOM){
            return this.customRooms;
        } else if (roomType == Room.Type.MATCHING){
            return this.matchingRooms;
        }
        return null;
    }
    
    public Map<Integer, Room> getRooms(Room.GameType gameType,  Room.Type roomType){
        Map<Room.GameType, Map<Integer, Room>> _roomMap = this.getRoomMap(roomType);
        if(_roomMap == null){
            logger.error("获取房间类型对应的房间列表失败：" + roomType.getName());
            return null;
        }
        
        Map<Integer, Room> _rooms = _roomMap.get(gameType);
        if(_rooms == null){
            logger.error("获取游戏房间列表失败：" + gameType.getName());
            return null;
        }
        return _rooms;
    }
    
    /**
     * 客户端初始化完成
     *
     * @param player
     */
    public void clientInitializeOver(Player player)
    {
        //TODO:推送部分房间列表
    }

    public void removeOneRoomByNumber(Room.GameType gameType,  Room.Type roomType, int roomNumber)
    {
        Map<Integer, Room> _rooms = this.getRooms(gameType, roomType);
        Room _removeRoom = _rooms.get(roomNumber);
        
        // 通知所有人离开房间
        Map<Long, FightPlayerBase> fightPlayers = _removeRoom.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            this.resPlayerLeaveRoom(entry.getValue().getUserId(), entry.getValue().getRoleId());

        }
        _rooms.remove(roomNumber);
        logger.info(roomNumber + "房间已将移除！房间时间到期。");
    }

    public void onPlayerCreateRoom(Player player, RoomMessage.ReqCreateRoom reqMsg)
    {
        if (player != null)
        {
            boolean _isCanCreate = this.isCanCreateRoom(player);
            if (_isCanCreate)
            {
                /*
                // 扣住房卡 道具
                List<Item> removeList = new ArrayList<>();
                Item createItem = BeanFactory.createItem(ROOM_CREATE_ITEM_ID, ROOM_CRAATE_ITEM_NUM);
                removeList.add(createItem);
                ErrorCode removeError = player.getBackpackManager().removeItem(removeList, true, Reasons.CRREATE_ROOM);
                if (removeError != null)
                {
                    logger.error("创建房间道具扣除失败！");
                    return;
                }
                */
                Room.GameType _gameType = Room.GameType.values()[reqMsg.getGameType()];
                Room.Type roomType =  Room.Type.values()[reqMsg.getRoomType()];
                Room _room;
                _room = this.getEmptyRoom(_gameType, roomType);
                if(_room == null){
                    _room = this.createOneRoom(_gameType, roomType);
                    if(_room != null){
                        this.AddRoomInMap(_gameType, roomType, _room);
                    }
                }
                
                if(_room == null){
                    logger.error("创建房间失败，原因未知！");
                    return;
                }
                
                logger.info(player.getUserId() + "玩家创建房间：" + _room.getRoomNumber());  
                boolean _enterSuccess = _room.enterRoom(player);
                if (_enterSuccess)
                {
                    this.sendPlayersEnterRoom(_room, player);
                }
                else
                {
                    logger.error("onPlayerCreateRoom 进入房间失败");
                }
            }
            else
            {
                //TODO:告诉玩家创建失败:原因道具不足
            }
        }
        else
        {
            logger.error(" 玩家不存在，创建房间失败");
        }
    }

    public Room createOneRoom( Room.GameType gameType, Room.Type roomType){
            // 根据游戏类型创建对应的房间
            Room _room;
            int _roomNumber = 0;
            switch(gameType){
                case ZJH:
                    _room = new ZJHRoom();
                    break;
                case TANK:
                    _room = new TankFightRoom();
                    break;
                default:
                    logger.error("创建房间失败, 客户端传递的游戏类型错误！");
                    return null;
            }
            
            if(roomType == Room.Type.CUSTOM){
                _roomNumber = Integer.valueOf(Integer.toString(gameType.getIndex()) + Integer.toString(this.getUniquedRoomNumber()));
            } else {
               _roomNumber = this.getUniquedRoomNumber();    
            }
            
            if(_roomNumber == 0){
                return null;
            }
                        
            long _roomId = UniqueId.getUniqueId(_roomNumber, 1, UniqueId.FuncType.MAIL);
            _room.setRoomId(_roomId);
            _room.setRoomNumber(_roomNumber);
            _room.setCreatorId(0);
            _room.setRoomType(roomType);
            _room.setGameType(gameType);
            _room.setTimestamp(System.currentTimeMillis());
            return _room;
    }
    

    public Room getEmptyRoom(Room.GameType gameType, Room.Type roomType){
        Map<Integer, Room> _rooms = this.getRooms(gameType, roomType);
        for(Room _room : _rooms.values()){
            if(_room.isEmpty())
            {
                return _room;
            }
        }
        return null;
    }
    
    public boolean isCanCreateRoom(Player player)
    {
        BackpackManager _bpm = player.getBackpackManager();
        int _haveNum = _bpm.getAmount(ROOM_CREATE_ITEM_ID);
        return _haveNum >= ROOM_CRAATE_ITEM_NUM;
    }

    public void onPlayerEnterRoom(Player player, RoomMessage.ReqEnterRoom reqMsg)
    {
        logger.info(player.getUserId() + "玩家进入");
        int _roomNumber = reqMsg.getRoomNumber();
        int _gameTypeIndex =  (int)( _roomNumber / 10000);
        Room.GameType _gameType = Room.GameType.values()[_gameTypeIndex];
        Room.Type _roomType =  Room.Type.values()[reqMsg.getRoomType()];
        this.playerEnterRoom(player, _gameType, _roomType, _roomNumber);
    }
    
    public void playerReenterRoom(Player player,int gameTypeIndex, int roomTypeIndex, int roomNumber){
        Room.GameType _gameType = Room.GameType.values()[gameTypeIndex];
        Room.Type _roomType =  Room.Type.values()[roomTypeIndex];
        this.playerEnterRoom(player, _gameType, _roomType, roomNumber);
    }
    
    public void playerEnterRoom(Player player, Room.GameType gameType, Room.Type roomType, int roomNumber)
    {
        Map<Integer, Room> _rooms = this.getRooms(gameType, roomType);      
        
        boolean _isExistRoom = _rooms.containsKey(roomNumber);
        if (_isExistRoom)
        {
            // 老玩家重新进入房间          
            Room _findRoom = _rooms.get(roomNumber);
            FightPlayerBase _existPlayer =_findRoom.getFightPlayer(player.getRoleId());
            if(_existPlayer != null){
                this.sendPlayersEnterRoom(_findRoom, player);
                return;
            }
            
            boolean _isFull = _findRoom.isFull();
            if (_isFull)
            {
                //TODO:进入房间失败，房间已将满员
                logger.info("room full ,enter fail");
            }
            else
            {
                // 新玩家进入房间
                boolean _enterResult = _findRoom.enterRoom(player);
                if (_enterResult)
                {
                    this.sendPlayersEnterRoom(_findRoom, player);
                }
                else
                {
                    logger.error("onPlayerEnterRoom 进入房间失败");
                }
            }
        }
        else
        {
            //TODO:进入房间失败，房间不存在
            logger.error("onPlayerEnterRoom 进入房间失败，房间耗不存在:%d"+ roomNumber);
        }
    }
    
    public void onPlayerLeaveRoom(Player player, RoomMessage.ReqLeaveRoom reqMsg){
        logger.info(player.getUserId() + "玩家离开房间");
        int _roomNum = player.getCurRoomNum();
        int _roomTypeIndex = player.getCurRoomType();
        int _gameTypeIndex = player.getCurGameType();
        Room.GameType _gameType = Room.GameType.values()[_gameTypeIndex];
        Room.Type _roomType =  Room.Type.values()[_roomTypeIndex];
        Map<Integer, Room> _rooms = this.getRooms(_gameType, _roomType);  
        Room _room = _rooms.get(_roomNum);
        if(_room != null){
            this.sendPlayersLeaveRoom(_room, player);
        }
    }

    public void sendPlayersEnterRoom(Room room, Player player)
    {
        // 房间信息存储
        player.setCurRoomNum(room.getRoomNumber());
        player.setCurRoomType(room.getRoomType().getIndex());
        player.setCurGameType(room.getGameType().getIndex());
                    
        // 构建进入的房间数据
        RoomMessage.RoomInfo.Builder _roomInfo = RoomMessage.RoomInfo.newBuilder();
        _roomInfo.setRoomId(Long.toString(room.getRoomId()));
        _roomInfo.setRoomNum(room.getRoomNumber());
        _roomInfo.setRoomType(room.getRoomType().getIndex());
        _roomInfo.setGameType(room.getGameType().getIndex());

        // 通知自己进入房间
        long _curEnterRoleId = player.getRoleId();
        FightPlayerBase _fightPlayer = room.getFightPlayer(_curEnterRoleId);
        this.resPlayerEnterRoom(_fightPlayer.getUserId(), _fightPlayer, _roomInfo);

        // 通知房间其他人员进入房间
        Map<Long, FightPlayerBase> fightPlayers = room.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            if (entry.getKey() != _curEnterRoleId)
            {
                // 把其他已经在房间的人员通知给我
                this.resPlayerEnterRoom(_fightPlayer.getUserId(), entry.getValue(), _roomInfo);
                // 通知其他玩家自己进入房间
                this.resPlayerEnterRoom(entry.getValue().getUserId(), _fightPlayer, _roomInfo);
            }
        }
    }

    public void sendPlayersLeaveRoom(Room room, Player leavePlayer)
    {
        long _curLeaveRoleId = leavePlayer.getRoleId();
        FightPlayerBase _fightPlayer = room.getFightPlayer(_curLeaveRoleId);
        
        //  通知房间里面的所有玩家xx离开
        Map<Long, FightPlayerBase> fightPlayers = room.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            this.resPlayerLeaveRoom(entry.getValue().getUserId(), _curLeaveRoleId);
        }
        
         //  修改离开玩家 的数据
        room.leaveRoom(_curLeaveRoleId);
        leavePlayer.setCurRoomNum(0);
        leavePlayer.setCurRoomType(0);
        leavePlayer.setCurGameType(0);
    }

    public void resPlayerEnterRoom(long notifyUserId, FightPlayerBase _fightPlayer, RoomMessage.RoomInfo.Builder roomInfo)
    {
        logger.info("发送进入房间通知，发送给：" + notifyUserId + " " +"进房者：" +  _fightPlayer.getUserId() + "游戏类型：" + roomInfo.getGameType());
        WPlayer _wplayer = GameWorld.getInstance().getPlayer(notifyUserId);
        if (_wplayer != null)
        {
            IoSession session = _wplayer.getSession();
            RoomMessage.ResEnterRoom.Builder resMsg = RoomMessage.ResEnterRoom.newBuilder();
            resMsg.setRoomInfo(roomInfo);
            resMsg.setPlayerBaseInfo(_fightPlayer.getPlayerInfo());
            resMsg.setLocationIndex(_fightPlayer.getLocation());
            resMsg.setBReady(_fightPlayer.isReady());
            resMsg.setGameType(roomInfo.getGameType());
            MessageUtils.send(session, new SMessage(RoomMessage.ResEnterRoom.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
        }
        else
        {
            logger.info("获取世界玩家失败");
        }
    }

    public void resPlayerLeaveRoom(long notifyUserId, long leavePlayerRoleId)
    {
        logger.info("发送离开房间通知，发送给：" + notifyUserId);
        WPlayer _wplayer = GameWorld.getInstance().getPlayer(notifyUserId);
        if (_wplayer != null)
        {
            IoSession session = _wplayer.getSession();
            RoomMessage.ResLeaveRoom.Builder resMsg = RoomMessage.ResLeaveRoom.newBuilder();
            resMsg.setPlayerId(UniqueId.toBase36(leavePlayerRoleId));
            MessageUtils.send(session, new SMessage(RoomMessage.ResLeaveRoom.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
        }
        else
        {
            logger.info("获取世界玩家失败");
        }
    }
    
    public Room getPlayerPlaceRoom(long userId)
    {
        Player _player = PlayerManager.getPlayerByUserId(userId);
        if(_player != null){
            int _roomNum = _player.getCurRoomNum();
            int _roomTypeIndex = _player.getCurRoomType();
            int _gameTypeIndex = _player.getCurGameType();

            Room.GameType _gameType = Room.GameType.values()[_gameTypeIndex];
            Room.Type _roomType =  Room.Type.values()[_roomTypeIndex];
            Map<Integer, Room> _rooms = this.getRooms(_gameType, _roomType);   

            Room _room = _rooms.get(_roomNum); 
            return _room;
        } else {
            logger.info("没有找到该玩家，获取他所在房间失败");
            return null;
        }
    }
    
    /***
     *  房间里面的玩家上下线通知
     * @param userId
     * @param bOnline 
     */
    public void resPlayerOnOffLine(long userId, boolean bOnline)
    {
        Room _room = this.getPlayerPlaceRoom(userId);
        if(_room != null){
            Player _player = PlayerManager.getPlayerByUserId(userId);
            String _playerID = UniqueId.toBase36(_player.getRoleId());
            _room.onPlayerOnOffline(_playerID, bOnline);
        }
    }
    
    public void reqPlayerAction(Player player, RoomMessage.ReqAction reqMsg){
        Room _room = this.getPlayerPlaceRoom(player.getUserId());
        if(_room != null){
            _room.OnGameMessage(reqMsg);
        }
    }

    public int getUniquedRoomNumber()
    {
        return roomNumber++;
    }
    
    /**
      * 玩家离线处理
      * @param userId
      * 
      */
    public void offlineProcess(long userId)
    {
        this.resPlayerOnOffLine(userId, false);
    }
    
    /***
     *  玩家上线处理
     * @param userId 
     */
    public void onlineProcess(long userId){
        this.resPlayerOnOffLine(userId, true);
    }
    
    public void reqEnterRoomUDPMessageHandler(Player player, RoomMessage.ReqUDPEnterRoom msgData, IoSession udpSession){
        String _roleId = UniqueId.toBase36(player.getRoleId());
        TankFightRoom _room  = (TankFightRoom)this.getPlayerPlaceRoom(player.getUserId());
        _room.onPlayerUDPReady(_roleId, udpSession);
    }
    
    public void reqReqMoveKeyDataHandler(Player player, RoomMessage.ReqMoveKeyData msgData, IoSession udpSession){
        TankFightRoom _room  = (TankFightRoom)this.getPlayerPlaceRoom(player.getUserId());
        _room.OnFrameMoveMessage(msgData, udpSession);
    }
}
