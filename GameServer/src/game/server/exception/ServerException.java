package game.server.exception;

/**
 *
 * <b>服务端顶层异常类.</b>
 * <p>
 * 所有服务端自定义异常都从该类派生.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ServerException extends RuntimeException
{
    public ServerException()
    {
    }

    public ServerException(String message)
    {
        super(message);
    }

    public ServerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ServerException(Throwable cause)
    {
        super(cause);
    }
}
