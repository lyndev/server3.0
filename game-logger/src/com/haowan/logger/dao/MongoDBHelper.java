/*
 * 文件名：MongoDBHelper.java
 * 版权：Copyright 2012-2014 Chengdu HaoWan123 Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.haowan.logger.dao;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.common.platform.config.ConfigurationKey;
import com.common.platform.config.ConfigurationReader;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

/**
 * 功能描述：MongoDB数据库访问工具类。
 * <p>
 * 
 * @author andy.zheng 
 * @version 1.0, 2014年6月13日 下午2:45:28
 * @since Common-Platform/ 1.0
 */
public final class MongoDBHelper {

    /** 日志对象 */
    private static Logger logger = Logger.getLogger(MongoDBHelper.class);

    /** Mongo数据库访问实例对象 **/
    private static MongoDBHelper helper = new MongoDBHelper();

    /** Mongo连接池对象 */
    private static MongoClient mongo;

    /** 数据库对象 */
    private static DB db;

    public void init(){
        ConfigurationReader reader = ConfigurationReader.getInstance();

        try {
            String host = reader.getString(ConfigurationKey.MONGO_DB_HOST_KEY);
            int port = reader.getInt(ConfigurationKey.MONGO_DB_PORT_KEY);
            String dbName = reader.getString(ConfigurationKey.MONGO_DB_NAME_KEY);
            String userName = reader.getString(ConfigurationKey.MONGO_USER_NAME_KEY);
            String password = reader.getString(ConfigurationKey.MONGO_PASSWORD_KEY);

            Builder builder = MongoClientOptions.builder();
            int connections = reader.getInt(ConfigurationKey.MONGO_CONNECTIONS_KEY);
            if (connections > 0) {
                builder.connectionsPerHost(connections);
            }

            int threads = reader.getInt(ConfigurationKey.MONGO_THREADS_KEY);
            if (threads > 0) {
                builder.threadsAllowedToBlockForConnectionMultiplier(threads);
            }

            mongo = new MongoClient(new ServerAddress(host, port), builder.build());
            db = mongo.getDB(dbName);
            if (null != userName && !userName.isEmpty() && null != password) {
                db.authenticate(userName, password.toCharArray());
            }
        } catch (UnknownHostException e) {
            logger.error("Connectn mongo db fail!", e);
        }
    }

    /**
     * 功能描述：获取MongoDBHelp实例。
     * 
     * @return
     */
    public static MongoDBHelper getInstance() {
        return helper;
    }

    /**
     * 功能描述：记录日志信息。
     * 
     * @param values
     *            待记录的日志信息。
     * @return 记录日志是否成功。
     */
    public boolean addLog(Map<String, Object> values) throws Exception {
        if (null == values || values.isEmpty()) {
            return false;
        }

        // 封装日志信息
        BasicDBObject document = new BasicDBObject(values);

        // 获取集合对象
        DBCollection collection = this.getCollection();
        try {
            collection.insert(document);
        } catch (Exception e) {
            throw e;
        }

        return true;
    }

    /**
     * 功能描述：记录日志信息。
     * 
     * @param values
     *            待记录的日志信息。
     * @return 记录日志是否成功。
     */
    public boolean addLog(BasicDBObject logObject) throws Exception {
        if (null == logObject) {
            return false;
        }

        // 获取集合对象
        DBCollection collection = this.getCollection();
        try {
            collection.insert(logObject);
        } catch (Exception e) {
            throw e;
        }

        return true;
    }

    /**
     * 功能描述：ping MongoDB是否联通。
     * 
     * @return MongoDB是否联通。
     */
    public boolean ping() {
        boolean isConnection = true;

        ConfigurationReader reader = ConfigurationReader.getInstance();
        String dbName = reader.getString(ConfigurationKey.MONGO_DB_NAME_KEY);
        String userName = reader.getString(ConfigurationKey.MONGO_USER_NAME_KEY);
        String password = reader.getString(ConfigurationKey.MONGO_PASSWORD_KEY);

        try {
            this.mongo.getConnector().authenticate(
                    MongoCredential.createMongoCRCredential(userName, dbName, password.toCharArray()));
        } catch (Exception e) {
            if (e instanceof MongoException.Network) {
                logger.error("Connect mongo db fail!", e);
                isConnection = false;
            }
        }

        return isConnection;
    }

    /**
     * 功能描述： 获取当前集合名称。
     * 
     * @return 集合名称。
     */
    public DBCollection getCollection() {
        DBCollection collection = null;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);

        String gameName = ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
        String currentCollectionName = gameName + "-" + year + "_" + month + "_" + day;

        collection = this.db.getCollection(currentCollectionName);

        return collection;
    }

    /**
     * 功能描述：断开MongoDB连接。
     * 
     */
    public void close() {
        mongo.close();
    }

    /*
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        this.close();
    }

    public static void main(String[] args) {
    	ConfigurationReader.getInstance().setProperties("172", "127.0.0.1", "27017", "game_log", "", "");
    	
        final MongoDBHelper helper = MongoDBHelper.getInstance();

        boolean isConnection = helper.ping();
        if (!isConnection) {
            System.err.println("数据库连接失败！！！");
            return;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        int count = 500;
        final CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread() {
                @SuppressWarnings("serial")
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        try {
                            helper.addLog(new HashMap<String, Object>() {
                                {
                                    this.put("name", "andy");
                                    this.put("age", "29");
                                    this.put("random", UUID.randomUUID());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    latch.countDown();
                };
            }.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //helper.getCollection().drop();

        helper.close();
    }
}
