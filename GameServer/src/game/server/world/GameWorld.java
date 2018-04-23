package game.server.world;

import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.core.timer.SimpleTimerProcessor;
import game.core.timer.TimerEvent;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.dao.UserDao;
import game.server.logic.login.handler.WLLoginSuccessHandler;
import game.server.logic.struct.Player;
import game.server.util.MessageUtils;

import game.server.world.chat.ChatManager;
import game.server.world.friend.FriendManager;
import game.server.world.rank.RankServer;
import game.server.world.room.RoomManager;
import game.server.world.wplayer.WPlayer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author YangHanzhou
 */
public class GameWorld extends CommandProcessor
{
    public final static long MAX_PLAYER = 8000;
    private long maxPlayer; // 所能承载的最大玩家数
    private final Logger log = Logger.getLogger(GameWorld.class);
    private final Map<Long, WPlayer> userPlayers = new ConcurrentHashMap<>(); // userId -> Player
    private final FriendManager friendManager = new FriendManager();
    private final ChatManager chatManager = new ChatManager();
    private final RoomManager roomManager = new RoomManager();

    private volatile boolean initialized = false; // 是否初始化完毕

    private GameWorld()
    {
        super("GameWorldThread");
        this.maxPlayer = MAX_PLAYER;
        SimpleTimerProcessor.getInstance().addEvent(new GameWorldTick());       // 计时器
    }

