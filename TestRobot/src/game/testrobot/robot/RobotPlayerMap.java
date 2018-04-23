/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.robot;

import game.testrobot.robot.RobotPlayer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class RobotPlayerMap
{
    private final static Logger logger = Logger.getLogger(RobotPlayerMap.class);
    private final Map<Long, RobotPlayer> userPlayers = new ConcurrentHashMap<>(); // userId -> RobotPlayer
    private final Map<Long, RobotPlayer> rolePlayers = new ConcurrentHashMap<>(); // roleId -> RobotPlayer

    public Map<Long, RobotPlayer> getRolePlayers()
    {
        return rolePlayers;
    }

    public RobotPlayer put(RobotPlayer player)
    {
        return put(player.getUserId(), player.getRoleId(), player);
    }

    public RobotPlayer put(long userId, long roleId, RobotPlayer player)
    {
        userPlayers.put(userId, player);
        return rolePlayers.put(roleId, player);
    }

    public RobotPlayer getByUserId(long userId)
    {
        return userPlayers.get(userId);
    }

    public RobotPlayer getByRoleId(long roleId)
    {
        return rolePlayers.get(roleId);
    }

    public RobotPlayer remove(RobotPlayer player)
    {
        RobotPlayer player1 = userPlayers.remove(player.getUserId());
        RobotPlayer player2 = rolePlayers.remove(player.getRoleId());
        if (player1 != player2 || player1 != player)
        {
            logger.error("player = " + player + "player1 = " + player1 + "player2 = " + player2);
        }
        return player;
    }

    public RobotPlayer removeByUserId(long userId)
    {
        RobotPlayer player = userPlayers.remove(userId);
        rolePlayers.remove(player.getRoleId());
        return player;
    }

    public RobotPlayer removeByRoleId(long roleId)
    {
        RobotPlayer player = rolePlayers.remove(roleId);
        userPlayers.remove(player.getUserId());
        return player;
    }
}
