package script.test;

import game.core.script.IScript;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import java.util.Calendar;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class ItemScript implements IScript
{

    private final Logger log = Logger.getLogger(ItemScript.class);

    @Override
    public int getId()
    {
        return 1003;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        log.info("call scriptId = " + scriptId + "arg = " + arg.toString());
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.PLAYER);
        if (player != null)
        {
            log.info("add gold to player");
            player.getBackpackManager().addResource(
                    ResourceType.GOLD, 10000, true, Reasons.ITEM_USE, Calendar.getInstance().getTime());
        }
        else
        {
            log.info("call script error! cause: not found player.");
        }

        return null;
    }

}
