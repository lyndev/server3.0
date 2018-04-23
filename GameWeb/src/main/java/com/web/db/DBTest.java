package com.web.db;
import com.web.db.bean.GlobalBean;
import com.web.db.dao.GlobalDao;

public class DBTest {
	public static void main(String[] args) {
//		int success = 0;
//		int filed = 0;
//		short batch = 2;
//		Set<String> keys = GenerateKeyUtil.generateKey(batch,(short)1000);
//		for (String key : keys) {
//			KeyBean keyBean = new KeyBean();
//			keyBean.setAttrId(batch);
//			keyBean.setUse(false);
//			keyBean.setKeyCode(key);
//			if (KeyDao.insert(keyBean, batch) == 0) {
//				filed += 1;
//			} else {
//				success += 1;
//			}
//		}
		
		for (int i = 10100; i < 10200; i++) {
			GlobalBean globalBean = new GlobalBean();
	    	globalBean.setId(i); //激活码表的批次
	    	globalBean.setValue("1");
			if(GlobalDao.insert(globalBean) != 0){
				System.out.println("创建全局数据。id = "+i);	
			}
		}
		System.err.println("++++++++++++++++++++++");
		
		
//		System.out.println("生成成功。实际：" + success + ", 失败：" + filed);
		
		
//		KeyDao.insert(keyBean, batch);
//		System.err.println(JSONObject.toJSONString(KeyDao.selectByKey("1ux4y4wle", (short) 2)));
//		System.err.println(JSONObject.toJSONString(KeyDao.selectByKey("1ux4y4wle", (short) 2)));
//		System.err.println(JSONObject.toJSONString(KeyDao.selectByKey("1ux4y4wle", (short) 2)));
//		System.err.println(JSONObject.toJSONString(KeyDao.selectByKey("1ux4y4wle", (short) 2)));
	}
}