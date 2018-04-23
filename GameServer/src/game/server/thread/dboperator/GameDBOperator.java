/**
 * @date 2014/5/17
 * @author ChenLong
 */
package game.server.thread.dboperator;

import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.thread.dboperator.handler.DBOperatorHandler;
import game.server.thread.dboperator.handler.ReqUpdateRobotHandler;
import game.server.thread.dboperator.handler.ReqUpdateRoleHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * gameDB操作线程, 执行: 1. INSERT 2. UPDATE, 带有合并功能 3. SELECT
 *
 * @author ChenLong
 */
public class GameDBOperator
{
    private final static int EXEC_NUM_PER_LOOK = 20; // 每次循环执行的请求个数, 防止大量相同请求未合并造成请求队列暴涨
    private final Logger log = Logger.getLogger(GameDBOperator.class);
    private final BlockingQueue<DBOperatorHandler> submitQueue = new LinkedBlockingQueue<>(); // 提交队列
    private final Queue<DBOperatorHandler> workQueue = new LinkedList<>();
    private volatile boolean onoff = false; // 启动(true) - 停止(false)
    private volatile boolean queueIsEmpty = false; // 停止 后workQueue是否为空(在调用stop()后)
    private Thread thread = null;

    public static GameDBOperator getInstance()
    {
        return GameDBOperator.Singleton.INSTANCE.getOperator();
    }

    public void start()
    {
        if (onoff)
        {
            log.warn("GameDBOperator has started");
            return;
        }
        log.info("GameDBOperator starting");
        onoff = true;
        thread = new Thread(new Worker());
        thread.setName(GameDBOperator.class.getSimpleName());
        thread.start();
    }

    public void stop()
    {
        onoff = false;
    }

    public void awaitStop()
    {
        if (onoff)
        {
            log.error("has not call stop() method");
            return;
        }
        while (!queueIsEmpty) // 等待队列为空
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException ex)
            {
                log.warn("InterruptedException", ex);
            }
        }
    }

    /**
     * 停止并等待停止
     */
    public void stopAndAwaitStop()
    {
        saveAllRobots();
        stop();
        awaitStop();
    }
    
    /**
     * 停服时回存机器人数据.
     */
    public void saveAllRobots()
    {
        log.info("回存机器人数据...");
        List<Player> robots = PlayerManager.getRobots();
        if (robots != null && !robots.isEmpty())
        {
            log.info("回存的机器人总数：" + robots.size());
            for (Player robot : robots)
            {
                submitRequest(new ReqUpdateRobotHandler(robot.toRoleBean().compress()));
            }
        }
    }

    /**
     * 提交操作请求
     *
     * @param handler
     */
    public void submitRequest(DBOperatorHandler handler)
    {
        Validate.notNull(handler);
        if (!onoff)
            throw new RejectedExecutionException("GameDBOperator has not turn on");
        try
        {
            submitQueue.put(handler);
            if (submitQueue.size() > 50)
                log.warn("***************************** submitQueue.size() = " + submitQueue.size());
        }
        catch (InterruptedException ex)
        {
            log.error("InterruptedException, should not this exception, check code !", ex); // 无界的LinkedBlockingQueue不应该会抛出这个异常
        }
    }

    /**
     * 处理请求, 处理: 1. 合并请求 2. 执行请求
     *
     * @throws InterruptedException
     */
    private void processRequest() throws InterruptedException
    {
        while (onoff || !submitQueue.isEmpty() || !workQueue.isEmpty())
        {
            try
            {
                DBOperatorHandler handler = null;
                if (workQueue.isEmpty())
                    handler = submitQueue.poll(100, TimeUnit.MILLISECONDS); // workQueue队列为空时, submitQueue.poll执行阻塞poll, 防止两队列都空时的CPU busy loop
                else
                    handler = submitQueue.poll();

                if (handler != null)
                {
                    combineRequest(handler);
                }
                else
                {
                    execRequest(EXEC_NUM_PER_LOOK);
                }
            }
            catch (Throwable t) // 此处hold住所有Exception, 防止因异常让GameDBOperator线程退出
            {
                log.error("processRequest loop Exception", t);
            }
        }
        queueIsEmpty = true;
    }

    /**
     * 合并同一个玩家的update操作 etc.
     *
     * @param handler
     */
    private void combineRequest(DBOperatorHandler handler)
    {
        if (handler instanceof ReqUpdateRoleHandler)
        {
            ReqUpdateRoleHandler newReqHandler = (ReqUpdateRoleHandler) handler;
            for (DBOperatorHandler dbHandler : workQueue) // 合并workQueue内已有的Update请求
            {
                if (dbHandler instanceof ReqUpdateRoleHandler)
                {
                    ReqUpdateRoleHandler reqHandler = (ReqUpdateRoleHandler) dbHandler;
                    if (reqHandler.canCombine(newReqHandler))
                    {
                        reqHandler.combine(newReqHandler);
                        return;
                    }
                }
            }
        }

        workQueue.add(handler);
    }

    /**
     * 执行请求
     *
     * @param maxNum 最多执行个数
     */
    private void execRequest(int maxNum)
    {
        if (workQueue.size() > 50)
            log.warn("############################# submitQueue.size() = " + workQueue.size());
        int num = (maxNum > workQueue.size()) ? workQueue.size() : maxNum;
        for (int i = 0; i < num; ++i)
        {
            DBOperatorHandler handler = workQueue.poll();
            handler.action();
        }
    }

    private class Worker implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                processRequest();
            }
            catch (Exception ex)
            {
                log.error("Exception", ex);
            }
        }
    }

    private enum Singleton
    {
        INSTANCE;
        GameDBOperator operator;

        Singleton()
        {
            this.operator = new GameDBOperator();
        }

        GameDBOperator getOperator()
        {
            return operator;
        }
    }
}
