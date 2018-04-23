
package game.server.world.room.handler;
import game.core.command.Handler;
import game.message.RoomMessage;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import game.server.world.room.RoomManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
/**
 *
 * @author Administrator
 */
public class ReqCreateRoomHandler  extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqCreateRoomHandler.class);

    @Override
    public void action()
    {
        Player player = (Player)getAttribute("player");
        if (player != null)
        {
            GameWorld.getInstance().getRoomManager().onPlayerCreateRoom(player, (RoomMessage.ReqCreateRoom) getMessage().getData());//.onReqApplyFriend(userId, (FriendMessage.ReqApplyFriend) getMessage().getData());
        }
        else
        {
            logger.error("ReqCreateRoomHandler userId  player is null");
        }
    } 
}
