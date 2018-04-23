/**
 * @date 2014/5/12
 * @author ChenLong
 */
package script.login;

import game.server.logic.login.handler.AddLogRoleLoginCommand;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import game.server.util.UniqueId;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class LoginSuccessScript implements IScript
{
    private final Logger logger = Logger.getLogger(LoginSuccessScript.class);

    @Override
    public int getId()
    {
        return 1001;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        /*
        ScriptArgs args = (ScriptArgs) arg;
        IoSession session = (IoSession) args.get(ScriptArgs.Key.IO_SESSION);
        Player player = (Player) args.get(ScriptArgs.Key.PLAYER);
        String functionName = (String) args.get(ScriptArgs.Key.FUNCTION_NAME);

        logger.info("call java script " + scriptId);

        if (player == null)
        {
            logger.error("player == null");
            return null;
        }

        if (!player.getScriptFlag().contains(1024))
        {
//            ScriptManager.getInstance().call(1024, arg);
            player.getScriptFlag().add(1024);
        }
        ScriptManager.getInstance().call(1029, arg);
        addLogRoleLogin(player);
*/
        return null;
    }

    /**
     * 记录日志
     *
     * @param player
     */
    private void addLogRoleLogin(Player player)
    {
        AddLogRoleLoginCommand command = new AddLogRoleLoginCommand();
        command.set(player.getFgi(),
                Integer.toString(player.getServerId()),
                UniqueId.toBase36(player.getRoleId()),
                player.getFedId(),
                player.getRoleName(),
                Integer.toString(player.getRoleLevel()),
                "",
                "",
                "",
                new Date());
        BackLogProcessor.getInstance().addCommand(command);
    }
}
