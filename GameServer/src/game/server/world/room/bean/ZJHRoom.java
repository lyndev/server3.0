/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.room.bean;

import com.google.protobuf.ByteString;
import game.core.command.ICommand;
import game.core.message.SMessage;
import game.core.timer.SimpleTimerProcessor;
import game.core.timer.TimerEvent;
import game.message.RoomMessage;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.util.MessageUtils;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.room.RoomManager;
import game.server.world.wplayer.WPlayer;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Administrator
 */
public class ZJHRoom extends Room
{
    private final static Logger logger = Logger.getLogger(RoomManager.class);

    public final int KIND_ID = 3;           // 游戏 I D
    public final int GAME_PLAYER = 7;       // 游戏人数
    public final int HAND_MAX_COUNT = 3;    // 扑克数目

    // 桌面状态
    public final int GS_FREE = 1;           // 空闲
    public final int GS_PLAYING = 2;        // 对局状态

    //游戏状态
    public final int GS_T_FREE = 1;         // 等待开始
    public final int GS_T_SCORE = 1;        // 叫分状态

    //扑克定义
    public byte[] m_cbCardListData =
    {
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, // 方块 A - K
        0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, // 梅花 A - K
        0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, // 红桃 A - K
        0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D    // 黑桃 A - K
    };

    //放弃事件
    //看牌事件
    //比牌事件
    //开牌事件
    //加注事件
    public int m_bOperaCount;                                       // 操作次数
    public int m_iCurrentUserIndex;                                 // 当前用户
    public int m_wBankerUser;                                       // 庄家用户
    public int[] m_wFlashUser = new int[GAME_PLAYER];               // 动画用户

    public int m_lCompareCount;                                     // 比牌状态
    boolean m_bGameEnd;                                             // 结束状态

    byte[][] m_cbHandCardData = new byte[GAME_PLAYER][HAND_MAX_COUNT];      // 桌面扑克
    int[] m_wCompardUser = new int[GAME_PLAYER];                            // 扑克数组

    //下注信息 
    public int[] m_lTableScore = new int[GAME_PLAYER];                      // 下注数目
    public int[] m_lUserMaxScore = new int[GAME_PLAYER];                    // 最大下注
    public int m_lMaxCellScore;                                             // 单元上限
    public int m_lCellScore = 100;                                                // 单元下注, 当前桌面的单元注
    public int m_lCurrentTimes = 1;                                             // 当前倍数
    public boolean[] m_bMingZhu = new boolean[GAME_PLAYER];                 // 看牌下注
    public int m_handUp = 0;

    //定时器 0~30
    public final int IDI_GAME_COMPAREEND = 1;                               // 结束定时器
    public final int IDI_GAME_OPENEND = 2;                                  // 结束定时器

    public final int TIME_GAME_COMPAREEND = 6000;                           // 结束定时器
    public final int TIME_GAME_OPENEND = 6000;                              // 结束定时器
    public final int INVALID_CHAIR = 0;
    private final ZJHCardController cardControllder = new ZJHCardController();// 棋牌控制器
    private final int ACTION_TIME_OUT = 60;
    private final int ACTION_DELAY_START_TIME_OUT = 2;

    private ScheduledFuture m_actionTimerFuture;
    private ScheduledFuture m_actionDelayStart;
    private int m_winnerLocation = -1;                                          // 赢家的位置索引

    // 动作循环计时器
    private final class FightActionTimer extends TimerEvent
    {
        private final ICommand command;

        public FightActionTimer(long interval, ICommand command, boolean bLoopFix)
        {
            super(interval * 1000, interval * 1000, bLoopFix);
            this.command = command;
        }

        @Override
        public void run()
        {
            this.command.action();
        }
    }

    private final class SpeakerActionTimer extends TimerEvent
    {
        private final ICommand command;

        public SpeakerActionTimer(long interval, ICommand command)
        {

            super(interval * 1000);
            this.command = command;
        }

        @Override
        public void run()
        {
            this.command.action();
        }
    }

    // 结束原因
    public enum GameEndReasonType
    {
        NONE("无类型", 0),
        GER_NO_PLAYER("没有玩家", 1),
        GER_COMPARECARD("比牌结束", 2),
        GER_USER_LEFT("用户强退", 3),
        GER_OPENCARD("开牌结束", 4);

        private String name;
        private int index;

        private GameEndReasonType(String name, int index)
        {
            this.name = name;
            this.index = index;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getIndex()
        {
            return index;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }
    }

