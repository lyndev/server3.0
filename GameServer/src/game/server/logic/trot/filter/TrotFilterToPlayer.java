
package game.server.logic.trot.filter;

import game.server.logic.struct.Player;
import game.server.util.UniqueId;

/**
 * 
 * @date   2014-7-16
 * @author pengmian
 */
public class TrotFilterToPlayer extends TrotFilter
{
    private String[] players;

    @Override
    public boolean isValid(Player player)
    {
        if (players == null)
        {
            return false;
        }
        
        for (String userId : players)
        {
            if (UniqueId.toBase10(userId) == player.getUserId())
            {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void setParameter(String parameter)
    {
        players = parseParameter(parameter);
        if (players == null || players.length == 0)
        {
            logger.error("TrotFilterToPlayer 参数无法解析：[" + parameter + "]");
        }
        
    }
}
