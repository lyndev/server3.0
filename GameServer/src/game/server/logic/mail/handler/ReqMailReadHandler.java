package game.server.logic.mail.handler;

import game.core.command.Handler;
import game.message.MailMessage;
import game.server.logic.struct.Player;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqMailReadHandler extends Handler
{
    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        player.getMailManager().onReqMailRead((MailMessage.ReqMailRead) (this.getMessage().getData()));
    }
}