    public void Test_DealHandCard()
    {

        logger.info("测试-扎金话-洗牌");
        Stack<Byte> newCardList = cardControllder.shuffle(this.m_cbCardListData);
        System.out.println(Arrays.toString(newCardList.toArray()));
        System.out.println(newCardList.size());

        logger.info("测试-扎金话-发牌");
        cardControllder.RandCardList(newCardList, m_cbHandCardData, HAND_MAX_COUNT);

        for (byte[] playerCard : m_cbHandCardData)
        {
            String _show = "";
            for (byte card : playerCard)
            {
                _show = _show + cardControllder.getCardTypeByValue(card).getName();
            }
            System.out.println(Arrays.toString(playerCard) + " : " + cardControllder.GetCardPatternType(playerCard, HAND_MAX_COUNT).getName() + ":" + _show);

            byte[] playerCarads = playerCard;
            String _showA = "", _showB = "";
            for (byte card : playerCarads)
            {
                _showA = _showA + cardControllder.getCardTypeByValue(card).getName();
            }
            for (int i = 0; i < m_cbHandCardData.length; i++)
            {
                _showB = "";
                for (byte card : m_cbHandCardData[i])
                {
                    _showB = _showB + cardControllder.getCardTypeByValue(card).getName();
                }
                System.out.println(_showA + " 和 " + _showB + "比较" + cardControllder.CompareCard(playerCarads, m_cbHandCardData[i]));
            }
        }
    }

    public ZJHRoom()
    {
        this.m_seat = new boolean[GAME_PLAYER];
        for (int i = 0; i < GAME_PLAYER; i++)
        {
            this.m_seat[i] = false;
        }
    }

    public void onFightActionTimerEnd()
    {
        logger.info("action timeout player speak index ：" + this.getCurrentSpeekIndex());
        FightPlayerZJH _curSpeekPlayer = (FightPlayerZJH) this.getFightPlayerByIndex(this.getCurrentSpeekIndex());
        this.OnUserGiveUp(_curSpeekPlayer.getRoleId());
    }

    // 复位桌子
    void RepositTableFrameSink()
    {
        logger.info("reset table");
        this.cancelActionTimer();
        this.setState(State.END);

        // 游戏变量
        m_bOperaCount = 0;
        m_iCurrentUserIndex = INVALID_CHAIR;
        m_lCompareCount = 0;
        m_handUp = 0;

        // 下注信息
        m_lMaxCellScore = 0;
        m_lCellScore = 0;
        m_lCurrentTimes = 1;

        // 其他信息
        m_bMingZhu = new boolean[GAME_PLAYER];
        m_lTableScore = new int[GAME_PLAYER];
        m_lUserMaxScore = new int[GAME_PLAYER];

        m_wFlashUser = new int[GAME_PLAYER];
        m_cbHandCardData = new byte[GAME_PLAYER][HAND_MAX_COUNT];
        this.m_winnerLocation = -1;
    }

    // 游戏开始
    @Override
    public boolean onGameStart()
    {
        logger.info("zjh game start");

        // 设置状态
        this.setState(Room.State.FIGHT);

        // 最大下注
        Long lTimes = 5L; // TODO根据房间等级读取最大下注（单次加注）

        Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
            int index = _player.getLocation();
            m_lUserMaxScore[index] = _player.getCellScore();

            // 判断单注
            Long Temp = lTimes;
            if (m_lUserMaxScore[index] < 10100)
                Temp = 1L;
            else if (m_lUserMaxScore[index] < 101000)
                Temp = 2L;
            else if (m_lUserMaxScore[index] < 1010000)
                Temp = 3L;
            else if (m_lUserMaxScore[index] < 10100000)
                Temp = 4L;
            if (lTimes > Temp)
                lTimes = Temp;
        }

        //最大下注
        for (int i = 0; i < GAME_PLAYER; i++)
        {
            m_lUserMaxScore[i] = Math.min(m_lUserMaxScore[i], m_lMaxCellScore * 101);
        }

        // 洗牌
        Stack<Byte> newCardList = cardControllder.shuffle(this.m_cbCardListData);

        // 分发扑克
        cardControllder.RandCardList(newCardList, m_cbHandCardData, HAND_MAX_COUNT);

        // 设置用户状态
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
            _player.setPlayerStatus(FightPlayerZJH.StatusType.PLAYING);
        }

        this.m_handUp = this.getReadyCount();

        // 随机设置庄家
        if (m_wBankerUser == INVALID_CHAIR)
            m_wBankerUser = RandomUtils.nextInt() % this.m_handUp;

        m_iCurrentUserIndex = m_wBankerUser;

        // 发送数据棋牌开始
        RoomMessage.ResGameStartFightData.Builder resMsg = RoomMessage.ResGameStartFightData.newBuilder();

