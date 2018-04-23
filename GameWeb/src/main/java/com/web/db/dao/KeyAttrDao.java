package com.web.db.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.web.db.DBFactory;
import com.web.db.bean.KeyAttrBean;

public class KeyAttrDao {

	public static List<KeyAttrBean> selectAll() {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			List<KeyAttrBean> beans = session.selectList("keyAttr.selectAll");
			return beans;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return null;

	}

	public static KeyAttrBean selectByBatch(short batch) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			KeyAttrBean bean = session.selectOne("keyAttr.selectById", batch);
			return bean;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return null;

	}

	public static int insert(KeyAttrBean bean) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			int rows = session.insert("keyAttr.insert", bean);
			session.commit();
			return rows;
		} catch (Exception e) {
			DBFactory.GAME_DB.getLogger().error(e);
		}
		return 0;
	}

}
