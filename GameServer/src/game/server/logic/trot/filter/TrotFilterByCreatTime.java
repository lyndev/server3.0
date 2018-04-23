package game.server.logic.trot.filter;


import game.server.logic.struct.Player;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 
 * @date   2014-7-16
 * @author pengmian
 */
public class TrotFilterByCreatTime extends TrotFilter
{
    private Date beg = null;
    private Date end = null;


    @Override
    public boolean isValid(Player player)
    {
        if (beg == null || end == null)
        {
            return false;
        }
        return (player.getCreateRoleTime() >= beg.getTime() && player.getCreateRoleTime() <= end.getTime());
    }

    @Override
    public void setParameter(String parameter)
    {
        String[] strs = parseParameter(parameter);
        if (strs != null && strs.length == 2)
        {
            try 
            {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                beg = formater.parse(strs[0]);
                end = formater.parse(strs[1]);
            }
            catch (ParseException ex) 
            {
                logger.error("TrotFilterByCreatTime 参数无法解析：[" + parameter + "]");
                beg = end = null;
            }
        }
        else
        {
            logger.error("TrotFilterByCreatTime 参数无法解析：[" + parameter + "]");
        }
    }

}
