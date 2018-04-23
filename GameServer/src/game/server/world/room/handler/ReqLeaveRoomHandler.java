/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.room.handler;

import game.core.command.Handler;
import game.message.RoomMessage;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ReqLeaveRoomHandler extends Handler
{   
    private static final Logger logger = Logger.getLogger(ReqLeaveRoomHandler.class);

    @Override
    public void action()
    {
        Player player = (Player)getAttribute("player");
         if (player != null)
        {
            GameWorld.getInstance().getRoomManager().onPlayerLeaveRoom(player, (RoomMessage.ReqLeaveRoom) getMessage().getData());
        }
        else
        {
            logger.error("ReqLeaveRoomHandler player is null");
        }
    }
}
