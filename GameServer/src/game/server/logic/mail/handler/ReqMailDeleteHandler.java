package game.server.logic.mail.handler;

import game.core.command.Handler;
import game.message.MailMessage;
import game.server.logic.struct.Player;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqMailDeleteHandler extends Handler
{
    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        player.getMailManager().onReqMailDelete((MailMessage.ReqMailDelete) (this.getMessage().getData()));
    }
}
