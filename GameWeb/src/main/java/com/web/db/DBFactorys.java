package com.web.db;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBFactorys {
	private static final String CONFIG = "/db-config.xml";

	private SqlSessionFactory sessionFactory;

	private final static DBFactorys instance;
	
	
	static{
		instance = new DBFactorys();
	}
	
	public static DBFactorys getInstance(){
		return instance;
	}
	
	/**
	 * 初始化一个DB实例.
	 *
	 * @param loggerName
	 *            logger名称
	 * @param config
	 *            mybatis配置文件的相对路径
	 */
	private DBFactorys() {
		InputStream in = DBFactorys.class.getResourceAsStream(CONFIG);
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
	 * 获取SQLSeesionFactory.
	 *
	 * @return
	 */
	public SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
