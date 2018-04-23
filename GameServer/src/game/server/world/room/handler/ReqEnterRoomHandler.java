
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
public class ReqEnterRoomHandler  extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqEnterRoomHandler.class);

    @Override
    public void action()
    {
        Player player = (Player)getAttribute("player");
         if (player != null)
        {
            GameWorld.getInstance().getRoomManager().onPlayerEnterRoom(player, (RoomMessage.ReqEnterRoom) getMessage().getData());
        }
        else
        {
            logger.error("ReqEnterRoomHandler player is null");
        }
    }  
}
