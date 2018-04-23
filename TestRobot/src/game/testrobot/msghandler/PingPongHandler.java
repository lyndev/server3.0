package game.testrobot.msghandler;

/**
 * @date 2014/7/14
 * @author ChenLong
 */
import game.core.command.Handler;
import game.message.LoginMessage;
import game.testrobot.robot.RobotPlayer;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class PingPongHandler extends Handler
{
    private final static Logger logger = Logger.getLogger(PingPongHandler.class);
    @Override
    public void action()
    {
        RobotPlayer player = (RobotPlayer) getAttribute("PLAYER");
        if (player != null)
        {
            //LoginMessage.PingPong resMsg = (LoginMessage.PingPong) this.getMessage().getData();
            //player.resPingPongIncrement(resMsg);
        }
        else
        {
            logger.error("cannot get PLAYER attribute");
        }
    }
}
