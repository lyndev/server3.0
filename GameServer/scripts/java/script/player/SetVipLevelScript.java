/**
 * @date 2014/7/24
 * @author ChenLong
 */
package script.player;

import game.server.logic.player.handler.AddLogSetVipLevelCommand;
import game.core.script.IScript;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import game.server.util.UniqueId;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * VIP等级改变脚本
 *
 * @author ChenLong
 */
public class SetVipLevelScript implements IScript
{
    private final Logger logger = Logger.getLogger(SetVipLevelScript.class);

    @Override
    public int getId()
    {
        return 1015;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.ARG1);
        IoSession session = (IoSession) args.get(ScriptArgs.Key.ARG2);

        logger.info("call java script " + scriptId);

        if (player == null)
        {
            logger.error("player == null");
            return null;
        }

        addLogSetVipLevel(player);

        return null;
    }

    private void addLogSetVipLevel(Player player)
    {
        AddLogSetVipLevelCommand command = new AddLogSetVipLevelCommand();
        command.set(player.getFgi(),
                Integer.toString(player.getServerId()),
                UniqueId.toBase36(player.getRoleId()),
                player.getFedId(),
                Integer.toString(player.getVipManager().getVipLevel()),
                new Date());
        BackLogProcessor.getInstance().addCommand(command);
    }
}
