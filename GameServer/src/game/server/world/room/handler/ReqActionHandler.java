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
 * @author undakunteera
 */
public class ReqActionHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqActionHandler.class);
    @Override
    public void action()
    {
         Player player = (Player)getAttribute("player");
        if (player != null)
        {
            GameWorld.getInstance().getRoomManager().reqPlayerAction(player, (RoomMessage.ReqAction) getMessage().getData());//.onReqApplyFriend(userId, (FriendMessage.ReqApplyFriend) getMessage().getData());
        }
        else
        {
            logger.error("ReqCreateRoomHandler userId  player is null");
        }
    }
}