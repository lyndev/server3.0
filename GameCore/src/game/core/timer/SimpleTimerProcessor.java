package game.core.timer;

import org.apache.log4j.Logger;

/**
 *
 * <b>一个TimerProcessor实现.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class SimpleTimerProcessor extends TimerProcessor
{
    private final static Logger logger = Logger.getLogger(SimpleTimerProcessor.class);

    private SimpleTimerProcessor()
    {
        // 单例对象，提供私有构造器
        super(1);
    }

    @Override
    public void writeError(String message)
    {
        logger.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        logger.error(message, t);
    }

    /**
     * 获取TimerSingleProcessor的实例对象.
     *
     * @return
     */
    public static SimpleTimerProcessor getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;

        SimpleTimerProcessor processor;

        Singleton()
        {
            this.processor = new SimpleTimerProcessor();
        }

        SimpleTimerProcessor getProcessor()
        {
            return processor;
        }
    }
}
