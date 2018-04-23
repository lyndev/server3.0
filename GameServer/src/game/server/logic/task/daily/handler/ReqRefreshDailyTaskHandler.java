/**
 * @date 2014/8/5
 * @author ChenLong
 */
package game.server.logic.task.daily.handler;

import game.core.command.Handler;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ReqRefreshDailyTaskHandler extends Handler
{
    private final static Logger log = Logger.getLogger(ReqGetDailyTaskAwardHandler.class);

    @Override
    public void action()
    {
//        Player player = (Player) getAttribute("player");
//        if (player != null)
//        {
//            player.getDailyTaskManager().getAllDailyTaskInfo();
//        }
//        else
//        {
//            log.error("player == null");
//        }
    }
}
