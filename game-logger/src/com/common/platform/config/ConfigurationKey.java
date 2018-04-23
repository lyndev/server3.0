/*
 * 文件名：ConfigurationKey.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 薪酬自动分发客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.common.platform.config;


/**
 * 功能描述：<code>ConfigurationKey</code>提供客户端配置项key容器。
 * <p>
 * 功能详细描述：本类管理客户端配置项key全局配置，所声明的常量与<b>config.properties</b>配置文件中的key一一对应。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @version  1.0, 2013-3-29 下午1:41:24
 * @since    SolrClient 1.0
 */
public final class ConfigurationKey {

    /** 当前游戏Key */
	public static final String CURRENT_GAME_ID_KEY = "app.game.name";
	
	/** Mongo数据库主机Key */
	public static final String MONGO_DB_HOST_KEY = "db.mongo.host";
	
	/** Mongo数据库端口Key */
	public static final String MONGO_DB_PORT_KEY = "db.mongo.port";
	
	/** Mongo数据库名Key */
	public static final String MONGO_DB_NAME_KEY = "db.mongo.dbname";
	
	/** Mongo数据库连接用户名Key */
	public static final String MONGO_USER_NAME_KEY = "pt.db.mongo.username";
	
	/** Mongo数据库连接密码Key  */
    public static final String MONGO_PASSWORD_KEY = "db.mongo.password";
	
	/** Mongo数据库连接密码Key  */
	public static final String MONGO_CONNECTIONS_KEY = "db.mongoOptions.connectionsPerHost";
	
	/** Mongo数据库连接密码Key  */
	public static final String MONGO_THREADS_KEY = "db.mongoOptions.threadsAllowedToBlockForConnectionMultiplier";
	
	/** 工作线程数量Key */
	public static final String WORK_THREAD_COUNT = "app.work.thread.count";
	
	/** 日志备份临时路径Key */
	public static final String BACK_LOG_TEMP_PATH = "app.back.log.temp.path";
}
