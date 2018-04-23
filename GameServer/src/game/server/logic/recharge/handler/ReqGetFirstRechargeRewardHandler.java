/**
 * @date 2014/9/25
 * @author ChenLong
 */
package game.server.logic.recharge.handler;

import game.core.command.Handler;
import game.message.RechargeMessage;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ReqGetFirstRechargeRewardHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqGetFirstRechargeRewardHandler.class);

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            RechargeMessage.ReqGetFirstRechargeReward reqMsg = (RechargeMessage.ReqGetFirstRechargeReward) this.getMessage().getData();
            player.getRechargeManager().reqGetFirstRechargeReward(reqMsg);
        }
        else
        {
            logger.error("player == null");
        }
    }
}
