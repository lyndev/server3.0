/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.world.login.handler;

import game.core.command.Handler;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.wplayer.WPlayer;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Administrator
 */
public class LWReconnectSuccessHandler extends Handler
{
    private final IoSession session;
    public LWReconnectSuccessHandler(IoSession session)
    {
        this.session = session;
    }
    @Override
    public void action()
    {
        String userIdStr = (String)getAttribute("userId");
        WPlayer player = GameWorld.getInstance().getPlayer(UniqueId.toBase10(userIdStr));
        if (player != null)
        {
            GameWorld.getInstance().reconnectSuccess(player, session);
        }
    }
}
