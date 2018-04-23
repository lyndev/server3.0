package com.web.db;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public enum DBFactory {
	GAME_DB("gameDB", "/db-config.xml");

	private final Logger logger;
	@SuppressWarnings("unused")
	private final String name;
	private SqlSessionFactory sessionFactory;

	/**
	 * 初始化一个DB实例.
	 *
	 * @param loggerName
	 *            logger名称
	 * @param config
	 *            mybatis配置文件的相对路径
	 */
	private DBFactory(String name, String config) {
		this.name = name;
		this.logger = Logger.getLogger(name + "Logger");
		InputStream in = DBFactory.class.getResourceAsStream(config);
		this.sessionFactory = new SqlSessionFactoryBuilder().build(in);
		
//		this.name = name;
//        this.logger = Logger.getLogger(name + "Logger");
//        try
//        {
//			config = DBFactory.class.getResource("/").getFile() + config;
//        	InputStream in = new FileInputStream(config);
//            this.sessionFactory = new SqlSessionFactoryBuilder().build(in);
//        }
//        catch (IOException e)
//        {
//            logger.error(e, e);
//        }
	}

	/**
	 * 获取日志管理器.
	 *
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * 获取SQLSeesionFactory.
	 *
	 * @return
	 */
	public SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
