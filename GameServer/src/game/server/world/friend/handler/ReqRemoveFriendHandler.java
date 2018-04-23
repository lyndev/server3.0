/**
 * @date 2014/10/9
 * @author ChenLong
 */
package game.server.world.friend.handler;

import game.core.command.Handler;
import game.message.FriendMessage;
import game.server.world.GameWorld;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class ReqRemoveFriendHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqRemoveFriendHandler.class);

    @Override
    public void action()
    {   
         IoSession session = getMessage().getSession();
        long userId = (long) getAttribute("userId");
        if (userId > 0)
        {
            GameWorld.getInstance().getFriendManager().onReqRemoveFriend(userId, (FriendMessage.ReqRemoveFriend) getMessage().getData());
        }
        else
        {
            logger.error("ReqRemoveFriendHandler userId = " + userId);
        }
    }
}
