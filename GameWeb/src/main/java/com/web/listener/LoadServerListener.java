/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.web.db.bean.GlobalBean;
import com.web.db.dao.GlobalDao;
import com.web.key.server.KeyHttpServer;

/**
 *
 * @author XU
 */
public class LoadServerListener implements ServletContextListener {

    private final Logger log = Logger.getLogger(LoadServerListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Web服务器开始启动。");
        KeyHttpServer.getInstance().accept();
        GlobalBean globalBean = GlobalDao.select(1001);
        if (globalBean == null) {
        	globalBean = new GlobalBean();
        	globalBean.setId(1001); //激活码表的批次
        	globalBean.setValue("1");
			GlobalDao.insert(globalBean);
			log.info("创建全局数据 1001。");
		}
    }

    public void contextDestroyed(ServletContextEvent sce) {
    	KeyHttpServer.getInstance().unaccept();
    	log.info("Web服务器已经关闭。");
    }
    
}
