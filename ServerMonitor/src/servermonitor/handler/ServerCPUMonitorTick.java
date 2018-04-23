/**
 * @date 2014/6/23
 * @author ChenLong
 */
package servermonitor.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.timer.TimerEvent;
import java.util.List;
import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import servermonitor.config.ServerConfig;

/**
 *
 * @author ChenLong
 */
public class ServerCPUMonitorTick extends TimerEvent
{
    private final static Logger logger = Logger.getLogger(ServerCPUMonitorTick.class);

    public ServerCPUMonitorTick(long delaySecond)
    {
        super(delaySecond * 1000, delaySecond * 1000, true); // 间隔delaySecond秒
    }

    @Override
    public void run()
    {
        try
        {
            Sigar sigar = new Sigar();
            CpuPerc cpuPerc = sigar.getCpuPerc();

            List<Integer> serverIds = ServerConfig.getInstance().getServerIds();
            for (Integer id : serverIds)
            {
                ServicesFactory.getSingleLogService().addLogServerCPUUsage(
                        Integer.toString(id),
                        Double.toString(cpuPerc.getCombined() * 100.0),
                        Long.toString(System.currentTimeMillis() / 1000));
            }
        }
        catch (SigarException ex)
        {
            logger.error("SigarException", ex);
        }
    }
}
