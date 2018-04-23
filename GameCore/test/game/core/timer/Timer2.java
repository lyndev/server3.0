package game.core.timer;

/**
 *
 * @author Administrator
 */
public class Timer2 extends TimerEvent
{

    private int count;
    
    public Timer2(long initialDelay, long delay, boolean loopFixed)
    {
        super(initialDelay, delay, loopFixed);
    }

    @Override
    public void run()
    {
        count++;
        System.out.println("我已来了" + count + "发！");
    }

}
