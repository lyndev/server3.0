/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.concurrent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Administrator
 * @param <K>
 * @param <V>
 */
public class OrderedQueuePool<K, V>
{
    private final Map<K, CommandQueue<V>> map = new ConcurrentHashMap<>();

    /**
     * 获得指定键值的命令队列.
     *
     * @param key 指定键值
     * @return
     */
    public synchronized CommandQueue<V> getCommandQueue(K key)
    {
        CommandQueue<V> queue = map.get(key);
        if (queue == null)
        {
            queue = new CommandQueue<>();
            map.put(key, queue);
        }
        return queue;
    }

    public synchronized int getPoolSize()
    {
        int sum = 0;
        for (Entry<K, CommandQueue<V>> entry : map.entrySet())
        {
            CommandQueue<V> queue = entry.getValue();
            sum += queue.size();
        }
        return sum;
    }

    /**
     * 获得所有命令队列.
     *
     * @return
     */
    public synchronized Map<K, CommandQueue<V>> getAllCommandQueues()
    {
        return map;
    }

    /**
     * 移除指定键值的命令队列.
     *
     * @param key 指定键值
     */
    public synchronized void removeCommandQueue(K key)
    {
        map.remove(key);
    }
}
