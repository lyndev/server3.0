/*
 * 文件名：ExecutorServices.java
 * 版权：Copyright 2012-2014 Chengdu HaoWan123 Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.haowan.logger.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.common.platform.config.ConfigurationKey;
import com.common.platform.config.ConfigurationReader;

/**
 * 功能描述：多任务执行服务处理器。
 * <p>
 * 
 * @author andy.zheng 
 * @version 1.0, 2014年6月13日 下午7:12:39
 * @since Common-Platform/ 1.0
 */
public final class ExecutorServices {

    /** 任务执行服务对象 */
    private static ExecutorService services;

    static {
        // 工作线程默认为3。
        int workThread = 3;

        try {
            workThread = ConfigurationReader.getInstance().getInt(ConfigurationKey.WORK_THREAD_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        services = Executors.newFixedThreadPool(workThread);
    }

    /**
     * 功能描述：提交回调任务到执行服务中。
     * 
     * @param task  当前回调任务。
     * @return      任务执行结果。
     */
    public static Future<?> submit(Callable<?> task) {
        return services.submit(task);
    }

    /**
     * 功能描述：提交任务到执行服务中。
     *
     * @param task  待执行的任务。
     */
    public static void submit(Runnable task) {
        services.execute(task);
    }
}
