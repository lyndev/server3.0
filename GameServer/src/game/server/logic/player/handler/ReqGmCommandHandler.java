/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.player.handler;

import game.core.command.Handler;
import game.message.PlayerMessage;
import game.server.logic.struct.Player;


/**
 *
 * @author Administrator
 */
public class ReqGmCommandHandler extends Handler
{
    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            PlayerMessage.ReqGMCommand msg = (PlayerMessage.ReqGMCommand)getMessage().getData();
            player.doGmCommand(msg.getCommand());
        }
    }
}
