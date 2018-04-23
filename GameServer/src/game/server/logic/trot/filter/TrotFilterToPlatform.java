package game.server.logic.trot.filter;


import game.server.logic.struct.Player;

/**
 * 
 * @date   2014-7-16
 * @author pengmian
 */
public class TrotFilterToPlatform extends TrotFilter
{    

    private String[] platformIds;
  
    public TrotFilterToPlatform()
    {
        platformIds = null;
    }

    @Override
    public boolean isValid(Player player)
    {
        if (platformIds == null)
        {
            return false;
        }

        for (String platformId : platformIds)
        {
            if (player.getPlatformId() == Integer.parseInt(platformId))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setParameter(String parameter)
    {
        platformIds = parseParameter(parameter);
        if (platformIds == null || platformIds.length == 0)
        {
            logger.error("TrotFilterToPlatform 参数无法解析：[" + parameter + "]");
        }
    }
    
}
