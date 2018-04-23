package game.core.timer;

import game.core.command.CommandThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * <b>计时器事件的线程基类.</b>
 * <p>
 * 负责调度并执行每个TimerEvent.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class TimerProcessor
{
    private final ScheduledExecutorService executor;

    /**
     * 计时器事件的线程基类.
     */
    public TimerProcessor()
    {
        this(1);
    }

    /**
     * 计时器事件的线程基类.
     *
     * @param corePoolSize 核心线程数
     */
    public TimerProcessor(int corePoolSize)
    {
        if (corePoolSize > 1)
            executor = Executors.newScheduledThreadPool(corePoolSize, new CommandThreadFactory(TimerProcessor.class.getSimpleName()));
        else
            executor = Executors.newScheduledThreadPool(1, new CommandThreadFactory(TimerProcessor.class.getSimpleName()));
    }

    public void stop(boolean immediately)
    {
        if (immediately)
            executor.shutdownNow();
        else
            executor.shutdown();
    }

    /**
     * 等待所有任务完成
     */
    public void awaitStop()
    {
        boolean loop = true;
        do
        {
            try
            {
                loop = !executor.awaitTermination(200, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ex)
            {
                writeError("InterruptedException", ex);
            }
        } while (loop);
    }

    public void stopAndAwaitStop(boolean immediately)
    {
        stop(immediately);
        awaitStop();
    }

    /**
     * 添加一个计时器事件.
     *
     * @param event 计时器事件
     * @return 可用于提取结果或取消的Future
     */
    public final ScheduledFuture addEvent(TimerEvent event)
    {
        if (event.getDelay() == 0)
        {
            return executor.schedule(
                    event, event.getInitialDelay(), TimeUnit.MILLISECONDS);
        }
        else if (event.isLoopFixed())
        {
            return executor.scheduleAtFixedRate(
                    event, event.getInitialDelay(), event.getDelay(), TimeUnit.MILLISECONDS);
        }
        else
        {
            return executor.scheduleWithFixedDelay(
                    event, event.getInitialDelay(), event.getDelay(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 添加一个可取消的计时器事件
     *
     * @param timerEventWrapper
     * @return
     */
    public final ScheduledFuture addEvent(TimerEventWrapper timerEventWrapper)
    {
        if (timerEventWrapper.getEvent().getDelay() == 0)
        {
            return executor.schedule(
                    timerEventWrapper, timerEventWrapper.getEvent().getInitialDelay(), TimeUnit.MILLISECONDS);
        }
        else if (timerEventWrapper.getEvent().isLoopFixed())
        {
            return executor.scheduleAtFixedRate(
                    timerEventWrapper, timerEventWrapper.getEvent().getInitialDelay(), timerEventWrapper.getEvent().getDelay(), TimeUnit.MILLISECONDS);
        }
        else
        {
            return executor.scheduleWithFixedDelay(
                    timerEventWrapper, timerEventWrapper.getEvent().getInitialDelay(), timerEventWrapper.getEvent().getDelay(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     */
    public abstract void writeError(String message);

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     * @param t 产生错误的异常类
     */
    public abstract void writeError(String message, Throwable t);
}
