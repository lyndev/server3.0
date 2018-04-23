package game.core.timer;

import game.core.command.ICommand;
import org.apache.commons.lang.time.DateUtils;

/**
 * 初始延迟delay, 连续执行days天的定时器
 *
 * @author ZouZhaopeng
 */
public class DaysTimer extends TimerEvent
{
    private int days; //连续执行的天数
    private final ICommand cmd; //执行的任务

    public DaysTimer(long delay, int days, ICommand cmd)
    {
        super(delay); //创建的是只执行一次的定时器
        this.days = days;
        this.cmd = cmd;
    }

    @Override
    public void run()
    {
        if (--days > 0) //如果执行天数还没达到指定值, 在执行之前添加下一个定时器
        {
            SimpleTimerProcessor.getInstance().addEvent(new DaysTimer(DateUtils.MILLIS_PER_DAY, days, cmd));
        }
        cmd.action();
    }
}
