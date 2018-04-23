package game.server.logic.login.handler;

import game.core.command.Handler;
import game.message.LoginMessage.ReqLogin;
import game.server.logic.login.service.LoginService;
import org.apache.mina.core.session.IoSession;

public class ReqLoginHandler extends Handler
{
    @Override
    public void action()
    {
        IoSession session = getMessage().getSession();
        ReqLogin reqMsg = (ReqLogin) getMessage().getData();
        LoginService.getInstance().login(session, reqMsg, false);
    }
}
