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
 * 开始登录脚本
 *
 * @author ChenLong
 */
public class LoginBeginScript implements IScript
{
    private final Logger log = Logger.getLogger(LoginBeginScript.class);

    @Override
    public int getId()
    {
        return 1006;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        IoSession session = (IoSession) args.get(ScriptArgs.Key.IO_SESSION);
        LoginMessage.ReqLogin reqLogin = (LoginMessage.ReqLogin) args.get(ScriptArgs.Key.OBJECT_VALUE);
        boolean verify = (Boolean) args.get(ScriptArgs.Key.BOOLEAN_VALUE);

        log.info("call LoginBeginScript script " + scriptId);

        return null;
    }
}
