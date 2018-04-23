package game.server.logic.player;

import game.core.message.SMessage;
import game.message.NotifyMessage;
import game.server.GameServer;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.dao.UserDao;
//import game.server.logic.card.Card;
import game.server.logic.constant.CloseSocketReason;
import game.server.logic.constant.SessionKey;
import game.server.logic.robot.NewRobotBuilder;
import game.server.logic.struct.Player;
import game.server.logic.struct.PlayerState;
import game.server.logic.trot.filter.TrotFilter;
import game.server.util.MessageUtils;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
//import java.util.Map.Entry;
//import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.mina.core.session.IoSession;

/**
 *
 * <b>玩家管理类.</b>
 * <p>
 * 提供诸如在线玩家列表等状态维护的管理工具，但不包含任何功能性管理，如玩家修改昵称等.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class PlayerManager
{
    private static final Map<Long, Player> userIdPlayers = new ConcurrentHashMap<>(); // userId --> Player
    private static final Map<String, Player> userNamePlayers = new ConcurrentHashMap<>(); // userName --> Player
    private static final Map<String, Player> soleIdPlayers = new ConcurrentHashMap<>(); // soleId --> Player

    private static volatile boolean isLoadRobots = false;

    /**
     * 加载机器人玩家数据.
     *
     * @return 机器人数量
     */
    public static synchronized int loadRobots()
    {
        if (!isLoadRobots)
        {
            isLoadRobots = true;
            List<UserRoleBean> list = UserDao.selectAllRobots();
            if (list == null || list.isEmpty())
            {
                if (new NewRobotBuilder().build() > 0)
                {
                    list = UserDao.selectAllRobots();
                }
            }

            if (list != null && !list.isEmpty())
            {
                for (UserRoleBean bean : list)
                {
                    Player player = new Player();
                    player.loadInitialize(bean);
                    userIdPlayers.put(player.getUserId(), player);
                    userNamePlayers.put(player.getUserName(), player);
                    soleIdPlayers.put(player.getUserName(), player);
                }

                return list.size();
            }
        }

        return 0;
    }

    /**
     * 获取所有机器人数据.
     *
     * @return
     */
    public static synchronized List<Player> getRobots()
    {
        List<Player> list = new ArrayList<>();
        for (Map.Entry<Long, Player> entry : userIdPlayers.entrySet())
        {
            if (entry.getValue().isRobot())
                list.add(entry.getValue());
        }

        return list;
    }

    /**
     * 注册一个玩家.
     *
     * @param session 连接会话
     * @param player 玩家信息
     */
    public static synchronized void register(IoSession session, Player player)
    {
        session.setAttribute(SessionKey.USER_ID.name(), player.getUserId());
        player.setSession(session);
        userIdPlayers.put(player.getUserId(), player);
        userNamePlayers.put(player.getUserName(), player);
    }

    public static synchronized void removeDetachPlayer(Player player)
    {
        userIdPlayers.remove(player.getUserId());
        userNamePlayers.remove(player.getUserName());
    }

    /**
     * 根据账号ID获取玩家对象.
     *
     * @param userId 账号ID
     * @return
     */
    public static synchronized Player getPlayerByUserId(long userId)
    {
        return userIdPlayers.get(userId);
    }

    public static synchronized Player getPlayerByUserName(String userName)
    {
        return userNamePlayers.get(userName);
    }

    public static synchronized Player getPlayerBySoleId(String soleId)
    {
        return soleIdPlayers.get(soleId);
    }
        
    /**
     * 根据账号ID获取玩家对象.
     *
     * @param userId 账号ID
     * @return
     */
    public static synchronized Player getPlayerByUserId(String userId)
    {
        if (userId != null && !userId.isEmpty())
            return userIdPlayers.get(UniqueId.toBase10(userId));
        else
            return null;
    }

    public static synchronized void changeRoleName(Player player, String beforeName){
        userNamePlayers.remove(beforeName);
        userNamePlayers.put(player.getRoleName(), player);
    }
    
    /**
     * 根据连接会话获取所关联的玩家对象.
     *
     * @param session 连接会话
     * @return
     */
    public static synchronized Player getPlayerBySession(IoSession session)
    {
        Player player = null;
        Object userIdObj = session.getAttribute(SessionKey.USER_ID.name());
        if (userIdObj != null && userIdObj instanceof Long)
        {
            long userId = (Long) userIdObj;
            if (userId != 0)
                player = userIdPlayers.get(userId);
        }
        return player;
    }

    /**
     * 在线玩家数
     *
     * @return
     */
    public static synchronized int onLinePlayerNum()
    {
        int count = 0;
        for (Map.Entry<Long, Player> entry : userIdPlayers.entrySet())
        {
            Player player = entry.getValue();
            if (!player.isRobot() && player.getPlayerState().getState() == PlayerState.State.NORMAL)
                ++count;
        }
        gcTrigger(count);
        return count;
    }

    /**
     * 每天凌晨4-6点间 && 在线数小于10时 执行一次Full GC
     *
     * @param online
     */
    private static void gcTrigger(int online)
    {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (online < 10
                && !DateUtils.isSameDay(new Date(), new Date(GameServer.getInstance().getLastGCTime()))
                && hour >= 4 && hour <= 6)
        {
            System.gc();
            GameServer.getInstance().setLastGCTime(System.currentTimeMillis());
        }
    }

    /**
     * 广播走马灯公告
     *
     * @param message 要播放的公告
     * @param filter 走马灯过滤器
     */
    public static synchronized void boardcastNotify(String message, TrotFilter filter)
    {
        NotifyMessage.ResNotify.Builder builder = NotifyMessage.ResNotify.newBuilder();
        builder.setContext(message);
        SMessage msg = new SMessage(NotifyMessage.ResNotify.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        for (Map.Entry<Long, Player> entry : userIdPlayers.entrySet())
        {
            Player player = entry.getValue();
            if (!player.isRobot()
                    && player.getPlayerState().getState() == PlayerState.State.NORMAL
                    && filter != null
                    && filter.isValid(player))
                MessageUtils.send(player.getSession(), msg);
        }
    }

    public static synchronized void boardcastNotify(String message)
    {
        NotifyMessage.ResNotify.Builder builder = NotifyMessage.ResNotify.newBuilder();
        builder.setContext(message);
        SMessage msg = new SMessage(NotifyMessage.ResNotify.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        for (Map.Entry<Long, Player> entry : userIdPlayers.entrySet())
        {
            Player player = entry.getValue();
            if (!player.isRobot() && player.getPlayerState().getState() == PlayerState.State.NORMAL)
                MessageUtils.send(player.getSession(), msg);
        }
    }

    public static synchronized void serverRestartNotify()
    {
        for (Map.Entry<Long, Player> entry : userIdPlayers.entrySet())
        {
            Player player = entry.getValue();
            if (!player.isRobot()
                    && player.getPlayerState().getState() == PlayerState.State.NORMAL
                    && player.getSession() != null)
            {
                MessageUtils.closeSocket(player.getSession(), CloseSocketReason.SERVER_RESTART);
                player.getSession().close(false);
            }
        }
    }

    public static synchronized void closeAllPlayerSession()
    {
        for (Map.Entry<Long, Player> entry : userIdPlayers.entrySet())
        {
            Player player = entry.getValue();
            if (!player.isRobot() && player.getSession() != null)
            {
                player.getSession().close(false);
            }
        }
    }
}
