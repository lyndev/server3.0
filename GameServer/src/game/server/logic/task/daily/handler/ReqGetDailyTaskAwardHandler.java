/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.task.daily.handler;

import game.core.command.Handler;
import game.message.TaskMessage;
import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ReqGetDailyTaskAwardHandler extends Handler
{
    private final static Logger log = Logger.getLogger(ReqGetDailyTaskAwardHandler.class);

    @Override
    public void action()
    {
//        Player player = (Player) getAttribute("player");
//        if (player != null)
//        {
//            TaskMessage.ReqGetDailyTaskAward reqMsg = (TaskMessage.ReqGetDailyTaskAward) this.getMessage().getData();
//            player.getDailyTaskManager().getDailyTaskAward(reqMsg);
//        }
//        else
//        {
//            log.error("player == null");
//        }
    }
}
