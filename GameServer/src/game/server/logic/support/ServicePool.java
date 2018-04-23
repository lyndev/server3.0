/**
 * @date 2014/4/17 16:00
 * @author ChenLong
 */
package game.server.logic.support;

import game.core.command.ICommand;
import game.core.timer.DailyTimer;
import game.core.timer.SimpleTimerProcessor;
import game.server.logic.line.GameLineManager;
import game.server.logic.login.service.LoginService;
import game.server.logic.support.handler.OnLinePlayerMonitorTick;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.Req21RankTickHandler;
import game.server.util.MessageUtils;
import game.server.world.friend.handler.ResetFriendManagerHandler;
import org.apache.log4j.Logger;

/**
 * Service容器 用于执行各Service初始化等操作
 *
 * @author ChenLong
 */
public class ServicePool
{
    private final static Logger logger = Logger.getLogger(ServicePool.class);

    private final LoginService loginService = LoginService.getInstance();

    /**
     * 初始化所有Service
     */
    public void init()
    {
        DBView.getInstance().load();
        initializeServerMonitorTick();
        initializeTick5();
        initialize21RankTick();
    }

    /**
     * 初始化21点刷新排行榜定时器, 包括成就排行榜/趣味关卡1排行榜/趣味关卡2排行榜
     */
    private void initialize21RankTick()
    {
        SimpleTimerProcessor.getInstance().addEvent(new DailyTimer("21RankTick", 21, new ICommand()
        {
            @Override
            public void action()
            {
                GameDBOperator.getInstance().submitRequest(new Req21RankTickHandler());
            }
        }).getTask());
    }

    /**
     * 初始化凌晨5点定时器
     */
    private void initializeTick5()
    {
        SimpleTimerProcessor.getInstance().addEvent(new DailyTimer("Tick5", 5, new ICommand()
        {
            @Override
            public void action()
            {
                // 好友系统重置
                try
                {
                    ResetFriendManagerHandler handler = new ResetFriendManagerHandler();
                    MessageUtils.sendToGameWorld(handler);
                }
                catch (Throwable t)
                {
                    logger.error("Throwable", t);
                }

                try
                {
                    // 各个GameLine tick5
                    GameLineManager.getInstance().tick5Command();
                }
                catch (Throwable r)
                {
                    logger.error("Throwable", r);
                }

                // 5点发放月卡福利
                DBView.getInstance().issueMonthCardAward();
            }
        }).getTask());
    }

    /**
     * 初始化服务器监控定时器
     */
    private void initializeServerMonitorTick()
    {
        int second = 5 * 60; // 后台5分钟监控一次
        SimpleTimerProcessor.getInstance().addEvent(new OnLinePlayerMonitorTick(second));
    }

    /**
     * 获取实例对象.
     *
     * @return
     */
    public static ServicePool getInstance()
    {
        return Singleton.INSTANCE.getService();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;
        ServicePool service;

        Singleton()
        {
            this.service = new ServicePool();
        }

        ServicePool getService()
        {
            return service;
        }
    }
}
