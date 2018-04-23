/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.world.login.handler;

import game.core.command.Handler;
import game.server.world.GameWorld;

/**
 *
 * @author YangHanzhou
 */
public class LWFlushDetachPlayerHandler extends Handler
{

    @Override
    public void action()
    {
        long userId = (long)(getAttribute("userId"));
        GameWorld.getInstance().LWFlushDetachPlayer(userId);
    }
}
