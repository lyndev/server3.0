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
public class ReqSendAudioMessageHandler extends Handler
{
    private static final Logger LOG = Logger.getLogger(ReqSendAudioMessageHandler.class);
    
    @Override
    public void action()
    {
        Player player = (Player)getAttribute("player");
        if (player != null)
        {
            GameWorld.getInstance().getChatManager().onSendAudioMessage(player, (ChatMessage.ReqSendAudioMessage) getMessage().getData());
        }
        else
        {
            LOG.error("ReqSendAudioMessageHandler player ==null ");
        }
    }
}
