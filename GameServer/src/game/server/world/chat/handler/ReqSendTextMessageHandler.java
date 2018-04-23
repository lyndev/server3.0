package game.server.world.chat.handler;

import game.core.command.Handler;
import game.message.ChatMessage;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqSendTextMessageHandler extends Handler
{
    private static final Logger LOG = Logger.getLogger(ReqSendTextMessageHandler.class);
    
    @Override
    public void action()
    {
        Player player = (Player)getAttribute("player");
        if (player != null)
        {
            GameWorld.getInstance().getChatManager().onSendTextMessage(player, (ChatMessage.ReqSendTextMessage) getMessage().getData());
        }
        else
        {
            LOG.error("ReqSendTextMessageHandler player ==null ");
        }
    }
}
