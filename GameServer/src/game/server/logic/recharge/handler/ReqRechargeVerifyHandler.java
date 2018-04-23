/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge.handler;

import game.core.command.Handler;
import game.message.RechargeMessage;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 客户端请求验证充值结果
 *
 * @author ChenLong
 */
public class ReqRechargeVerifyHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqRechargeVerifyHandler.class);

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            RechargeMessage.ReqRechargeVerify reqMsg = (RechargeMessage.ReqRechargeVerify) this.getMessage().getData();
            player.getRechargeManager().reqRecharge(reqMsg);
        }
        else
        {
            logger.error("player == null");
        }
    }
}
