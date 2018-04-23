package game.server.logic.exception;

import game.server.exception.ServerException;

/**
 *
 * <b>游戏逻辑顶层异常类.</b>
 * <p>
 * 所有逻辑层异常都应由该类派生.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class LogicException extends ServerException
{

    public LogicException()
    {
    }

    public LogicException(String message)
    {
        super(message);
    }

    public LogicException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public LogicException(Throwable cause)
    {
        super(cause);
    }

}
