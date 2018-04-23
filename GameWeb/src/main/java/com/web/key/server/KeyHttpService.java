package com.web.key.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.http.api.HttpRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.db.bean.KeyAttrBean;
import com.web.db.bean.KeyBean;
import com.web.db.dao.KeyAttrDao;
import com.web.db.dao.KeyDao;
import com.web.utils.GenerateKeyUtil;

/**
 * 鸡和马Http消息处理
 * 
 * @author XU
 *
 */
public class KeyHttpService {

	private final Logger log = Logger.getLogger(KeyHttpService.class);
	private final static KeyHttpService instance;

	static {
		instance = new KeyHttpService();
	}

	public static KeyHttpService getInstance() {
		return instance;
	}

	/**
	 * 处理激活码的消息
	 * 
	 * @param session
	 * @param httpRequest
	 */
	public void dispatcherRequest(IoSession session, HttpRequest httpRequest) {
		UseResult useResult = new UseResult();
		useResult.setCode(UseCode.OK.getCode());
		String param = httpRequest.getParameter("param");
		if (param == null || param.isEmpty()) {
			log.error("请求参数错误，参数为空。");
			useResult.setCode(UseCode.PARAM_IS_ERROR.getCode());
		}
		try {
			param = URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error(e);
			useResult.setCode(UseCode.PARAM_IS_ERROR.getCode());
		}
		if (useResult.getCode() == UseCode.OK.getCode()) {
			JSONObject jsonObject = JSONObject.parseObject(param);
			RequestParam requestParam = new RequestParam();
			requestParam.fromJson(jsonObject);
			this.useKey(requestParam, useResult);
		}

		GameHttpResponse httpResponse = new GameHttpResponse();
		String responseStr = JSONObject.toJSONString(useResult);
		httpResponse.setContentLength(responseStr.getBytes().length);
		IoBuffer buf = IoBuffer.allocate(responseStr.getBytes().length)
				.setAutoExpand(true);
		buf.put(responseStr.getBytes());
		buf.flip();

		session.write(httpResponse);
		session.write(buf);
	}

	private void useKey(RequestParam requestParam, UseResult useResult) {
		if (requestParam == null) {
			log.error("请求参数错误，参数缺失或者参数json不对。");
			useResult.setCode(UseCode.PARAM_IS_ERROR.getCode());
			return;
		}

		String key = requestParam.getKey();
		useResult.setKey(key);
		short batch = GenerateKeyUtil.getBatchByKey(key); // 通过Key算出batch
		if (batch <= 0) {
			log.info("激活码批次肯定不会是小于0的数。key = " + key);
			useResult.setCode(UseCode.KEYATTR_NOT_FOUND.getCode());
			return;
		}
		// 先检测属性表：因为里面有批次
		KeyAttrBean keyAttrBean = KeyAttrDao.selectByBatch(batch);
		if (keyAttrBean == null) {
			log.info("激活码属性未找到。key = " + key);
			useResult.setCode(UseCode.KEYATTR_NOT_FOUND.getCode());
			return;
		}

		KeyBean keyBean = KeyDao.selectByKey(key, batch);
		if (keyBean == null) {
			log.info("激活码表中未找到数据。key = " + key);
			useResult.setCode(UseCode.KEY_NOTFOUND.getCode());
			return;
		}

		if (keyBean.isUse()) {
			log.info("激活码已经使用。key = " + key);
			useResult.setCode(UseCode.KEY_ISUSED.getCode());
			return;
		}

		for (int type : requestParam.getUsedType()) {
			if (type == keyAttrBean.getKeyType()) {
				useResult.setCode(UseCode.KEY_USE_ONCE.getCode());
				log.info("同种类型的激活码只能使用一次");
				return;
			}
		}
		
		if (keyAttrBean.getFgi() != 0
				&& requestParam.getFgi() != keyAttrBean.getFgi()) {
			log.info("使用渠道不对。cur=" + requestParam.getFgi() + " fgi="
					+ keyAttrBean.getFgi());
			useResult.setCode(UseCode.USEFGI_WRONG.getCode());
			return;
		}

		if (keyAttrBean.getUseServer() != 0
				&& requestParam.getServerId() != keyAttrBean.getUseServer()) {
			log.info("使用服务器不对。cur=" + requestParam.getServerId() + " fgi="
					+ keyAttrBean.getUseServer());
			useResult.setCode(UseCode.USESERVER_WRONG.getCode());
			return;
		}

		if (!keyAttrBean.getPlatform().equals("all")
				&& requestParam.getPlatform().equals(keyAttrBean.getPlatform())) {
			log.info("使用平台不对。cur=" + requestParam.getServerId() + " fgi="
					+ keyAttrBean.getUseServer());
			useResult.setCode(UseCode.USEPLAM_WRONG.getCode());
			return;
		}

		long now = System.currentTimeMillis();
		if (now > keyAttrBean.getDeadTime()) {
			log.info("激活码过期了。key = " + key);
			useResult.setCode(UseCode.KEY_OVERDUE.getCode());
			return;
		}

		// 更新一下数据
		keyBean.setUserInfo(requestParam.getRoleId());
		keyBean.setUse(true);
		int result = KeyDao.update(keyBean, batch);
		if (result == 0) {
			log.info("使用激活码验证成功，但是更新数据库失败。key = " + key);
			useResult.setCode(UseCode.KEY_USE_FAIL.getCode());
			return;
		}
		// 使用成功OK
		useResult.setCode(UseCode.OK.getCode());
		useResult.setReward(keyAttrBean.getReward());
		useResult.setUseTime(now);
		useResult.setEndTime(keyAttrBean.getDeadTime());
		useResult.setKeyType(keyAttrBean.getKeyType());
		log.info("激活码验证成功：key=" + useResult.getKey());
	}

