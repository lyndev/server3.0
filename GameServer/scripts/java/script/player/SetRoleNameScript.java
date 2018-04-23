/**
 * @date 2014/7/24
 * @author ChenLong
 */
package script.player;

import game.server.logic.player.handler.AddLogSetRoleNameCommand;
import game.core.script.IScript;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import game.server.util.UniqueId;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 设置角色名脚本
 *
 * @author ChenLong
 */
public class SetRoleNameScript implements IScript
{
    private final Logger logger = Logger.getLogger(SetRoleNameScript.class);

    @Override
    public int getId()
    {
        return 1014;
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

        addLogSetRoleName(player);

        return null;
    }

    private void addLogSetRoleName(Player player)
    {
        AddLogSetRoleNameCommand command = new AddLogSetRoleNameCommand();
        command.set(player.getFgi(),
                Integer.toString(player.getServerId()),
                UniqueId.toBase36(player.getRoleId()),
                player.getFedId(),
                player.getRoleName(),
                new Date());
        BackLogProcessor.getInstance().addCommand(command);
    }
}
