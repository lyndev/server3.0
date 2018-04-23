package com.web.logic.service;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.web.db.bean.GlobalBean;
import com.web.db.bean.KeyAttrBean;
import com.web.db.bean.KeyBean;
import com.web.db.dao.GlobalDao;
import com.web.db.dao.KeyAttrDao;
import com.web.db.dao.KeyDao;
import com.web.utils.GenerateKeyUtil;

@Service("keyCodeService")
public class KeyCodeService {

	private static final Logger logger = Logger.getLogger(KeyCodeService.class);

	/**
	 * 生成礼包激活码
	 * 
	 * @param bean
	 */
	public String generateKey(KeyAttrBean keyAttrBean) {
		GlobalBean globalBean = GlobalDao.select(1001);
		short batch = 1;
		if (globalBean != null) {
			batch = (short) (Short.valueOf(globalBean.getValue()) + 1);
		} else {
			logger.error("全局表中找不到激活码批次的配置。");
		}

		KeyAttrBean bean = KeyAttrDao.selectByBatch(batch);
		if (bean != null) {
			logger.error("激活码批次不对，找程序检查。batch = " + batch);
			return "激活码批次不对，找程序检查。batch = " + batch;
		}

		keyAttrBean.setBatch(batch);
		keyAttrBean.setOperator("admin");

		// 1、先增加属性表的记录
		int result = KeyAttrDao.insert(keyAttrBean);
		if (result == 0) {
			logger.error("插入激活码属性表失败。batch = " + batch);
			return "插入激活码属性表失败";
		}
		// 2、创建批次表
		if (!KeyDao.createTableKey(batch)) {
			logger.error("创建批次表失败。");
			return "创建批次表失败";
		}
		// 3、插入激活码
		int success = 0;
		int filed = 0;
		Set<String> keys = GenerateKeyUtil.generateKey(batch,
				keyAttrBean.getGenerateNum());
		//只需要new一次就OK
		KeyBean keyBean = new KeyBean();
		for (String key : keys) {
			keyBean.setAttrId(batch);
			keyBean.setUse(false);
			keyBean.setKeyCode(key);
			if (KeyDao.insert(keyBean, batch) == 0) {
				logger.error("插入数据库时出错, 出错的激活码为: " + keyBean.getKeyCode());
				filed += 1;
			} else {
				success += 1;
			}
		}
		
		logger.info("激活码生成结束。批次："+batch+"，成功：" + success + "，失败：" + filed);
		// 4、更新全局表
		globalBean.setValue(batch + "");
		GlobalDao.update(globalBean);
		return "生成成功。实际：" + success + ", 失败：" + filed;
	}
	
	/**
	 * 获取激活码属性表所有记录
	 * @return
	 */
	public List<KeyAttrBean> getKeyAttrBeanList(){
		return KeyAttrDao.selectAll();
	}
}
