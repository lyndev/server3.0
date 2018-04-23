/**
 * @date 2014/6/23
 * @author ChenLong
 */
package game.server.logic.support.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.core.timer.TimerEvent;
import game.server.config.ServerConfig;
import game.server.logic.player.PlayerManager;
import game.server.thread.BackLogProcessor;
import java.util.Date;

/**
 *
 * @author ChenLong
 */
public class OnLinePlayerMonitorTick extends TimerEvent
{
    public OnLinePlayerMonitorTick(long delaySecond)
    {
        super(delaySecond * 1000, delaySecond * 1000, true); // 间隔delaySecond秒
    }

    @Override
    public void run()
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String serverId;
            private String concurrent;
            private Date date;

            public ICommand set(String serverId, String concurrent, Date date)
            {
                this.serverId = serverId;
                this.concurrent = concurrent;
                this.date = date;
                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogServerOnline(
                        serverId,
                        concurrent,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(Integer.toString(ServerConfig.getInstance().getServerId()),
                Integer.toString(PlayerManager.onLinePlayerNum()),
                new Date()));
    }
}
