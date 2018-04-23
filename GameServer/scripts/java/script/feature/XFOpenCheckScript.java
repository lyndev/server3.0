/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package script.feature;

import game.core.script.IScript;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanTemplet;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 检测仙府开启脚本
 *  
 * @date   2014-8-5
 * @author pengmian
 */
public class XFOpenCheckScript  implements IScript
{

    private final Logger logger = Logger.getLogger(XFOpenCheckScript.class);
    
    @Override
    public int getId()
    {
        return 1025;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object call(int scriptId, Object arg)
    {
        String reason = new String();
        if (arg instanceof Map)
        {
            do
            {

                Map<String, Object> map = (HashMap) arg;
                if (map.isEmpty())
                {
                    logger.error("map.isEmpty()");
                    reason = BeanTemplet.getLanguage(220024).getQ_lgtext();
                    break;
                }

                Player owner = (Player) map.get("player");
                if (owner == null)
                {
                    logger.error("player == null！");
                    reason = BeanTemplet.getLanguage(220024).getQ_lgtext();
                    break;
                }
                
                else
                {
                    Item item = (Item) map.get("obj");
                    if (item == null || item.getId() != 159999)
                    {
                        reason = BeanTemplet.getLanguage(220025).getQ_lgtext();
                    }
                    else
                    {
                        reason = "ok";
                    }
                }
            } while (false);
        }
        return reason; 
    }

}
