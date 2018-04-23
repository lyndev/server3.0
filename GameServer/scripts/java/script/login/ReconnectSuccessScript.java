/**
 * @date 2014/7/23
 * @author ChenLong
 */
package script.login;

import game.server.logic.login.handler.AddLogRoleLoginCommand;
import game.core.script.IScript;
import game.server.logic.login.handler.AddLogUserLoginCommand;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import game.server.util.SessionUtils;
import game.server.util.UniqueId;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 重连接成功脚本
 *
 * @author ChenLong
 */
public class ReconnectSuccessScript implements IScript
{
    private final Logger logger = Logger.getLogger(ReconnectSuccessScript.class);

    @Override
    public int getId()
    {
        return 1011;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
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

        addLogRoleLogin(player);

        return null;
    }

    /**
     * 记录日志
     *
     * @param player
     */
    private void addLogRoleLogin(Player player)
    {
        {
            AddLogRoleLoginCommand command = new AddLogRoleLoginCommand();
            command.set(player.getFgi(),
                    Integer.toString(player.getServerId()),
                    UniqueId.toBase36(player.getRoleId()),
                    player.getFedId(),
                    player.getRoleName(),
                    Integer.toString(player.getRoleLevel()),
                    "0",
                    "0",
                    "0",
                    new Date());
            BackLogProcessor.getInstance().addCommand(command);
        }
        { // 平台需要在重连时给他们发送 用户登录 日志(注: 另一处是在Agent中发送)
            String ip = SessionUtils.getPeerIp(player.getSession());

            AddLogUserLoginCommand command = new AddLogUserLoginCommand();
            command.set(player.getClient(), player.getFgi(), Integer.toString(player.getPlatformId()), player.getFedId(),
                    player.getUserName(), player.getPlatform_uid(), "", "",
                    player.getClientVer(), player.getClientVer(), player.getDevice(), ip,
                    new Date());
            BackLogProcessor.getInstance().addCommand(command);
        }
    }
}
