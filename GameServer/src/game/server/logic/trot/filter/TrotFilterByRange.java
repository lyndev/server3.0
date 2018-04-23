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
public class TrotFilterByRange extends TrotFilter
{
    private int minLevel = -1;
    private int maxLevel = -1;

    @Override
    public boolean isValid(Player player)
    {
        if (minLevel == -1 || maxLevel == -1)
        {
            return false;
        }
        // todo 排行榜
        return true;
    }

    @Override
    public void setParameter(String parameter)
    {
        String[] strLevel = parseParameter(parameter);
        if (strLevel != null && strLevel.length == 2)
        {
            minLevel = Integer.parseInt(strLevel[0]);
            maxLevel = Integer.parseInt(strLevel[1]);
        }
        else
        {            
            logger.error("TrotFilterByRange 参数无法解析：[" + parameter + "]");
        }
    }
    
    
    
}
