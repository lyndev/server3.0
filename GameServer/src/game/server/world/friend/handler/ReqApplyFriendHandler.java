/**
 * @date 2014/10/8
 * @author ChenLong
 */
package game.server.world.friend.handler;

import game.core.command.Handler;
import game.message.FriendMessage;
import game.server.world.GameWorld;
import org.apache.log4j.Logger;

/**
 * 申请好友
 *
 * @author ChenLong
 */
public class ReqApplyFriendHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqApplyFriendHandler.class);

    @Override
    public void action()
    {
        long userId = (long) getAttribute("userId");
        if (userId > 0)
        {
            GameWorld.getInstance().getFriendManager().onReqApplyFriend(userId, (FriendMessage.ReqApplyFriend) getMessage().getData());
        }
        else
        {
            logger.error("ReqApplyFriendHandler userId = " + userId);
        }
    }
}
