/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.db.dao;

import org.apache.ibatis.session.SqlSession;

import com.web.db.DBFactory;
import com.web.db.bean.GlobalBean;

/**
 * 
 * @date 2014-6-26
 * @author pengmian
 */
public class GlobalDao {
	public static int insert(GlobalBean globalBean) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			int r = session.insert("global.insert", globalBean);
//			DBFactory.GAME_DB.getLogger().info("新建全局数据: " + globalBean.getId());
			session.commit();
			return r;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return 0;
	}

	public static int update(GlobalBean globalBean) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			int r = session.update("global.update", globalBean);
			session.commit();
			// DBFactory.GAME_DB.getLogger().info("更新全局数据: " +
			// globalBean.getId());
			return r;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return 0;
	}

	public static GlobalBean select(int id) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			GlobalBean bean = session.selectOne("global.selectOne", id);
			session.commit();
			return bean;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return null;
	}

}
