/**
 * @date 2014/4/17 11:45
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
public class ReqCreateRoleHandler extends Handler
{
    @Override
    public void action()
    {
        IoSession session = getMessage().getSession();
        LoginMessage.ReqCreateRole reqMsg = (LoginMessage.ReqCreateRole) getMessage().getData();
        LoginService.getInstance().createRole(session, reqMsg);
    }
}
