package game.server.logic.mail.handler;

import game.core.command.Handler;
import game.message.MailMessage;
import game.server.logic.struct.Player;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqMailListHandler extends Handler
{
    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        player.getMailManager().onReqMailList((MailMessage.ReqMailList) (this.getMessage().getData()));
    }
}
