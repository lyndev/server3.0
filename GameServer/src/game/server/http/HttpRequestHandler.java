/**
 * @date 2014/5/14
 * @author ChenLong
 */

package game.server.http;

import game.core.command.ICommand;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.http.api.HttpRequest;

/**
 * Http处理handler
 * @author ChenLong
 */
public class HttpRequestHandler implements ICommand
{
    private final Logger log = Logger.getLogger(HttpRequestHandler.class);

    private final IoSession session;
    private final HttpRequest request;

    public HttpRequestHandler(IoSession session, HttpRequest request)
    {
        this.session = session;
        this.request = request;
    }

    @Override
    public void action()
    {
        HttpService.getInstance().dispatcherRequest(session, request);
    }   
}
