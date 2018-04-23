/**
 * @date 2014/10/11
 * @author ChenLong
 */
package game.server.world.friend.handler;

import game.core.command.Handler;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 请求修改好友上限
 * @author ChenLong
 */
public class ModifyFriendNumHandler extends Handler
{
    private final static Logger logger = Logger.getLogger(ModifyFriendNumHandler.class);
    private long userId;
    private int friendNum;

    public Handler set(long userId, int friendNum)
    {
        this.userId = userId;
        this.friendNum = friendNum;
        return this;
    }

    @Override
    public void action()
    {
        Player player = PlayerManager.getPlayerByUserId(userId);
        if (player != null)
        {
            player.modifyFriendNum(friendNum);
        }
        else
        {
            logger.warn("ModifyFriendNumHandler找不到Player, userId = " + userId);
        }
    }
}
