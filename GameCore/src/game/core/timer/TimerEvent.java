package game.core.timer;

/**
 *
 * <b>计时器事件的抽象基类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class TimerEvent implements Runnable
{

    private long initialDelay; // 首次执行的延迟时间（单位：毫秒）

    private long delay; // 循环执行的间隔时间（单位：毫秒）

    private boolean loopFixed; // 循环执行时是否采用固定频率

    /**
     * 创建一个尚未初始化的计时器事件.
     * <p>
     * 该构造器主要提供给扩展类调用，并配以相关set方法，可以定制更多内容的Timer.
     * <p>
     * 子类和相同包下的类都可以对TimerEvent进行扩展，具体实现可以参考DailyTimer.
     */
    TimerEvent()
    {
    }

    /**
     * 创建一个只执行一次的计时器事件.
     *
     * @param delay 执行的延迟时间（单位：毫秒）
     * @throws IllegalArgumentException 如果delay小于1ms
     */
    public TimerEvent(long delay)
    {
        if (delay > 0)
        {
            this.initialDelay = delay;
        }
        else
        {
            throw new IllegalArgumentException("delay must > 0");
        }
    }

    /**
     * 创建一个循环执行的计时器事件.
     *
     * @param initialDelay 首次执行的延迟时间（单位：毫秒）
     * @param delay 循环执行的间隔时间（单位：毫秒）
     * @param loopFixed 循环执行时是否采用固定频率： <br>
     * true表示不管上一次执行所花费的时间为何，下一次执行必然在initialDelay + 2 * delay后进行；<br>
     * false表示每次执行必然延迟给定的时间(delay)，即下一次执行会等待上一次执行完成后再计算延迟时间.
     * @throws IllegalArgumentException 如果initialDelay或者delay小于1ms
     */
    public TimerEvent(long initialDelay, long delay, boolean loopFixed)
    {
        if (initialDelay > 0 && delay > 0)
        {
            this.initialDelay = initialDelay;
            this.delay = delay;
            this.loopFixed = loopFixed;
        }
        else
        {
            throw new IllegalArgumentException("initialDelay and delay must > 0");
        }
    }

    /**
     * 获取首次执行的延迟时间.
     *
     * @return
     */
    public long getInitialDelay()
    {
        return initialDelay;
    }

    /**
     * 获取循环执行的间隔时间.
     *
     * @return
     */
    public long getDelay()
    {
        return delay;
    }

    /**
     * 循环执行时是否采用固定频率.
     *
     * @return
     */
    public boolean isLoopFixed()
    {
        return loopFixed;
    }

    /**
     * 设置首次执行的延迟时间.
     *
     * @param initialDelay 执行的延迟时间（单位：毫秒）
     */
    void setInitialDelay(long initialDelay)
    {
        this.initialDelay = initialDelay;
    }

    /**
     * 设置循环执行的间隔时间.
     *
     * @param delay 循环执行的间隔时间（单位：毫秒）
     */
    void setDelay(long delay)
    {
        this.delay = delay;
    }

    /**
     * 设置循环执行时是否采用固定频率.
     *
     * @param loopFixed 取值说明：<br>
     * true表示不管上一次执行所花费的时间为何，下一次执行必然在initialDelay + 2 * delay后进行；<br>
     * false表示每次执行必然延迟给定的时间(delay)，即下一次执行会等待上一次执行完成后再计算延迟时间.
     */
    void setLoopFixed(boolean loopFixed)
    {
        this.loopFixed = loopFixed;
    }

}
