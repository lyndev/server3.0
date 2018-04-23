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
public class ReqUDPEnterRoomHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqUDPEnterRoomHandler.class);

    @Override
    public void action()
    {
        Player player = (Player)getAttribute("player");
         if (player != null)
        {
            GameWorld.getInstance().getRoomManager().reqEnterRoomUDPMessageHandler(player, (RoomMessage.ReqUDPEnterRoom) getMessage().getData(), getMessage().getSession());
            logger.info("服务器接收到了客户端发送的udp数据");
        }
        else
        {
            logger.error("ReqUDPEnterRoomHandler player is null");
        }
    }
}
