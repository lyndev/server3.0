/**
 * @date 2014/6/23
 * @author ChenLong
 */
package servermonitor.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.timer.TimerEvent;
import java.util.List;
import org.apache.log4j.Logger;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import servermonitor.config.ServerConfig;

/**
 *
 * @author ChenLong
 */
public class ServerMemMonitorTick extends TimerEvent
{
    private final static Logger logger = Logger.getLogger(ServerMemMonitorTick.class);

    public ServerMemMonitorTick(long delaySecond)
    {
        super(delaySecond * 1000, delaySecond * 1000, true); // 间隔delaySecond秒
    }

    @Override
    public void run()
    {
        try
        {
            Sigar sigar = new Sigar();
            Mem mem = sigar.getMem();
            Swap swap = sigar.getSwap();

            List<Integer> serverIds = ServerConfig.getInstance().getServerIds();
            for (Integer id : serverIds)
            {
                ServicesFactory.getSingleLogService().addLogServerMemoryUsage(
                        Integer.toString(id),
                        Double.toString(mem.getUsedPercent()),
                        Long.toString(System.currentTimeMillis() / 1000));
            }
        }
        catch (SigarException ex)
        {
            logger.error("SigarException", ex);
        }
    }
}
