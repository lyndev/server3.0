/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.player.handler;

import game.core.command.Handler;
import game.message.PlayerMessage;
import game.server.logic.player.service.PlayerService;
import game.server.logic.struct.Player;

/**
 *
 * @author YangHanzhou
 */
public class ReqFinishQuestionHandler extends Handler
{

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            //PlayerService.getInstance().onReqQuestionFinish(player,  (PlayerMessage.ReqFinishQuestionnaire) getMessage().getData());
        }
    }
    
}
