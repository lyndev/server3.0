/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.msghandler;

import game.core.command.Handler;
import game.message.LoginMessage;
import game.testrobot.robot.RobotPlayer;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ResLoginSuccessHandler extends Handler
{
    private final static Logger logger = Logger.getLogger(ResLoginSuccessHandler.class);

    @Override
    public void action()
    {
        RobotPlayer player = (RobotPlayer) getAttribute("PLAYER");
        if (player != null)
        {
            LoginMessage.ResLoginSuccess resMsg = (LoginMessage.ResLoginSuccess) this.getMessage().getData();
            player.loginSuccess(resMsg);
        }
        else
        {
            logger.error("cannot get PLAYER attribute");
        }
    }
}
