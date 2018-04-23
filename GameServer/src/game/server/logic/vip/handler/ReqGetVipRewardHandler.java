/**
 * @date 2014/9/17
 * @author ChenLong
 */
package game.server.logic.vip.handler;

import game.core.command.Handler;
import game.message.VipMessage;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 请求领取Vip奖励
 *
 * @author ChenLong
 */
public class ReqGetVipRewardHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqGetVipRewardHandler.class);

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            VipMessage.ReqGetRewards reqMsg = (VipMessage.ReqGetRewards) this.getMessage().getData();
            player.getVipManager().ReqGetVipReward(reqMsg);
        }
        else
        {
            logger.error("player == null");
        }
    }
}
