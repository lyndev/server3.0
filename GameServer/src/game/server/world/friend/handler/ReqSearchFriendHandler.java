/**
 * @date 2014/10/10
 * @author ChenLong
 */
package game.server.world.friend.handler;

import game.core.command.Handler;
import game.message.FriendMessage;
import game.server.world.GameWorld;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ReqSearchFriendHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqSearchFriendHandler.class);

    @Override
    public void action()
    {
        long userId = (long) getAttribute("userId");
        if (userId > 0)
        {
            GameWorld.getInstance().getFriendManager().onReqSearchFriend(userId, (FriendMessage.ReqSearchFriend) getMessage().getData());
        }
        else
        {
            logger.error("ReqSearchFriendHandler userId = " + userId);
        }
    }
}
