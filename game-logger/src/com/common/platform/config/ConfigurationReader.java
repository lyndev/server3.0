/*
 * 文件名：ConfigurationReader.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.common.platform.config;

import java.util.Properties;

import com.haowan.logger.dao.MongoDBHelper;

/**
 * 功能描述：<code>ConfigurationReader</code>类是客户端配置项读取器。
 * <p>
 * 功能详细描述：系统加载时，本读取器将加载<b>config.properties</b>配置文件并读取所有配置项缓存到系统内存中。
 * 本类为单例实例，获取配置项示例代码如下：
 * 
 * <pre>
 * ConfigurationReader configReader = ConfigurationReader.getInstance();
 * String solrBaseUrl = configReader.getString(ConfigurationKey.SOLR_BASE_URL);
 * </pre>
 * 
 * @author andy.zheng 
 * @version 1.0, 2014年6月13日 下午2:45:28
 * @since Common-Platform/ 1.0
 */
public final class ConfigurationReader {

	/** 系统配置文件读取器 */
	private static ConfigurationReader configReader = new ConfigurationReader();

	/** 配置属性集合 */
	private Properties properties = null;

	/**
	 * 私有构造器
	 */
	private ConfigurationReader() {
		properties = new Properties();
	}

	/**
	 * 获取配置文件读取器对象
	 * 
	 * @return 配置文件读取器对象
	 */
	public static ConfigurationReader getInstance() {
		return configReader;
	}

	/**
	 * 根据key获取值
	 * 
	 * 值类型为字符串
	 * 
	 * @param key
	 *            配置键
	 * @return 字符类型的值
	 */
	public String getString(String key) {
		if (null != key && "".equals(key)) {
			return null;
		}
		return properties.getProperty(key);
	}

	/**
	 * 根据key获取值
	 * 
	 * 值类型为Int数值类型
	 * 
	 * @param key
	 *            配置键
	 * @return Int数值类型的值
	 */
	public Integer getInt(String key) {
		if (null != key && "".equals(key)) {
			return null;
		}
		return Integer.valueOf(this.getString(key));
	}

	/**
	 * 根据key获取值
	 * 
	 * 值类型为Boolean类型
	 * 
	 * @param key
	 *            配置键
	 * @return Boolean类型的值
	 */
	public Boolean getBoolean(String key) {
		if (null != key && "".equals(key)) {
			return null;
		}
		return Boolean.valueOf(this.getString(key));
	}

	/**
	 * 
	 * 功能描述：配置mogodb
	 * 
	 * @see 类、类#方法、类#成员
	 * @param gameId
	 * @param mdbHost
	 * @param mdbPort
	 * @param mdbName
	 * @param mdbUserName
	 * @param mdbPwd
	 */
	public void setProperties(String gameId, String mdbHost, String mdbPort,
			String mdbName, String mdbUserName, String mdbPwd) {
		properties.setProperty(ConfigurationKey.CURRENT_GAME_ID_KEY, gameId);

		properties.setProperty(ConfigurationKey.MONGO_DB_HOST_KEY, mdbHost);
	
		if (null == mdbPort || 0 == mdbPort.trim().length()) {
			properties.setProperty(ConfigurationKey.MONGO_DB_PORT_KEY, "27017");
		}else{
			properties.setProperty(ConfigurationKey.MONGO_DB_PORT_KEY, mdbPort);
		}

		properties.setProperty(ConfigurationKey.MONGO_DB_NAME_KEY, mdbName);

		if (null != mdbUserName && 0 < mdbUserName.trim().length()) {
			properties.setProperty(ConfigurationKey.MONGO_USER_NAME_KEY,
					mdbUserName);
		}
		
		if (null != mdbPwd && 0 < mdbPwd.trim().length()) {
			properties.setProperty(ConfigurationKey.MONGO_PASSWORD_KEY, mdbPwd);
		}
		
		properties.setProperty(ConfigurationKey.MONGO_THREADS_KEY, "50");
		properties.setProperty(ConfigurationKey.WORK_THREAD_COUNT, "5");
		properties.setProperty(ConfigurationKey.MONGO_CONNECTIONS_KEY, "100");
		properties.setProperty(ConfigurationKey.BACK_LOG_TEMP_PATH,
				"back_log.tmp");
		
		MongoDBHelper.getInstance().init();
	}
}
