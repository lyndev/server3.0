/**
 * @date 2014/7/2
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
public class ResCreateRoleFailedHandler extends Handler
{
    private final static Logger logger = Logger.getLogger(ResLoginFailedHandler.class);

    @Override
    public void action()
    {
        RobotPlayer player = (RobotPlayer) getAttribute("PLAYER");
        if (player != null)
        {
            LoginMessage.ResCreateRoleFailed resMsg = (LoginMessage.ResCreateRoleFailed) this.getMessage().getData();
            player.createRoleFailed(resMsg);
        }
        else
        {
            logger.error("cannot get PLAYER attribute");
        }
    }

}
