/**
 * @date 2014/7/17
 * @author ChenLong
 */
package script.login;

import game.core.script.IScript;
import game.message.LoginMessage;
import game.server.logic.util.ScriptArgs;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 开始创建角色
 *
 * @author ChenLong
 */
public class CreateRoleBeginScript implements IScript
{
    private final Logger log = Logger.getLogger(CreateRoleBeginScript.class);

    @Override
    public int getId()
    {
        return 1007;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        IoSession session = (IoSession) args.get(ScriptArgs.Key.IO_SESSION);
        LoginMessage.ReqCreateRole reqLogin = (LoginMessage.ReqCreateRole) args.get(ScriptArgs.Key.OBJECT_VALUE);

        log.info("call CreateRoleBeginScript script " + scriptId);

        return null;
    }
}
