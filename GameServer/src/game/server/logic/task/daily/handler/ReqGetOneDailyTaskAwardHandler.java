/**
 * @date 2014/9/4
 * @author ChenLong
 */
package game.server.logic.task.daily.handler;

import game.core.command.Handler;
import game.message.TaskMessage;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ReqGetOneDailyTaskAwardHandler extends Handler
{
    private final static Logger log = Logger.getLogger(ReqGetOneDailyTaskAwardHandler.class);

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            TaskMessage.ReqGetOneDailyTaskAward reqMsg = (TaskMessage.ReqGetOneDailyTaskAward) this.getMessage().getData();
            player.getDailyTaskManager().onGetOneDailyTaskAward(reqMsg);
        }
        else
        {
            log.error("player == null");
        }
    }
}
