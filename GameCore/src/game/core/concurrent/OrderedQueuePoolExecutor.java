/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class OrderedQueuePoolExecutor extends ThreadPoolExecutor
{
    public final static int DEFAULT_MAX_QUEUE_SIZE = 100000; // 10万
    protected static final Logger log = Logger.getLogger(OrderedQueuePoolExecutor.class);
    private final OrderedQueuePool<Integer, AbstractWork> pool = new OrderedQueuePool<>();
    private String name;
    private int corePoolSize;
    private int maxQueueSize;

    public OrderedQueuePoolExecutor(String name, int corePoolSize, int maxQueueSize)
    {
        super(corePoolSize, 2 * corePoolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maxQueueSize = maxQueueSize;
    }

    public OrderedQueuePoolExecutor(int corePoolSize)
    {
        this("queue-pool", corePoolSize, DEFAULT_MAX_QUEUE_SIZE);
    }

    /**
     * 增加执行任务
     *
     * @param key
     * @param work
     * @return
     */
    public boolean addTask(int key, AbstractWork work)
    {
        key = key % corePoolSize;
        CommandQueue<AbstractWork> queue = pool.getCommandQueue(key);
        boolean run = false;
        boolean result = false;
        synchronized (queue)
        {
            if (maxQueueSize > 0 && queue.size() > maxQueueSize)
            {
                log.error("队列" + name + ", size = " + queue.size() + ", (" + key + ")" + "抛弃指令!");
                queue.clear();
            }
            result = queue.add(work);
            if (result)
            {
                work.setCommandQueue(queue);
                {
                    if (queue.isProcessingCompleted())
                    {
                        queue.setProcessingCompleted(false);
                        run = true;
                    }
                }
            }
            else
            {
                log.error("队列添加任务失败");
            }
        }
        if (run)
        {
            execute(queue.poll());
        }
        return result;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r, t);

        AbstractWork work = (AbstractWork) r;
        CommandQueue<AbstractWork> queue = work.getCommandQueue();
        if (queue != null)
        {
            AbstractWork afterWork = null;
            synchronized (queue)
            {
                afterWork = queue.poll();
                if (afterWork == null)
                {
                    queue.setProcessingCompleted(true);
                }
            }
            if (afterWork != null)
            {
                execute(afterWork);
            }
        }
        else
        {
            log.error("执行队列为空");
        }
    }

    public void stopAndAwait()
    {
        try
        {
            // 等待将pool中的任务全部提交到executor
            while (pool.getPoolSize() > 0)
                Thread.sleep(1000);

            // 等待executor执行完所有任务
            while (!this.awaitTermination(1, TimeUnit.SECONDS))
            {
            }
        }
        catch (InterruptedException ex)
        {
            log.error("InterruptedException", ex);
        }
    }
}
