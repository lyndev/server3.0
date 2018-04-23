package game.server.world.chat.handler;

import game.core.command.Handler;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;

/**
 * 
 * @author ZouZhaopeng
 */
public class LWChatInitializeOverHandler extends Handler
{
    private final Player player;

    public LWChatInitializeOverHandler(Player player)
    {
        this.player = player;
    }
    
    @Override
    public void action()
    {
        GameWorld.getInstance().getChatManager().clientInitializeOver(player); //聊天
        GameWorld.getInstance().getFriendManager().clientInitializeOver(player);//好友
    }
    
}
