/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.world.login.handler;

import game.core.command.Handler;
import game.server.world.GameWorld;
import game.server.world.wplayer.WPlayer;

/**
 *
 * @author Administrator
 */
public class LWConnectClosedHandler extends Handler
{
    private final long lastConnectTime;
    public LWConnectClosedHandler(long lastConnectTime)
    {
        this.lastConnectTime = lastConnectTime;
    }
    
    @Override
    public void action()
    {
        WPlayer player = GameWorld.getInstance().getPlayer((long)(getAttribute("userId")));
        if (player != null)
        {
            GameWorld.getInstance().connectClosed(player, lastConnectTime);
        }
    }
    
}
