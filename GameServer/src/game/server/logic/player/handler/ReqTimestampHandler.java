/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.player.handler;

import game.core.command.Handler;
import game.server.logic.struct.Player;
import game.server.logic.vip.handler.ReqGetVipRewardHandler;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ReqTimestampHandler extends Handler
{
    private static final Logger logger = Logger.getLogger(ReqGetVipRewardHandler.class);

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            player.reqTimestamp();
        }
        else
        {
            logger.error("player == null");
        }
    }
}
