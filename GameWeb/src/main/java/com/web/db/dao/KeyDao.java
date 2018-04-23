package com.web.db.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.web.db.DBFactory;
import com.web.db.bean.KeyBean;

public class KeyDao {

	public static List<KeyBean> selectKeysByBatch(short batch) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			String tableName = "chickenandhorse_"+batch;
			Map<String, String> map = new HashMap<String, String>();
			map.put("tableName", tableName);
			List<KeyBean> beans = session.selectList("key.selectByBatch", map);
			return beans;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return null;

	}
	
	public static KeyBean selectByKey(String key, short batch) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			Map<String, String> map = new HashMap<String, String>();
			String tableName = "chickenandhorse_"+batch;
			map.put("keyCode", key);
			map.put("tableName", tableName);
			KeyBean bean = (KeyBean) session.selectOne("key.selectByKey", map);
			return bean;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return null;

	}

	public static int insert(KeyBean bean, short batch) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			String tableName = "chickenandhorse_"+batch;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tableName", tableName);
			map.put("keyCode", bean.getKeyCode());
			map.put("attrId", bean.getAttrId());
			map.put("isUse", bean.isUse());
			map.put("userInfo", bean.getUserInfo());
			int rows = session.insert("key.insert", map);
			session.commit();
			return rows;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return 0;

	}

	public static int update(KeyBean bean, short batch) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			String tableName = "chickenandhorse_"+batch;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tableName", tableName);
			map.put("keyCode", bean.getKeyCode());
			map.put("attrId", bean.getAttrId());
			map.put("isUse", bean.isUse());
			map.put("userInfo", bean.getUserInfo());

			int rows = session.update("key.updateUseData", map);
			session.commit();
			return rows;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return 0;
	}

	/**
	 * 创建一个激活码批次表
	 * 
	 * @param tableName
	 */
	public static boolean createTableKey(short batch) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			Connection conn = session.getConnection();
			Statement stat = conn.createStatement();
			// 构建sql语句
			String tableName = "chickenandhorse_" + batch;
			StringBuilder sql = new StringBuilder();
			sql.append("CREATE TABLE `" + tableName + "`(");
			sql.append("`keyCode` varchar(30),");
			sql.append("`attrId` smallint,");
			sql.append("`isUse` bit,");
			sql.append("`userInfo` varchar(255),");
			sql.append("PRIMARY KEY(`keyCode`)");
			sql.append(")");
			sql.append("ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			// 开始执行创建table语句
			// stat.addBatch("DROP TABLE IF EXISTS `" + tableName + "`;");
			stat.addBatch(sql.toString());
			// 执行sql语句
			stat.executeBatch();

			conn.commit();
			DBFactory.GAME_DB.getLogger().info("【" + tableName + "】数据表创建完成");
			return true;
		} catch (BatchUpdateException e) {
			DBFactory.GAME_DB.getLogger().error(e);
		} catch (SQLException e) {
			DBFactory.GAME_DB.getLogger().error(e);
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return false;
	}
}
