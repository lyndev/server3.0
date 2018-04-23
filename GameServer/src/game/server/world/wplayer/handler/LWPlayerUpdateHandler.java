package game.server.world.wplayer.handler;

import game.core.command.Handler;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;

/**
 *
 * @author Administrator
 */
public class LWPlayerUpdateHandler extends Handler
{
    
    private final Player player;
    
    public LWPlayerUpdateHandler(Player player)
    {
        this.player = player;
    }
    
    @Override
    public void action()
    {
        GameWorld.getInstance().updatePlayer(player);
    }
    
}
