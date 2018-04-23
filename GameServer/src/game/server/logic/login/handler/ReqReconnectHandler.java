/**
 * @date 2014/4/18 14:20
 * @author ChenLong
 */
package game.server.logic.login.handler;

import game.core.command.Handler;
import game.message.LoginMessage;
import game.server.logic.login.service.LoginService;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class ReqReconnectHandler extends Handler
{
    @Override
    public void action()
    {
        IoSession session = getMessage().getSession();
        LoginMessage.ReqReconnect reqMsg = (LoginMessage.ReqReconnect) getMessage().getData();
        LoginService.getInstance().reconnect(session, reqMsg);
    }
}
