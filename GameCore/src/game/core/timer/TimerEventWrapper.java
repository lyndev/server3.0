
package game.core.timer;

import java.util.concurrent.ScheduledFuture;

/**
 * 指定执行次数的timer
 * @date   2014-7-24
 * @author pengmian
 */
public class TimerEventWrapper implements Runnable
{
    /**
     *  执行事件
     */
    private final TimerEvent event;
    
    /**
     *  future
     */
    private ScheduledFuture future;
    
    /**
     *  执行次数
     */
    private int count;                              

    public TimerEventWrapper(TimerEvent event, int count)
    {
        this.event = event;
        this.count = count;
        future = null;
    }

    public void start()
    {
        future = SimpleTimerProcessor.getInstance().addEvent(this);
    }

    public void cancel(boolean mayInterruptIfRunning)
    {
        if (future != null)
            future.cancel(mayInterruptIfRunning);
    }

    public TimerEvent getEvent()
    {
        return event;
    }
    

    @Override
    public void run()
    {
        if (count > 0)
        {
            count--;
            event.run();
        }
        else
        {
            cancel(false);
        }
    }
}
