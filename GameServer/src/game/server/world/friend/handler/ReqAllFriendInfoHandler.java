/**
 * @date 2014/10/8
 * @author ChenLong
 */
package game.server.world.friend.handler;

import game.core.command.Handler;
import game.server.world.GameWorld;
import org.apache.log4j.Logger;

/**
 * 获取所有好友信息
 *
 * @author ChenLong
 */
public class ReqAllFriendInfoHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqAllFriendInfoHandler.class);

    @Override
    public void action()
    {
        long userId = (long)getAttribute("userId");
        if (userId > 0)
        {
            GameWorld.getInstance().getFriendManager().onReqAllFriendInfo(userId);
        }
    }
}