        // 桌面数据
        RoomMessage.FightDeskData.Builder _fightDeskData = this.getFightDeskData();
        if (_fightDeskData != null)
        {
            resMsg.setFightDeskData(_fightDeskData);
        }

        // 玩家手上的牌
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
            RoomMessage.PlayerFightData.Builder _msg = RoomMessage.PlayerFightData.newBuilder();
            RoomMessage.ZJHPlayerFightData.Builder _zjhPlayerMsg = RoomMessage.ZJHPlayerFightData.newBuilder();
            byte[] cards = this.m_cbHandCardData[_player.getLocation()];
            String _showA = UniqueId.toBase36(_player.getRoleId()) + " ";
            for (int j = 0; j < HAND_MAX_COUNT; j++)
            {
                _showA = _showA + cardControllder.getCardTypeByValue(cards[j]).getName();
                _zjhPlayerMsg.addCards(Byte.toString(cards[j]));
            }
            logger.info("player hand card：" + _showA + " card byte:" + cards[0] + ": " + cards[1] + ":" + cards[2]);
            _zjhPlayerMsg.setPlayerId(UniqueId.toBase36(_player.getRoleId()));
            _msg.setZjhPlayerFightData(_zjhPlayerMsg);
            resMsg.addPlayerFightData(_msg);
        }

        // 发送给每个玩家
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
            WPlayer _wplayer = GameWorld.getInstance().getPlayer(_player.getUserId());
            if (_wplayer != null)
            {
                IoSession session = _wplayer.getSession();
                MessageUtils.send(session, new SMessage(RoomMessage.ResGameStartFightData.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
            }
            else
            {
                logger.info("get world player fail");
            }
        }

        // 第一个说话的玩家        
        this.createActionDelayStart(m_wBankerUser, 1);

        return true;
    }

    public void createActionTimer()
    {
        this.cancelActionTimer();
        ICommand _actionCommand = new ICommand()
        {
            @Override
            public void action()
            {
                onFightActionTimerEnd();
            }
        };

        FightActionTimer _actionTimer = new FightActionTimer(ACTION_TIME_OUT, _actionCommand, true);
        this.m_actionTimerFuture = SimpleTimerProcessor.getInstance().addEvent(_actionTimer);
    }

    public void createActionDelayStart(final int index, long delayTime)
    {
        this.cancelActionDelayTiemr();
        ICommand _actionCommand = new ICommand()
        {
            @Override
            public void action()
            {
                playerSpeekAction(index);
            }
        };

        SpeakerActionTimer _actionTimer = new SpeakerActionTimer(delayTime, _actionCommand);
        this.m_actionDelayStart = SimpleTimerProcessor.getInstance().addEvent(_actionTimer);
    }

    public void cancelActionTimer()
    {
        if (this.m_actionTimerFuture != null)
        {
            this.m_actionTimerFuture.cancel(true);
        }
    }

    public void cancelActionDelayTiemr()
    {
        if (this.m_actionDelayStart != null)
        {
            this.m_actionDelayStart.cancel(true);
        }
    }

    // 执行当前该谁说话
    public void playerSpeekAction(int index)
    {
        FightPlayerZJH _bankerPlayer = (FightPlayerZJH) this.getFightPlayerByIndex(index);
        if (_bankerPlayer == null)
        {
            logger.error("speaker player is nul, indexl:" + index);
            return;
        }

        // 发送消息
        RoomMessage.Action.Builder resMsg = RoomMessage.Action.newBuilder();
        resMsg.setActionType(ActionType.A_THINKING.getIndex());
        resMsg.setPlayerId(_bankerPlayer.getStrRoleId());
        this.resActionResult(resMsg.build(), PlayerType.All, null);

        // 增加一个计时器
        this.createActionTimer();
    }

    // 下一位说话
    public void nextplayerSpeekAction(long delayTime)
    {
        int _nextSpeekIndex = this.getNextSpeekIndex();
        this.m_iCurrentUserIndex = _nextSpeekIndex;
        if(delayTime == 0){
            playerSpeekAction(_nextSpeekIndex);
        } else {
            this.createActionDelayStart(_nextSpeekIndex, delayTime);
        }
        
    }

    // 获取当前说话的玩家
    public int getCurrentSpeekIndex()
    {
        return this.m_iCurrentUserIndex;
    }

    // 获取下一个说话的玩家
    public int getNextSpeekIndex()
    {
        int _nextUserIndex = (this.getCurrentSpeekIndex() + 1) % this.m_handUp;

        while ((this.getFightPlayerByIndex(_nextUserIndex) == null) || (this.getFightPlayerByIndex(_nextUserIndex).getPlayerStatus() != FightPlayerBase.StatusType.PLAYING))
        {
            _nextUserIndex = (_nextUserIndex + 1) % this.m_handUp;
        }
        logger.info("next speak player index:" + _nextUserIndex);
        return _nextUserIndex;
    }

    public RoomMessage.FightDeskData.Builder getFightDeskData()
    {

        // 获取庄玩家id
        Long bankerPlayerId = 0L;
        FightPlayerBase _bankerPlayer = this.getFightPlayerByIndex(m_iCurrentUserIndex);
        if (_bankerPlayer == null)
        {
            return null;
        }
        bankerPlayerId = _bankerPlayer.getRoleId();

        // 桌面数据
        RoomMessage.FightDeskData.Builder resMsg = RoomMessage.FightDeskData.newBuilder();
        RoomMessage.ZJHDeskData.Builder resZJHMsg = RoomMessage.ZJHDeskData.newBuilder();
        resZJHMsg.setMaxScore(m_lMaxCellScore);
        resZJHMsg.setCellScore(m_lCellScore);
        resZJHMsg.setCurrentTimes(m_lCurrentTimes);
        resZJHMsg.setMaxScoreLimit(m_lMaxCellScore);
        resZJHMsg.setCurrentPlayerId(UniqueId.toBase36(bankerPlayerId));
        resMsg.setZjhDeskData(resZJHMsg);
        return resMsg;
    }

    //游戏结束
    boolean OnEventGameEnd(GameEndReasonType cbReason)
    {
        switch (cbReason)
        {
            case GER_COMPARECARD:   //比牌结束
            case GER_NO_PLAYER:     //没有玩家
            {
                m_bGameEnd = true;

                // 唯一玩家
                this.m_winnerLocation = 0;
                for (int i = 0; i < GAME_PLAYER; i++)
                {
                    FightPlayerBase _player = this.getFightPlayerByIndex(i);
                    if (_player != null)
                    {
                        if (_player.getPlayerStatus() == FightPlayerBase.StatusType.PLAYING)
                        {
                            this.m_winnerLocation = i;
                            if (GameEndReasonType.GER_COMPARECARD == cbReason)
                                m_wBankerUser = i;
                        }
                    }
                }
                this.GameOver();
                return true;
            }

            case GER_USER_LEFT:     //用户强退

            case GER_OPENCARD:      //开牌结束   
            {
                if (m_bGameEnd)
                    return true;
                m_bGameEnd = true;

                //胜利玩家
                //计算分数
                //处理税收
                //开牌结束
                //扑克数据
                //修改积分
                //写入积分
                //if(wChairID==GAME_PLAYER)
                //{
                //结束数据
                //}
                //else
                //{
                //发送信息
                //结束游戏
                //}
                return true;
            }
        }

        return false;
    }

    //游戏消息处理
    @Override
    public boolean OnGameMessage(RoomMessage.ReqAction reqMsg)
    {
        RoomMessage.Action _action = reqMsg.getActions();
        ActionType _actionType = ActionType.getTypeByValue(_action.getActionType());
        String _actionPlayerId = _action.getPlayerId();
        FightPlayerBase _player = this.getFightPlayer(UniqueId.toBase10(_actionPlayerId));
        
        if (_player == null)
        {
            logger.info("excute action fail , not find this player：" + _actionPlayerId);
            return false;
        }
        
        Room.State _roomState = this.getState();
        if (_roomState == Room.State.FIGHT)
        {
            int _actionSpeakPlayerLocation = _player.getLocation();
            if(_actionSpeakPlayerLocation != this.m_iCurrentUserIndex){
                logger.info("not speaker action handler invalid：" + _actionSpeakPlayerLocation);
                return false;
            }
        }
        
       
        switch (_actionType)
        {
            case A_READY:           // 准备
                if (_roomState != Room.State.FIGHT)
                {
                    _player.setReady(true);
                    this.OnPlayerReady(_actionPlayerId, true);
                    this.increaseReadyCountRef();
                    // 检查是否可以开局
                    if (this.isCanFightStart())
                    {
                        this.onGameStart();
                    }
                }

                return true;
            case A_UNREADY:         // 取消准备
                if (_roomState != Room.State.FIGHT)
                {
                    this.OnPlayerReady(_actionPlayerId, false);
                    _player.setReady(false);
                    this.decreaseReadyCountRef();
                }
                return true;
            case A_GIVE_UP:         //用户放弃(弃牌) 玩家超时自动弃牌
            {
                //消息处理
                return this.OnUserGiveUp(UniqueId.toBase10(_actionPlayerId));
            }
            case A_LOOK_CARD:       //用户看牌
                //消息处理
                return this.OnUserLookCard(UniqueId.toBase10(_actionPlayerId));
            case A_OPEN_CARD:       //用户开牌
            {

            }
            case A_COMPARE_CARD:    //用户比牌
            {
                //参数效验
                int _curSpeekIndex = this.getCurrentSpeekIndex();
                int _curPalyerLocation = _player.getLocation();
                if (_curSpeekIndex != _curPalyerLocation)
                {
                    return false;
                }

                long _targetPlayerId = 0L;

                // 暂停计时器
                this.cancelActionTimer();

                //消息处理
                return this.OnUserCompareCard(UniqueId.toBase10(_actionPlayerId), _targetPlayerId);
            }
            case A_ADD_SCORE:       // 用户加注
            {
                //消息处理
                RoomMessage.ZJH_AddScore _addScoreData = _action.getZjhAddScore();
                if(_addScoreData == null){
                    logger.error("add score data is null");
                    return false;
                }
                int _addScore = _addScoreData.getAddScoreCount();
                return this.OnUserActionScore(UniqueId.toBase10(_actionPlayerId), _addScore, 1);
            }
            case A_FOLLOW_SCORE:    // 玩家跟注
            {
                return this.OnUserActionScore(UniqueId.toBase10(_actionPlayerId), 0, 1);
            }
            case A_WAIT_COMPARE:    //等待比牌
            {
                //用户效验
                //状态判断
                //消息处理
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean leaveRoom(Long roleId)
    {
        FightPlayerZJH _player = (FightPlayerZJH) this.getFightPlayer(roleId);
        if (_player == null)
        {
            logger.info("onPlayerLeave , player is null");
            return false;
        }
        int _playerLocation = _player.getLocation();

        super.leaveRoom(roleId);

        Room.State _roomState = this.getState();
        if (_roomState == Room.State.FIGHT)
        {
            //人数统计
            int wPlayerCount = 0;
            Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
            for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
            {
                FightPlayerZJH _active_player = (FightPlayerZJH) entry.getValue();
                if (_active_player.getPlayerStatus() == FightPlayerZJH.StatusType.PLAYING)
                {
                    wPlayerCount++;
                }
            }
            //判断结束
            if (wPlayerCount >= 2)
            {
                if (m_iCurrentUserIndex == _playerLocation)
                {
                    this.nextplayerSpeekAction(0);
                }
            }
            else
                OnEventGameEnd(GameEndReasonType.GER_NO_PLAYER);
        }
        return true;
    }

    @Override
    public void resWillExcuteAction(RoomMessage.Action willAction)
    {

    }

    @Override
    public void resActionResult(RoomMessage.Action resultAction, PlayerType playerType, Long playerId)
    {
        Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
        switch (playerType)
        {
            case Self:
                for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
                {
                    FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
                    //指定玩家
                    if (playerId != _player.getRoleId())
                        continue;

                    WPlayer _wplayer = GameWorld.getInstance().getPlayer(_player.getUserId());
                    if (_wplayer != null)
                    {
                        IoSession session = _wplayer.getSession();
                        RoomMessage.ResAction.Builder resMsg = RoomMessage.ResAction.newBuilder();
                        resMsg.setAction(resultAction);
                        MessageUtils.send(session, new SMessage(RoomMessage.ResAction.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
                    }
                    else
                    {
                        logger.info("get world player fail");
                    }
                }
                break;
            case Others:
                for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
                {
                    FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
                    // 排除指定玩家
                    if (playerId == _player.getRoleId())
                        continue;

                    WPlayer _wplayer = GameWorld.getInstance().getPlayer(_player.getUserId());
                    if (_wplayer != null)
                    {
                        IoSession session = _wplayer.getSession();
                        RoomMessage.ResAction.Builder resMsg = RoomMessage.ResAction.newBuilder();
                        resMsg.setAction(resultAction);
                        MessageUtils.send(session, new SMessage(RoomMessage.ResAction.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
                    }
                    else
                    {
                        logger.info("get world player fail");
                    }
                }
                break;
            case All:
                // 同步给所有玩家动作
                for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
                {
                    FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
                    WPlayer _wplayer = GameWorld.getInstance().getPlayer(_player.getUserId());
                    if (_wplayer != null)
                    {
                        IoSession session = _wplayer.getSession();
                        RoomMessage.ResAction.Builder resMsg = RoomMessage.ResAction.newBuilder();
                        resMsg.setAction(resultAction);
                        MessageUtils.send(session, new SMessage(RoomMessage.ResAction.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
                    }
                    else
                    {
                        logger.info("get world player fail");
                    }
                }
                break;
        }
    }

    // 放弃事件
    boolean OnUserGiveUp(Long playerId)
    {
        FightPlayerZJH _player = (FightPlayerZJH) this.getFightPlayer(playerId);
        if (_player == null)
        {
            logger.info("on user giveup fail , player is null");
            return false;
        }

        //设置数据
        _player.setPlayerStatus(FightPlayerZJH.StatusType.OVER);

        //发送消息
        RoomMessage.Action.Builder resMsg = RoomMessage.Action.newBuilder();
        resMsg.setActionType(ActionType.A_GIVE_UP.getIndex());
        resMsg.setPlayerId(_player.getStrRoleId());
        this.resActionResult(resMsg.build(), PlayerType.All, null);

        //人数统计
        int wPlayerCount = 0;
        Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerZJH _active_player = (FightPlayerZJH) entry.getValue();
            if (_active_player.getPlayerStatus() == FightPlayerZJH.StatusType.PLAYING)
            {
                wPlayerCount++;
            }
        }

        //判断结束
        if (wPlayerCount >= 2)
        {
            if (m_iCurrentUserIndex == _player.getLocation())
            {
                this.nextplayerSpeekAction(0);
            }
        }
        else
            OnEventGameEnd(GameEndReasonType.GER_NO_PLAYER);

        return true;
    }

    // 看牌事件
    boolean OnUserLookCard(Long playerId)
    {
        FightPlayerZJH _player = (FightPlayerZJH) this.getFightPlayer(playerId);
        if (_player == null)
        {
            return false;
        }
        
        m_bMingZhu[_player.getLocation()] = true;

        //发送数据            
        RoomMessage.Action.Builder resMsg = RoomMessage.Action.newBuilder();
        RoomMessage.ZJH_LookCard.Builder zjh_lookCardMsg = RoomMessage.ZJH_LookCard.newBuilder();
        byte[] cards = this.m_cbHandCardData[_player.getLocation()];
        for (int j = 0; j < HAND_MAX_COUNT; j++)
        {
            zjh_lookCardMsg.addCards(Byte.toString(cards[j]));
        }
        resMsg.setActionType(ActionType.A_LOOK_CARD.getIndex());
        resMsg.setPlayerId(_player.getStrRoleId());
        resMsg.setZjhLookcard(zjh_lookCardMsg);

        // 发送给看牌玩家（包含牌数据）
        this.resActionResult(resMsg.build(), PlayerType.Self, playerId);

        // 发送给其他玩家（不包含牌数据）
        RoomMessage.Action.Builder resMsgOther = RoomMessage.Action.newBuilder();
        resMsgOther.setActionType(ActionType.A_LOOK_CARD.getIndex());
        resMsgOther.setPlayerId(_player.getStrRoleId());
        this.resActionResult(resMsgOther.build(), PlayerType.Others, playerId);

        return true;
    }

    // 开牌
    boolean OnUserOpenCard(Long playerId)
    {
        /*
        FightPlayerZJH _player = (FightPlayerZJH) this.getFightPlayer(playerId);
        if (_player == null)
        {
            return false;
        }
        int _playerLocation = _player.getLocation();

        //发送数据            
        RoomMessage.Action.Builder resMsg = RoomMessage.Action.newBuilder();
        RoomMessage.ZJH_LookCard.Builder zjh_lookCardMsg = RoomMessage.ZJH_LookCard.newBuilder();
        byte[] cards = this.m_cbHandCardData[_player.getLocation()];
        ByteString byteStringCardsData = ByteString.copyFrom(cards);
        for (int j = 0; j < HAND_MAX_COUNT; j++)
        {
            zjh_lookCardMsg.setCards(j, byteStringCardsData.substring(j));
        }
        resMsg.setActionType(ActionType.A_OPEN_CARD.getIndex());
        resMsg.setPlayerId(String.valueOf(playerId));
        resMsg.setZjhLookcard(zjh_lookCardMsg);
        this.resActionResult(resMsg.build());
         */
        return true;
    }

    // 比牌事件
    boolean OnUserCompareCard(Long speekPlayerId, Long targetPlayerId)
    {
        if (!(m_lCompareCount > 0))
            return false;

        //比较大小
        FightPlayerZJH _speekPlayer = (FightPlayerZJH) this.getFightPlayer(speekPlayerId);
        FightPlayerZJH _targetPlayer = (FightPlayerZJH) this.getFightPlayer(targetPlayerId);
        int _compareResult = this.cardControllder.CompareCard(m_cbHandCardData[_speekPlayer.getLocation()], m_cbHandCardData[_targetPlayer.getLocation()]);

        //状态设置
        m_lCompareCount = 0;

        // 胜利用户
        long _winPlayerId = 0L;
        long _losePlayerId = 0L;

        if (_compareResult == 1)
        {
            _winPlayerId = speekPlayerId;
            _losePlayerId = targetPlayerId;
            _targetPlayer.setPlayerStatus(FightPlayerBase.StatusType.OVER);
        }
        else
        {
            _winPlayerId = targetPlayerId;
            _losePlayerId = speekPlayerId;
            _speekPlayer.setPlayerStatus(FightPlayerBase.StatusType.OVER);
        }

        //人数统计
        int iPlayerCount = 0;
        for (int i = 0; i < this.m_handUp; i++)
        {
            FightPlayerZJH _player = (FightPlayerZJH) this.getFightPlayerByIndex(i);
            if (_player != null)
            {
                if (_player.getPlayerStatus() == FightPlayerBase.StatusType.PLAYING)
                    iPlayerCount++;
            }
        }

        //继续游戏
        if (iPlayerCount >= 2)
        {
            this.nextplayerSpeekAction(2);
        }
        else
            m_iCurrentUserIndex = -1;

        //发送数据 
        RoomMessage.Action.Builder resMsg = RoomMessage.Action.newBuilder();
        RoomMessage.ZJH_CompareCard.Builder zjh_compareCard = RoomMessage.ZJH_CompareCard.newBuilder();
        zjh_compareCard.setCompareResult(_compareResult);
        zjh_compareCard.setComparePlayerId(_targetPlayer.getStrRoleId());
        resMsg.setActionType(ActionType.A_COMPARE_CARD.getIndex());
        resMsg.setPlayerId(_speekPlayer.getStrRoleId());
        resMsg.setZjhComparecard(zjh_compareCard);
        this.resActionResult(resMsg.build(), PlayerType.All, null);

        //结束游戏
        if (iPlayerCount < 2)
        {
            this.OnEventGameEnd(GameEndReasonType.GER_COMPARECARD);
        }

        return true;
    }

    //开牌事件
    boolean OnUserOpenCard(int wUserID)
    {
        //效验参数
        //清理数据
        //保存扑克
        //比牌玩家
        //查找最大玩家
        //胜利玩家
        //构造数据
        //发送数据
        //结束游戏
        return true;
    }

    //加注事件
    boolean OnUserActionScore(Long playerId, int addScore, int type)
    {
        
        FightPlayerZJH _fightPlayer = (FightPlayerZJH) this.getFightPlayer(playerId);
        if(_fightPlayer == null){
            logger.error("not find player");
            return false;
        }
        
        logger.info("add score type :" + type + "add score:" + addScore);
        
        //当前倍数                 
        if(addScore < this.m_lCellScore && type == 1){
            logger.error("client add score fail, < this.m_lCellScore");
            return false;
        }
        
        // 重置单元下注倍数
        this.m_lCellScore = addScore;
        
        int _willAddScore = this.getFollowScore(playerId, type, addScore);
        
        //用户注金
        m_lTableScore[_fightPlayer.getLocation()] += _willAddScore;

        // 扣住玩家的金币， 当玩家金币不足以扣住的时候需要提醒
        Player _player = PlayerManager.getPlayerByUserId(_fightPlayer.getUserId());
        if (_player != null)
        {
            _player.getBackpackManager().subResource(ResourceType.GOLD, -_willAddScore, m_bGameEnd, Reasons.ARENA_ATTACK, new Date());
        }
        
        // 发送加注成功
        RoomMessage.Action.Builder resMsg = RoomMessage.Action.newBuilder();
        RoomMessage.ZJH_AddScore.Builder _addScoreData = RoomMessage.ZJH_AddScore.newBuilder();
        _addScoreData.setAddScoreCount(_willAddScore);
        resMsg.setActionType(Room.ActionType.A_FOLLOW_SCORE.getIndex());
        resMsg.setPlayerId(_fightPlayer.getStrRoleId());
        this.resActionResult(resMsg.build(), PlayerType.All, null);
        
        this.nextplayerSpeekAction(0);
        
        return true;
    }

    
    int getFollowScore(Long playerId, int type, int addScore){
        
        FightPlayerBase _player = this.getFightPlayer(playerId);
        if(_player== null){
            logger.error("get player fail");
            return 0;
        }
        
        // type = 1 加注，type = 2 跟注
        int _willAddScore = 0;
        boolean _bMyselfLookCard = this.m_bMingZhu[_player.getLocation()];
        boolean _bAllLookCard = this.isAllLookCard();
        if(type == 1)
            
            // 自己处于闷牌状态
            if(!_bMyselfLookCard){
                _willAddScore = this.m_lCellScore;
            
            // 自己看牌了
            } else {
                if(_bAllLookCard){
                    _willAddScore =  addScore;
                } else {
                    float _isDoubleMultiple = addScore / this.m_lCellScore;
                    if(_isDoubleMultiple >= 2){
                        _willAddScore = addScore;
                    } else {
                        _willAddScore = addScore * 2;
                    }
                }
            }
        else if(type == 2){
            if(_bAllLookCard){
                _willAddScore =  this.m_lCellScore * 2;
            } else {
                _willAddScore =  this.m_lCellScore;
            }
        }
        return _willAddScore;
    }
    
    // 是不是所有人都看牌了
    boolean isAllLookCard(){
        boolean _isAllLook = true;
        Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerBase _player =  entry.getValue();
            int _locaiton = _player.getLocation();
            if(!this.m_bMingZhu[_locaiton]){
                return false;
            }
        }
        return _isAllLook;
    }

    @Override
    public void onPlayerOnOffline(String playerId, boolean bOnline)
    {
        RoomMessage.Action.Builder _action = RoomMessage.Action.newBuilder();
        if (bOnline)
        {
            _action.setActionType(Room.ActionType.A_ONLINE.getIndex());
        }
        else
        {
            _action.setActionType(Room.ActionType.A_OFFLINE.getIndex());
        }
        _action.setPlayerId(playerId);
        this.resActionResult(_action.build(), PlayerType.All, null);
    }

    @Override
    public void OnPlayerReady(String playerId, boolean bReady)
    {
        RoomMessage.Action.Builder _action = RoomMessage.Action.newBuilder();
        if (bReady)
        {
            _action.setActionType(Room.ActionType.A_READY.getIndex());
        }
        else
        {
            _action.setActionType(Room.ActionType.A_UNREADY.getIndex());
        }
        _action.setPlayerId(playerId);
        this.resActionResult(_action.build(), PlayerType.All, null);
    }

    @Override
    public void ResetRoom()
    {
        logger.info("sub class reset room data");
        this.RepositTableFrameSink();
        super.ResetRoom();
    }

    public void GameOver()
    {
        // 赢家赢取的积分或者金币
        int lWinnerScore = 0;
        for (int i = 0; i < GAME_PLAYER; i++)
        {
            lWinnerScore += m_lTableScore[i];
        }

        // TODO:税收处理
        RoomMessage.ZJH_GameResult.Builder game_result = RoomMessage.ZJH_GameResult.newBuilder();
        RoomMessage.ResFightResult.Builder resFightResult = RoomMessage.ResFightResult.newBuilder();

        Map<Long, FightPlayerBase> fightPlayers = this.getRoomFightPlayers();
        int _winnerSeat = -1;
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            RoomMessage.ZJH_GameResult.PlayerCard.Builder _playerCard = RoomMessage.ZJH_GameResult.PlayerCard.newBuilder();

            FightPlayerZJH _active_player = (FightPlayerZJH) entry.getValue();
            _active_player.setPlayerStatus(FightPlayerZJH.StatusType.NONE);
            int _location = _active_player.getLocation();
            byte[] cards = this.m_cbHandCardData[_location];
            for (int j = 0; j < HAND_MAX_COUNT; j++)
            {
                _playerCard.addCards(Byte.toString(cards[j]));
            }

            // 该玩家的积分输赢
            if (_active_player.getLocation() != this.m_winnerLocation)
            {
                _playerCard.setGetScore(-m_lTableScore[_location]);
            }
            else
            {
                _playerCard.setGetScore(lWinnerScore);
                _winnerSeat = _location;
            }
            _playerCard.setPlayerId(UniqueId.toBase36(_active_player.getRoleId()));
            game_result.addPlayerCard(_playerCard);
        }
        game_result.setWinnerLocation(_winnerSeat);
        resFightResult.setZjhGameResult(game_result);

        // 发送给每个玩家
        for (Map.Entry<Long, FightPlayerBase> entry : fightPlayers.entrySet())
        {
            FightPlayerZJH _player = (FightPlayerZJH) entry.getValue();
            WPlayer _wplayer = GameWorld.getInstance().getPlayer(_player.getUserId());
            if (_wplayer != null)
            {
                IoSession session = _wplayer.getSession();
                MessageUtils.send(session, new SMessage(RoomMessage.ResFightResult.MsgID.eMsgID_VALUE, resFightResult.build().toByteArray()));
            }
            else
            {
                logger.info("get world player fail");
            }
        }

        // 复位桌子
        this.ResetRoom();
    }
}
