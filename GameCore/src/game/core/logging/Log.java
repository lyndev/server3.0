package game.core.logging;

/**
 *
 * <b>日志抽象基类.</b>
 * <p>
 * 记录指定的系统事件或者用户事件的Bean都应继承该抽象类, 并调用父类的构造器指定其Handler.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class Log
{

    private final Class<? extends LogHandler> handler;

    /**
     * 获取日志处理器的类型.
     *
     * @return
     */
    public final Class<? extends LogHandler> getHandler()
    {
        return handler;
    }

    /**
     * 日志基类构造器.
     *
     * @param handler 日志处理器的类型
     */
    protected Log(Class<? extends LogHandler> handler)
    {
        this.handler = handler;
    }

}
