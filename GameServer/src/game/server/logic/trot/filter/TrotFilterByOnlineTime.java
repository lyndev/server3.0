/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.trot.filter;

import game.server.logic.struct.Player;

/**
 * 
 * @date   2014-7-16
 * @author pengmian
 */
public class TrotFilterByOnlineTime extends TrotFilter
{
    private long onlineTime = -1;

    @Override
    public boolean isValid(Player player)
    {
        return onlineTime == -1 ? false : (System.currentTimeMillis() - player.getLastConnectTime()) / 1000 > onlineTime; 
    }

    @Override
    public void setParameter(String parameter)
    {
        try
        {
            onlineTime = Integer.parseInt(parameter);
            if (onlineTime < 0)
            {
                logger.error("TrotFilterByOnlineTime 参数无法解析：[" + parameter + "]");
            }
        }
        catch(NumberFormatException ex)
        {
            logger.error("TrotFilterByOnlineTime 参数无法解析：[" + parameter + "]");
        }
    }

}
