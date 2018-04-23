/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.line;

import game.testrobot.robot.RobotPlayerMap;
import game.core.command.CommandProcessor;
import game.testrobot.robot.RobotPlayer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class Line extends CommandProcessor
{
    private final static Logger logger = Logger.getLogger(Line.class);
    private int lineId; // 线ID

    private final Map<String, RobotPlayer> userNamePlayers = new ConcurrentHashMap<>(); // userName -> RobotPlayer

    public Line(int lineId)
    {
        super(Line.class.getSimpleName() + "_" + lineId);
        this.lineId = lineId;
    }

    public void putPlayerByUserName(String userName, RobotPlayer player)
    {
        userNamePlayers.put(userName, player);
    }

    public int getLineId()
    {
        return lineId;
    }

    public void setLineId(int lineId)
    {
        this.lineId = lineId;
    }

    /**
     * 定时器触发tick
     */
    public void tick()
    {
        long currentTimeMillis = System.currentTimeMillis();
        //System.out.println("tick in line " + lineId);

        playerTick(currentTimeMillis);
    }

    @Override
    public void writeError(String message)
    {
        logger.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        logger.error(message, t);
    }

    private void playerTick(long currentTimeMillis)
    {
        for (Map.Entry<String, RobotPlayer> entry : userNamePlayers.entrySet())
        {
            RobotPlayer player = entry.getValue();
            player.tick();
        }
    }
}