	public class UseResult {

		private String key;

		private int keyType;

		private int code;

		private String reward;

		private long useTime;

		private long endTime;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public int getKeyType() {
			return keyType;
		}

		public void setKeyType(int keyType) {
			this.keyType = keyType;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getReward() {
			return reward;
		}

		public void setReward(String reward) {
			this.reward = reward;
		}

		public long getUseTime() {
			return useTime;
		}

		public void setUseTime(long useTime) {
			this.useTime = useTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

	}

	public class RequestParam {

		private String key; // 激活码

		private int serverId; // 区服ID

		private short fgi; // 渠道

		private String platform;

		private String roleId;

		private List<Integer> usedType;

		public void fromJson(JSONObject jsonObject) {
			this.key = jsonObject.getString("key");
			this.platform = jsonObject.getString("platform");
			this.roleId = jsonObject.getString("roleId");
			this.serverId = Integer.valueOf(jsonObject.getString("serverId"));
			this.fgi = Short.valueOf(jsonObject.getString("fgi"));
			this.usedType = new ArrayList<>();
			JSONArray jsonArray = jsonObject.getJSONArray("usedType");
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object obj : jsonArray) {
					usedType.add((Integer) obj);
				}
			}
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public int getServerId() {
			return serverId;
		}

		public void setServerId(int serverId) {
			this.serverId = serverId;
		}

		public short getFgi() {
			return fgi;
		}

		public void setFgi(short fgi) {
			this.fgi = fgi;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getRoleId() {
			return roleId;
		}

		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}

		public List<Integer> getUsedType() {
			return usedType;
		}

		public void setUsedType(List<Integer> usedType) {
			this.usedType = usedType;
		}
		
	}

	public enum UseCode {

		OK(2000), // 使用成功

		PARAM_IS_ERROR(3201), // 请求参数错误

		KEY_NOTFOUND(3202), // 找不到激活码

		KEYATTR_NOT_FOUND(3203), // 激活码属性找不到

		KEY_ISUSED(3204), // 激活码已经使用

		USEFGI_WRONG(3205), // 渠道不对（0-所有，1-ios，2-安卓）

		USESERVER_WRONG(3206), // 使用服务器不对

		USEPLAM_WRONG(3207), // 使用平台不对

		KEY_OVERDUE(3208), // 激活码过期了

		KEY_USE_FAIL(3209), // 激活码使用失败
		
		KEY_USE_ONCE(3210), // 同种类型的兑换码只能使用一个
		;

		private final int value;

		UseCode(int value) {
			this.value = value;
		}

		public int getCode() {
			return value;
		}

		public boolean compare(int value) {
			return this.value == value;
		}

	}
}
