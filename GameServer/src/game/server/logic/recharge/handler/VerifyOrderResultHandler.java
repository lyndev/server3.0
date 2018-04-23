/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge.handler;

import game.core.command.ICommand;
import game.server.logic.player.PlayerManager;
import game.server.logic.recharge.bean.OrderBean;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 充值单验证结果返回 注: 此Handler在Player所在线线程执行
 *
 * @author ChenLong
 */
public class VerifyOrderResultHandler implements ICommand
{
    private final static Logger logger = Logger.getLogger(VerifyOrderResultHandler.class);
    private final OrderBean bean;

    public VerifyOrderResultHandler(OrderBean bean)
    {
        this.bean = bean;
    }

    @Override
    public void action()
    {
        Player player = PlayerManager.getPlayerByUserId(bean.getUserId());
        if (player != null)
        {
            player.getRechargeManager().verifyResult(bean);
        }
        else
        {
            logger.error("cannot find Player Object userId = " + bean.getUserId());
        }
    }
}
