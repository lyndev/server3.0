package com.web.test;

import com.web.db.bean.GlobalBean;
import com.web.db.bean.KeyAttrBean;
import com.web.db.bean.KeyBean;
import com.web.db.dao.KeyDao;

public class DBTest {
	public static void main(String[] args) {

		GlobalBean bean = new GlobalBean();

		KeyAttrBean keyAttrBean = new KeyAttrBean();
		for (int i = 1; i < 1000; i++) {
			// keyAttrBean.setBatch(i);
			// keyAttrBean.setDeadTime(0);
			// keyAttrBean.setDescStr("");
			// keyAttrBean.setFgi((short)1);
			// keyAttrBean.setReward("");
			// KeyAttrDao.insert(keyAttrBean);
			KeyBean keyBean = new KeyBean();
			keyBean.setAttrId((short) 1);
			keyBean.setKeyCode("" + i);
			keyBean.setUse(false);
			KeyDao.insert(keyBean, (short) 2);
			// bean.setId(i);
			// bean.setValue("1111");
			// GlobalDao.insert(bean);
			System.err.println("=====================" + i);
		}
	}
}