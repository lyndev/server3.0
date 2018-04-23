/**
 * @date 2014/5/29
 * @author ChenLong
 */
package game.server.thread;

import game.core.command.CommandProcessor;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class HttpProcessor extends CommandProcessor
{
    private final Logger log = Logger.getLogger(HttpProcessor.class);
    private static final HttpProcessor instance;

    static
    {
        instance = new HttpProcessor();
    }

    public static HttpProcessor getInstance()
    {
        return instance;
    }

    private HttpProcessor()
    {
        super(HttpProcessor.class.getSimpleName());
    }

    @Override
    public void writeError(String message)
    {
        log.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        log.error(message, t);
    }
}
