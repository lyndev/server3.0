/**
 * @date 2014/4/17 14:45
 * @author ChenLong
 */
package game.server.logic.login.handler;

import game.core.command.Handler;
import game.server.logic.constant.HandlerKey;
import game.server.logic.login.service.LoginService;
import org.apache.mina.core.session.IoSession;

/**
 * 客户端连接断开handler
 *
 * @author ChenLong
 */
public class SessionCloseHandler extends Handler
{
    @Override
    public void action()
    {
        IoSession session = (IoSession) getAttribute(HandlerKey.SESSION.name());
        LoginService.getInstance().connectionClosed(session);
    }
}
