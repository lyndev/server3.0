package game.core.timer;

import game.core.command.ICommand;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;

/**
 *
 * <b>每日定时器.</b>
 * <p>
 * 将创建后的DailyTimer添加到线程池（如TimerProcessor），DailyTimer会在每天的指定时刻执行一次指定的Command.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class DailyTimer
{

    private final String name;

    private TimerEvent task;

    private static final Logger LOG = Logger.getLogger(DailyTimer.class);

    /**
     * 创建一个在每日任意时刻执行的定时任务.
     *
     * @param name 定时器的名称
     * @param daily 指定的任意时刻
     * @param command 定时执行的命令
     */
    public DailyTimer(String name, Calendar daily, ICommand command)
    {
        this.name = name;
        initialize(daily, command);
    }

    /**
     * 创建一个在每日整点执行的定时任务.
     *
     * @param name 定时器的名称
     * @param hourOfDay 指定小时数（24小时制：0~23）
     * @param command 定时执行的命令
     * @throws IllegalArgumentException 当hourOfDay取值不在有效范围内时
     */
    public DailyTimer(String name, int hourOfDay, ICommand command)
    {
        if (hourOfDay < 0 || hourOfDay > 23)
        {
            throw new IllegalArgumentException("hourOfDay的有效取值范围：0 ~ 23！hourOfDay = " + hourOfDay);
        }

        this.name = name;
        Calendar daily = Calendar.getInstance();
        daily.set(Calendar.HOUR_OF_DAY, hourOfDay);
        daily.set(Calendar.MINUTE, 0);
        daily.set(Calendar.SECOND, 0);
        daily.set(Calendar.MILLISECOND, 0);
        initialize(daily, command);
    }

    /**
     * 创建一个在每日指定时刻执行的定时任务（精确到分钟）.
     *
     * @param name 定时器的名称
     * @param hourOfDay 指定小时数（24小时制：0~23）
     * @param minute 指定分钟数（有效范围：0~59）
     * @param command 定时执行的命令
     * @throws IllegalArgumentException 当hourOfDay或minute取值不在有效范围内时
     */
    public DailyTimer(String name, int hourOfDay, int minute, ICommand command)
    {
        if (hourOfDay < 0 || hourOfDay > 23)
        {
            throw new IllegalArgumentException("hourOfDay的有效取值范围：0 ~ 23！hourOfDay = " + hourOfDay);
        }

        if (minute < 0 || minute > 59)
        {
            throw new IllegalArgumentException("minute的有效取值范围：0 ~ 59！minute = " + minute);
        }

        this.name = name;
        Calendar daily = Calendar.getInstance();
        daily.set(Calendar.HOUR_OF_DAY, hourOfDay);
        daily.set(Calendar.MINUTE, minute);
        daily.set(Calendar.SECOND, 0);
        daily.set(Calendar.MILLISECOND, 0);
        initialize(daily, command);
    }

    /**
     * 获取定时任务.
     *
     * @return
     */
    public TimerEvent getTask()
    {
        return task;
    }

    private void initialize(Calendar daily, ICommand command)
    {
        createTask(command);
        long nowTime = Calendar.getInstance().getTimeInMillis();

        if (nowTime < daily.getTimeInMillis())
        {
            task.setInitialDelay(daily.getTimeInMillis() - nowTime);
        }
        else if (nowTime > daily.getTimeInMillis())
        {
            task.setInitialDelay(daily.getTimeInMillis() + 1000 * 60 * 60 * 24 - nowTime);
        }
        else
        {
            task.setInitialDelay(1);
        }

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(daily.getTime());
        tomorrow.add(Calendar.DATE, 1);

        task.setDelay(tomorrow.getTimeInMillis() - daily.getTimeInMillis());
        task.setLoopFixed(true);

        if (LOG.isInfoEnabled())
        {
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            LOG.info("初始化 '" + name + "'，自定义时间 = " 
                    + sdf1.format(daily.getTime()) + "，当前时间 = " + sdf2.format(cal.getTime()));
            cal.setTimeInMillis(nowTime + task.getInitialDelay());
            LOG.info(name + "将在 " + sdf2.format(cal.getTime()) + " 首次执行任务.");
            cal.setTimeInMillis(cal.getTimeInMillis() + task.getDelay());
            LOG.info("此后，'" + name + "' 将在每天的" + sdf1.format(cal.getTime()) + "重复执行任务");
        }
    }

    private void createTask(ICommand command)
    {
        task = new TimerEvent()
        {
            private ICommand command;

            TimerEvent setCommand(ICommand command)
            {
                this.command = command;
                return this;
            }

            @Override
            public void run()
            {
                try
                {
                    LOG.info(name + " 开始执行任务...");
                    command.action();
                    LOG.info(name + " 执行完毕.");
                }
                catch (Throwable t)
                {
                    LOG.error(name + " 执行任务失败！cause：", t);
                }
            }
        }.setCommand(command);
    }

}
