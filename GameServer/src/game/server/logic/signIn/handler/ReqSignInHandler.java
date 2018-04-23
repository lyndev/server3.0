/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.signIn.handler;

import game.core.command.Handler;
import game.message.SignInMessage;
import game.server.logic.struct.Player;

/**
 *
 * @author Administrator
 */
public class ReqSignInHandler extends Handler
{

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            player.getSignInManager().onReqSignIn((SignInMessage.ReqSignIn) getMessage().getData());
        }
    }
    
}
