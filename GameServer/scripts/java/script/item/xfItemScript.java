/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package script.item;

import game.core.script.IScript;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.item.bean.Item;
import game.server.logic.item.bean.Prop;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.util.MessageUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author YangHanzhou
 */
public class xfItemScript implements IScript
{
    private final Logger log = Logger.getLogger(xfItemScript.class);

    @Override
    public int getId()
    {
        return 1023;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        log.info("Call BoxScript...");
        // 解析参数
        ScriptArgs args = (ScriptArgs) arg;
        Object arg1 = args.get(ScriptArgs.Key.ITEM);
        Object arg2 = args.get(ScriptArgs.Key.PLAYER);
        if (!(arg1 instanceof Prop))
        {
            log.error("仙府道具脚本参数类型错误！ScriptArgs.Key.ITEM class: " + arg1.getClass());
            return null;
        }
        if (!(arg2 instanceof Player))
        {
            log.error("仙府道具脚本参数类型错误！ScriptArgs.Key.PLAYER class: " + arg2.getClass());
            return null;
        }
        
        Item xfItem = (Item)arg1;
        Player player = (Player)arg2;
  
        
        return "true";
    }
}
