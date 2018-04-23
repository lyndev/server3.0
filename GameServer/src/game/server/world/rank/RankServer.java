package game.server.world.rank;

import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.core.timer.DailyTimer;
import game.core.timer.SimpleTimerProcessor;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.dao.UserDao;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.service.MailService;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.util.UniqueId;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * <b>排名服务器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class RankServer extends CommandProcessor
{

    private final Logger log = Logger.getLogger(RankServer.class);
    private final RankManager rankManager = new RankManager();
    private volatile boolean isInit = false;

    public synchronized void start()
    {
        if (!isInit)
        {
            // 每日21点发放竞技场奖励
            SimpleTimerProcessor.getInstance().addEvent(
                    new DailyTimer("arenaRankTimer", 21, new ICommand()  
            {
                
                private final Logger log = Logger.getLogger("ArenaRankTimerLogger");
                
                @Override
                public void action()
                {
                    RankServer.this.addCommand(new ICommand()
                    {
                        @Override
                        public void action()
                        {
                        }
                    });
                }
            }).getTask());
            
            // 每日21点拷贝竞技场列表
            SimpleTimerProcessor.getInstance().addEvent(
                    new DailyTimer("arenaCopyTimer", 21, new ICommand()
            {
                
                private final Logger log = Logger.getLogger("ArenaCopyTimerLogger");
                
                @Override
                public void action()
                {
                    RankServer.this.addCommand(new ICommand()
                    {
                        @Override
                        public void action()
                        {
                            log.info("开始拷贝竞技场数据...");
                            //TODO:
                            log.info("拷贝竞技场数据完毕.");
                        }
                    });
                }
            }).getTask());
        
            isInit = true;
        }
    }

    private RankServer()
    {
        super("RankServerThread");
    }

    public RankManager getRankManager()
    {
        return rankManager;
    }

    @Override
    public void writeError(String message)
    {
        log.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        log.error(message, t);
    }

    public static RankServer getInstance()
    {
        return RankServer.Singleton.INSTANCE.getManager();
    }

    private enum Singleton
    {
        INSTANCE;

        RankServer manager;

        Singleton()
        {
            this.manager = new RankServer();
        }

        RankServer getManager()
        {
            return manager;
        }
    }

}
