package game.server.world.login.handler;

import game.core.command.Handler;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;

/**
 *
 * @author Administrator
 */
public class LWLoginSuccessHandler extends Handler
{
    @Override
    public void action()
    {
        GameWorld.getInstance().loginSuccess((Player) getAttribute("player"));
    }
}
