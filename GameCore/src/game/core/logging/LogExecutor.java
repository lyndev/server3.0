package game.core.logging;

import org.apache.log4j.Logger;

/**
 *
 * <b>日志执行器.</b>
 * <p>
 * addTask方法提供了将一个日志作为任务提交给LogProcessor的入口.<br>
 * 当然，你也可以直接使用LogProcessor，但对于应用层来讲，LogExecutor让代码显得更简单一些.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class LogExecutor
{

    static final Logger LOG = Logger.getLogger(LogExecutor.class);

    /**
     * 添加一个待处理的日志任务.
     *
     * @param log 待处理的日志
     */
    public static void addTask(Log log)
    {
        if (log.getHandler() != null)
        {
            try
            {
                LogHandler handler = log.getHandler().newInstance();
                handler.setLog(log);
                LogProcessor.getInstance().addCommand(handler);
            }
            catch (InstantiationException | IllegalAccessException ex)
            {
                LOG.error("LogHandler instantiation failed! cause:", ex);
            }
        }
        else
        {
            LOG.error("Not found logHandler! log class: " + log.getClass().getName());
        }
    }

}
