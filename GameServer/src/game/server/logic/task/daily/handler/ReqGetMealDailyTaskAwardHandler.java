/**
 * @date 2014/8/5
 * @author ChenLong
 */
package game.server.logic.task.daily.handler;

import game.core.command.Handler;
import game.message.TaskMessage;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 领取“豪华午餐”和“超值晚餐”日常任务奖励
 *
 * @author ChenLong
 */
public class ReqGetMealDailyTaskAwardHandler extends Handler
{
    private final static Logger log = Logger.getLogger(ReqGetMealDailyTaskAwardHandler.class);

    @Override
    public void action()
    {
//        Player player = (Player) getAttribute("player");
//        if (player != null)
//        {
//            TaskMessage.ReqGetMealDailyTaskAward reqMsg = (TaskMessage.ReqGetMealDailyTaskAward) this.getMessage().getData();
//            player.getDailyTaskManager().ReqGetMealDailyTaskAwardHandler(reqMsg);
//        }
//        else
//        {
//            log.error("player == null");
//        }
    }
}
