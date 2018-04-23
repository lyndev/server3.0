/*
 * 文件名：ServicesFactory.java
 * 版权：Copyright 2012-2014 Chengdu HaoWan123 Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.haowan.logger.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.common.platform.config.ConfigurationReader;
import com.haowan.logger.service.impl.LogServiceImpl;
import com.haowan.logger.service.impl.VerifyServiceImpl;

/**
 * 功能描述：<code>ServicesFactory</code>类为业务服务工厂类。
 * <p>
 * 
 * @author andy.zheng 
 * @see LogService
 * @see VerifyService
 * @version 1.0, 2014年6月13日 下午8:38:05
 * @since Common-Platform/ 1.0
 */
public final class ServicesFactory {

	/** 日志服务对象 */
	private static LogService logService = new LogServiceImpl();

	/** 认证服务对象 */
	private static VerifyService verifyService = new VerifyServiceImpl();

	/**
	 * 功能描述：获取日志服务单例对象。
	 * 
	 * @return 日志服务对象。
	 */
	public static LogService getSingleLogService() {
		return logService;
	}

	/**
	 * 功能描述：获取认证业务服务单例对象。
	 * 
	 * @return 认证业务服务对象。
	 */
	public static VerifyService getSingleVerifyService() {
		return verifyService;
	}
}
