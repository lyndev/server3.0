package game.core.timer;

import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Administrator
 */
public class TimerTest
{

    public static void main(String[] args)
    {
        Timer1 t1 = new Timer1(3000);
        Timer2 t2 = new Timer2(1000, 16, true);
        SimpleTimerProcessor.getInstance().addEvent(t1);
        ScheduledFuture f2 = SimpleTimerProcessor.getInstance().addEvent(t2);

        try
        {
            Thread.sleep(1000 * 10);
            // true表示取消任务的同时中断执行此任务的线程
            // false表示允许正在运行的任务运行完成
            System.out.println("cancel ? " + f2.cancel(false));
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        } finally {
            SimpleTimerProcessor.getInstance().stop(false);
        }
    }

}
