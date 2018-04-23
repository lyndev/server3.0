/**
 * @date 2014/10/13
 * @author ChenLong
 */
package game.server.world.friend.handler;

import game.core.command.Handler;
import game.server.world.GameWorld;

/**
 * 凌晨5点重置
 *
 * @author ChenLong
 */
public class ResetFriendManagerHandler extends Handler
{
    @Override
    public void action()
    {
        GameWorld.getInstance().getFriendManager().tick5();
    }
}
