
package game.server.logic.trot.filter;

import game.server.logic.struct.Player;

/**
 * 
 * @date   2014-7-16
 * @author pengmian
 */
public class TrotFilterByLevel extends TrotFilter
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
        return (player.getRoleLevel() >= minLevel && player.getRoleLevel() <= maxLevel);
    }

    @Override
    public void setParameter(String parameter)
    {
        String[] splitLevel = parseParameter(parameter);
        if (splitLevel != null && splitLevel.length == 2)
        {
            minLevel = Integer.parseInt(splitLevel[0]);
            maxLevel = Integer.parseInt(splitLevel[1]);
        }
        else
        {   
            logger.error("TrotFilterByLevel 参数无法解析：[" + parameter + "]");
        }
    }

}
