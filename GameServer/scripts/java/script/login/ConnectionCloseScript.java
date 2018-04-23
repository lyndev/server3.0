/**
 * @date 2014/7/23
 * @author ChenLong
 */
package script.login;

import game.server.logic.login.handler.AddLogRoleLogoutCommand;
import game.core.script.IScript;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import game.server.util.UniqueId;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * 游戏客户端连接关闭脚本
 *
 * @author ChenLong
 */
public class ConnectionCloseScript implements IScript
{
    private final Logger logger = Logger.getLogger(ConnectionCloseScript.class);

    @Override
    public int getId()
    {
        return 1012;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.ARG1);

        logger.info("call java script " + scriptId);

        if (player == null)
        {
            logger.error("player == null");
            return null;
        }

        addLogRoleLogout(player);

        return null;
    }

    public void addLogRoleLogout(Player player)
    {
        AddLogRoleLogoutCommand command = new AddLogRoleLogoutCommand();
        command.set(player.getFgi(),
                Integer.toString(player.getServerId()),
                UniqueId.toBase36(player.getRoleId()),
                player.getFedId(),
                (System.currentTimeMillis() - player.getLastConnectTime()) / 1000,
                new Date());

        BackLogProcessor.getInstance().addCommand(command);
    }
}