    /**
     * 异步初始化
     *
     * @param maxPlayer
     */
    public void init(long maxPlayer)
    {
        addCommand(new ICommand()
        {
            private long maxPlayer;

            public ICommand set(long maxPlayer)
            {
                this.maxPlayer = maxPlayer;
                return this;
            }

            @Override
            public void action()
            {
                GameWorld.this.maxPlayer = maxPlayer;
                loadAllPlayers();
                initialized = true;
            }
        }.set(maxPlayer));

        // await initCommand finished
        while (initialized != true)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
                log.info("GameWorld.init InterruptedException");
            }
        }

        RankServer.getInstance().start();
    }

    /**
     * 加载所有玩家数据到WorldServer.
     */
    private void loadAllPlayers()
    {
        log.info("in GameWorld, loadAllPlayers");
        List<UserRoleBean> list = UserDao.selectAllPlayers();
        if (list == null || list.isEmpty())
        {
            log.warn("没有玩家数据！");
        }
        else
        {
            for (UserRoleBean obj : list)
            {
                Player player = new Player();
                player.loadInitialize(obj);
            }
        }
    }

    public WPlayer getPlayer(long id)
    {
        return userPlayers.get(id);
    }

    public FriendManager getFriendManager()
    {
        return friendManager;
    }

    public RoomManager getRoomManager()
    {
        return roomManager;
    }

    /**
     * 登陆成功
     *
     * @param player
     */
    public void loginSuccess(Player player)
    {
        if (player == null)
        {
            log.error("player is null!");
            return;
        }

        WPlayer wPlayer = getPlayer(player.getUserId());
        if (wPlayer != null)
        {
            wPlayer.setSession(player.getSession());
            wPlayer.setLineId(player.getLineId());
        }
        else
        {
            wPlayer = new WPlayer(player);
            this.registerPlayer(wPlayer);
            log.info("注册新的玩家到GameWorld。name = " + wPlayer.getRoleName() + "userId:" + player.getUserId());
        }

        // 初始化Friend系统
        friendManager.createInitialize(wPlayer.getUserId());

        // 返回GameLine登陆成功
        WLLoginSuccessHandler handler = new WLLoginSuccessHandler();
        handler.setAttribute("userId", wPlayer.getUserId());
        handler.setFriendNum(friendManager.getFriendNum(wPlayer.getUserId())); // 获取最新的好友数量
        MessageUtils.sendToGameLine(wPlayer.getLineId(), handler);

        //上线通知好友
        friendManager.onlineProcess(wPlayer.getUserId());
        //this.roomManager.offlineProcess(player.getUserId());
    }

    /**
     * 更新玩家数据.
     *
     * @param player
     */
    public void updatePlayer(Player player)
    {
        if (player == null)
        {
            log.error("player is null!");
            return;
        }

        WPlayer wp = getPlayer(player.getUserId());
        if (wp == null)
        {
            log.error("Not found player from WorldServer! userId = " + player.getUserId());
            return;
        }
        wp.setRoleName(player.getRoleName());
        wp.setRoleHead(player.getRoleHead());
        wp.setRoleLevel(player.getRoleLevel());
    }

    /**
     * 清理分离玩家(gameworld 不能直接删除)
     *
     * @param userId
     */
    public void LWFlushDetachPlayer(long userId)
    {
        WPlayer player = getPlayer(userId);
        if (player == null)
        {
            log.error("GameWorld LWFlushDetachPlayer  player is null !");
            return;
        }
        // 设置gameLineId为空
        player.setLineId(null);
    }

    /**
     * 断线重连
     *
     * @param player
     * @param session
     */
    public void reconnectSuccess(WPlayer player, IoSession session)
    {
        Validate.notNull(session);
        player.setSession(session);
    }

    /**
     * 连接断开
     *
     * @param player
     * @param lastConnectTime
     */
    public void connectClosed(WPlayer player, long lastConnectTime)
    {
        player.setSession(null);
        player.setLastConnectTime(lastConnectTime);

        //下线后，通知好友
        friendManager.offlineProcess(player.getUserId());
        this.roomManager.offlineProcess(player.getUserId());
    }

    public class GameWorldTick extends TimerEvent
    {
        public GameWorldTick()
        {
            super(1000, 1 * 1000, true); // 间隔1秒
        }

        @Override
        public void run()
        {
            addCommand(new ICommand()
            {
                @Override
                public void action()
                {
                    long currentTimeMillis = System.currentTimeMillis();
                    {
                        int timeStamp = (int) (currentTimeMillis / 1000L);
                    }
                    
                    try
                    {
                        friendManager.tick(currentTimeMillis);
                    }
                    catch (Throwable t)
                    {
                        log.error("Throwable", t);
                    }
                    
                    try
                    {
                        chatManager.getPrivateChatManager().tick(currentTimeMillis);
                    }
                    catch (Throwable t)
                    {
                        log.error("Throwable", t);
                    }
                }
            });
        }
    }

    @Override
    public void writeError(String message)
    {
        log.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        log.error(message, t);
    }

    public WPlayer registerPlayer(WPlayer player)
    {
        if (userPlayers.size() > this.maxPlayer)
            log.warn("GameWorld player max! userPlayers.size() = " + userPlayers.size());
        return userPlayers.put(player.getUserId(), player);
    }

    public WPlayer unregisterPlayer(long userId)
    {
        return userPlayers.remove(userId);
    }

    public static GameWorld getInstance()
    {
        return GameWorld.Singleton.INSTANCE.getManager();
    }

    public void stopAndAwaitStop()
    {
        requestSaveAll();
        stop(false);
        awaitStop();
    }

    private void requestSaveAll()
    {
        addCommand(new ICommand()
        {
            @Override
            public void action()
            {
                try
                {
                    friendManager.save();
                }
                catch (Throwable t)
                {
                    log.error("Throwable", t);
                }

                try
                {
                    chatManager.save();
                }
                catch (Throwable t)
                {
                    log.error("Throwable", t);
                }
            }
        });
    }

    public void onlineNumCommand()
    {
        addCommand(new ICommand()
        {
            @Override
            public void action()
            {
                log.info("GameWorld online WPlayer num = " + userPlayers.size());
            }
        });
    }

    private enum Singleton
    {
        INSTANCE;

        GameWorld manager;

        Singleton()
        {
            this.manager = new GameWorld();
        }

        GameWorld getManager()
        {
            return manager;
        }
    }

    public Map<Long, WPlayer> getUserPlayers()
    {
        return userPlayers;
    }

    public ChatManager getChatManager()
    {
        return chatManager;
    }

    /**
     * 刷新玩家在WorldServer中记录的禁聊结束时间戳.
     *
     * @param userId 玩家UID
     * @param denyChatTimes 禁聊结束时间戳
     */
    public void refreshDenyChatTimes(long userId, int denyChatTimes)
    {
        addCommand(new ICommand()
        {
            private long userId;
            private int denyChatTimes;

            public ICommand set(long userId, int denyChatTimes)
            {
                this.userId = userId;
                this.denyChatTimes = denyChatTimes;
                return this;
            }

            @Override
            public void action()
            {
                WPlayer wp = getPlayer(userId);
                if (wp == null)
                {
                    log.error("Not found player from WorldServer! userId = " + userId);
                    return;
                }

                wp.setDenyChatTimestamp(denyChatTimes);
            }
        }.set(userId, denyChatTimes));
    }
}
