/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.login.handler;

import game.core.command.Handler;
import game.server.logic.login.service.LoginService;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;

/**
 *
 * @author Administrator
 */
public class WLLoginSuccessHandler extends Handler
{
    private int friendNum = 0;

    public void setFriendNum(int friendNum)
    {
        this.friendNum = friendNum;
    }

    @Override
    public void action()
    {
        Player player = PlayerManager.getPlayerByUserId((long) getAttribute("userId"));
        if (player != null)
        {
            player.modifyFriendNum(friendNum);
            LoginService.getInstance().wlLoginSuccess(player);
        }
    }

}
