
package game.server.logic.rebate.handler;

import game.core.command.Handler;
import game.message.RebateMessage;
import game.server.logic.struct.Player;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqRebateInfoHandler extends Handler
{

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        player.getRebateManager().onRebateInfo((RebateMessage.ReqRebateInfo) getMessage().getData());
    }
    
}
