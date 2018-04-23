
package game.server.logic.trot.filter;

import game.server.logic.struct.Player;

/**
 * 
 * @date   2014-7-16
 * @author pengmian
 */
public class TrotFilterToAll extends TrotFilter
{
    @Override
    public boolean isValid(Player player)
    {
        return true;
    }

    @Override
    public void setParameter(String parameter)
    {
    }
}
