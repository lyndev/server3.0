package game.core.logging;

import game.core.command.CommandProcessor;
import org.apache.log4j.Logger;

/**
 *
 * <b>日志线程.</b>
 * <p>
 * 负责调度并执行每个LogHandler.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class LogProcessor extends CommandProcessor
{

    final Logger log = Logger.getLogger(LogProcessor.class);

    private LogProcessor()
    {
        super(LogProcessor.class.getSimpleName());
        // 单例对象，提供私有构造器
    }

    /**
     * 获取LogProcessor的实例对象.
     *
     * @return
     */
    public static LogProcessor getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
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

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {

        INSTANCE;

        LogProcessor processor;

        Singleton()
        {
            this.processor = new LogProcessor();
        }

        LogProcessor getProcessor()
        {
            return processor;
        }

    }

}
