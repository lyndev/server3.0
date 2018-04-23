

package game.server.logic.trot.filter;

import game.server.logic.struct.Player;
import org.apache.log4j.Logger;

/**
 * 
 * @date   2014-7-15
 * @author pengmian
 */
public abstract class TrotFilter 
{
    protected static Logger logger = Logger.getLogger(TrotFilter.class);
    
    /**
     * 判断是否需要接受消息的玩家
     * @param player
     * @return
     */
    public abstract boolean isValid(Player player);
    
    /**
     * 为fliter设置参数
     * @param parameter
     */
    public abstract void setParameter(String parameter);
    
    /**
     * 分割参数，大多数情况下，参数都是以"_"分割
     * @param parameter
     * @return
     */
    public String[] parseParameter(String parameter)
    {
        if (parameter == null || parameter.trim().equalsIgnoreCase(""))
        {
            return null;
        }
        return parameter.split("_");
    }
}
