/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.robot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ChenLong
 */
public class RobotPlayerManager
{
    private final static RobotPlayerManager instance;
    private final RobotPlayerMap players = new RobotPlayerMap();

    static
    {
        instance = new RobotPlayerManager();
    }

    public static RobotPlayerManager getInstance()
    {
        return instance;
    }

    public synchronized void put(RobotPlayer player)
    {

    }

    public synchronized void putByUserName(String userName, RobotPlayer player)
    {
        players.userNamePlayers.put(userName, player);
    }

    public synchronized void putByUserId(long userId, RobotPlayer player)
    {
        players.userIdPlayers.put(userId, player);
    }

    public synchronized void putByRoleId(long roleId, RobotPlayer player)
    {
        players.roleIdPlayers.put(roleId, player);
    }

    public synchronized RobotPlayer getByRoleId(long roleId)
    {
        return players.roleIdPlayers.get(roleId);
    }

    public synchronized RobotPlayer getByUserId(long userId)
    {
        return players.userIdPlayers.get(userId);
    }

    public synchronized RobotPlayer getByUserName(String userName)
    {
        return players.userNamePlayers.get(userName);
    }

    private class RobotPlayerMap
    {
        public final Map<String, RobotPlayer> userNamePlayers = new ConcurrentHashMap<>(); // userName --> Player

        public final Map<Long, RobotPlayer> userIdPlayers = new ConcurrentHashMap<>(); // userId --> Player
        public final Map<Long, RobotPlayer> roleIdPlayers = new ConcurrentHashMap<>(); // roleId --> Player
    }
}
