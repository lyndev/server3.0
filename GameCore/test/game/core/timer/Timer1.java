package game.core.timer;

/**
 *
 * @author Administrator
 */
public class Timer1 extends TimerEvent
{

    public Timer1(long delay)
    {
        super(delay);
    }

    @Override
    public void run()
    {
        System.out.println("我只来一发！");
    }

}
