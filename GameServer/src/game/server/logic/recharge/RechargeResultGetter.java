/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge;

import com.haowan.logger.service.ServicesFactory;
import org.apache.log4j.Logger;

/**
 * 获取充值查询结果
 *
 * @author ChenLong
 */
public class RechargeResultGetter
{
    private static final RechargeResultGetter instance;
    private static final Logger logger = Logger.getLogger(RechargeResultGetter.class);

    private volatile boolean onoff = false; // on(true) - off(false)
    private Thread thread = null;

    static
    {
        instance = new RechargeResultGetter();
    }

    private RechargeResultGetter()
    {
    }

    public static RechargeResultGetter getInstance()
    {
        return instance;
    }

    public void start()
    {
        if (!onoff)
        {
            logger.info("RechargeResultGetter starting");
            onoff = true;
            thread = new Thread(new Worker());
            thread.setName("RechargeResultGetter");
            thread.start();
        }
        else
        {
            logger.warn("RechargeResultGetter has started");
        }
    }

    public void stop()
    {
        onoff = false;
        thread.interrupt();
    }

    public void awaitStop()
    {
        if (!onoff)
        {
            while (thread.isAlive()) // 等待线程终止
            {
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException ex)
                {
                    logger.warn("InterruptedException", ex);
                }
            }
        }
        else
        {
            logger.error("has not call stop() method");
        }
    }

    /**
     * 停止并等待停止
     */
    public void stopAndAwaitStop()
    {
        stop();
        awaitStop();
    }

    private void processRequest() throws InterruptedException
    {
        while (onoff)
        {
            try
            {
                String result = ServicesFactory.getSingleVerifyService().getPayResult();
                if (result != null && !result.trim().isEmpty())
                {
                    logger.info("getPayResult: " + result);
                    RechargeService.getInstance().addVerifyOrderResultCommand(result);
                }
                else
                {
                    logger.error("getPayResult return empty, result: [" + result + "]");
                }
            }
            catch (Throwable t)
            {
                logger.error("RechargeResultGetter processRequest loop Exception", t);
            }
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
            catch (Throwable t)
            {
                logger.error("Throwable", t);
            }
        }
    }
}
