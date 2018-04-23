/*
 * 文件名：LogServiceImpl.java
 * 版权：Copyright 2012-2014 Chengdu HaoWan123 Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.haowan.logger.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.common.platform.config.ConfigurationKey;
import com.common.platform.config.ConfigurationReader;
import com.common.platform.config.Constants;
import com.haowan.logger.config.ErrorCode;
import com.haowan.logger.dao.MongoDBHelper;
import com.haowan.logger.service.LogService;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * 功能描述：日志采集业务访问实现类。
 * <p>
 * 
 * @author andy.zheng 
 * @see 相关类/方法
 * @version 1.0, 2014年6月13日 下午4:56:55
 * @since Common-Platform/ 1.0
 */
public final class LogServiceImpl implements LogService {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(LogService.class);

	/** 日志备份缓存器 */
	private static LinkedBlockingQueue<Object> backLogCache = new LinkedBlockingQueue<Object>();

	{
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				// 启动日志监控器
				LogServiceImpl.this.startLogMoninter();
			}
		}, 1 * 60L, 5 * 60L, TimeUnit.SECONDS);
	}

	/*
	 * @see com.haowan.logger.service.LogService#addCommonLog1(java.lang.String)
	 */
	@Override
	public void addCommonLog1(String totalJson) throws JSONException {
		if (null == totalJson || totalJson.isEmpty()) {
			throw new JSONException("Invalid json string!");
		}

		try {
			Object obj = JSON.parse(totalJson);
			if (obj instanceof BasicDBObject) {
				BasicDBObject gameLog = (BasicDBObject) obj;
				String currentGame = ConfigurationReader.getInstance()
						.getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
				gameLog.put("game", currentGame);

				ErrorCode code = ErrorCode.SUCCESS;
				try {
					MongoDBHelper.getInstance().addLog(gameLog);
				} catch (Exception e) {
					if (e instanceof MongoException.Network) {
						try {
							backLogCache.put(gameLog);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						code = ErrorCode.CONNECT_DB_FAIL;
					} else {
						code = ErrorCode.SAVE_LOG_ERROR;
					}

					logger.error(code.getDesc(), e);
				}
			}
		} catch (Exception e) {
			logger.error("Parse json fail!", e);
		}
	}

	/*
	 * @see com.haowan.logger.service.LogService#addCommonLog2(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addCommonLog2(String eventId, String eventJson)
			throws JSONException {
		if (null == eventId || eventId.isEmpty()) {
			throw new IllegalArgumentException("The event id is null!");
		}

		if (null == eventJson || eventJson.isEmpty()) {
			throw new JSONException("Invalid json string!");
		}

		try {
			Object obj = JSON.parse(eventJson);
			if (obj instanceof BasicDBObject) {
				BasicDBObject gameLog = (BasicDBObject) obj;
				String currentGame = ConfigurationReader.getInstance()
						.getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
				gameLog.put("game", currentGame);
				gameLog.put("eventId", eventId);

				ErrorCode code = ErrorCode.SUCCESS;
				try {
					MongoDBHelper.getInstance().addLog((BasicDBObject) gameLog);
				} catch (Exception e) {
					if (e instanceof MongoException.Network) {
						try {
							backLogCache.put(gameLog);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						code = ErrorCode.CONNECT_DB_FAIL;
					} else {
						code = ErrorCode.SAVE_LOG_ERROR;
					}

					logger.error(code.getDesc(), e);
				}
			}
		} catch (Exception e) {
			logger.error("Parse json fail!", e);
		}
	}

	/*
	 * @see com.haowan.logger.service.LogService#addLogLogin(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogLogin(String client, String fgi, String platform,
			String fedId, String game_uid, String platform_uid, String gender,
			String age, String version_res, String version_exe, String device,
			String ip, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SERVER_LOGIN_EVENT_ID);
		values.put("game", currentGame);
		values.put("client", client);
		values.put("fgi", fgi);
		values.put("platform", platform);
		values.put("fedId", fedId);
		values.put("game_uid", game_uid);
		values.put("platform_uid", platform_uid);
		values.put("gender", gender);
		values.put("age", age);
		values.put("version_res", version_res);
		values.put("version_exe", version_exe);
		values.put("device", device);
		values.put("ip", ip);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogLogout(java.lang.String,java
	 * .lang.String,java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogLogout(String fgi, String fedId, String time_spent,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SERVER_LOGOUT_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("fedId", fedId);
		values.put("time_spent", Integer.valueOf(time_spent));
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogCreateRole(java.lang.String
	 * ,java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogCreateRole(String fgi, String serverId, String playerId,
			String fedId, String name, String level, String gender,
			String faction, String camp, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.CREATE_ROLE_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("name", name);
		values.put("level", level);
		values.put("gender", gender);
		values.put("faction", faction);
		values.put("camp", camp);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogRoleLogin(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogRoleLogin(String fgi, String serverId, String playerId,
			String fedId, String name, String level, String gender,
			String faction, String camp, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.ROLE_LOGIN_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("name", name);
		values.put("level", level);
		values.put("gender", gender);
		values.put("faction", faction);
		values.put("camp", camp);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogRoleLogout(java.lang.String
	 * ,java.lang.String,java.lang.String,java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogRoleLogout(String fgi, String serverId, String playerId,
			String fedId, String time_spent, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.ROLE_LOGOUT_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("time_spent", Integer.valueOf(time_spent));
		values.put("playerId", playerId);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogRoleDelete(java.lang.String,
	 * java.lang.String, java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public void addLogRoleDelete(String fgi, String serverId, String playerId,
			String fedId, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.DEL_ROLE_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogChangeRoleLevel(java.lang.
	 * String,java.lang. String,java.lang.String,java.lang. String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogChangeRoleLevel(String fgi, String serverId,
			String playerId, String fedId, String level, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SET_ROLE_LEVEL_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("level", level);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogSetRoleName(java.lang.String,
	 * java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public void addLogSetRoleName(String fgi, String serverId, String playerId,
			String fedId, String playerName, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SET_ROLE_NAME_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("playerName", playerName);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogSetRoleGender(java.lang.String
	 * , java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public void addLogSetRoleGender(String fgi, String serverId,
			String playerId, String fedId, String gender, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SET_ROLE_SEX_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("gender", gender);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogSetRoleFaction(java.lang.String
	 * , java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogSetRoleFaction(String fgi, String serverId,
			String playerId, String fedId, String faction, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SET_ROLE_JOB_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("faction", faction);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogSetRoleCamp(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogSetRoleCamp(String fgi, String serverId, String playerId,
			String fedId, String camp, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SET_ROLE_CAMP_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("camp", camp);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogSetVipLevel(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogSetVipLevel(String fgi, String serverId, String playerId,
			String fedId, String vip, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SET_ROLE_VIP_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("vip", vip);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see com.haowan.logger.service.LogService#addLogSign(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogSign(String fgi, String serverId, String playerId,
			String fedId, String sign, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.ROLE_SIGN_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("sign", sign);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogBindThirdAccount(java.lang
	 * .String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogBindThirdAccount(String fgi, String serverId,
			String playerId, String fedId, String accountType, String account,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.ROLE_BIND_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("accountType", accountType);
		values.put("account", account);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogRoleTransfer(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogRoleTransfer(String fgi, String serverId,
			String playerId, String fedId, String fgi2, String serverId2,
			String playerId2, String fedId2, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.ROLE_TRANSFER_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("fgi2", fgi2);
		values.put("serverId2", serverId2);
		values.put("playerId2", playerId2);
		values.put("fedId2", fedId2);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogRechargeSuccess(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogRechargeSuccess(String fgi, String serverId,
			String playerId, String fedId, String orderId, String amount,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.PAY_BUY_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("orderId", orderId);
		values.put("amount", amount);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogCoinChange(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogCoinChange(String fgi, String serverId, String playerId,
			String fedId, String reasonType, String reasonGroup, String reason,
			String coinType, String coinNum, String left, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.CURRENCY_CHANAGE_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("reasonType", reasonType);
		values.put("reasonGroup", reasonGroup);
		values.put("reason", reason);
		values.put("coinType", coinType);
		values.put("coinNum", coinNum);
		values.put("left", left);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogStoreBuy(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogStoreBuy(String fgi, String serverId, String playerId,
			String fedId, String itemType, String itemId, String itemCnt,
			String coinType, String coinNum, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.STORE_BUY_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("itemType", itemType);
		values.put("itemId", itemId);
		values.put("itemCnt", itemCnt);
		values.put("coinType", coinType);
		values.put("coinNum", coinNum);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogPropsChange(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogPropsChange(String fgi, String serverId, String playerId,
			String fedId, String reasonType, String reasonGroup, String reason,
			String itemType, String itemId, String uniqueId, String itemCnt,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.PROP_CHANAGE_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("reasonType", reasonType);
		values.put("reasonGroup", reasonGroup);
		values.put("reason", reason);
		values.put("itemType", itemType);
		values.put("itemId", itemId);
		values.put("uniqueId", uniqueId);
		values.put("itemCnt", itemCnt);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogReceiveTask(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	// @Override
	// public void addLogReceiveTask(String serverId, String playerId, String
	// taskId, String taskType, String taskOrder,
	// String unixtimestamp) {
	// String currentGame =
	// ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
	//
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("eventId", "U6001");
	// values.put("game", currentGame);
	// values.put("serverId", serverId);
	// values.put("playerId", playerId);
	// values.put("taskId", taskId);
	// values.put("taskType", taskType);
	// values.put("taskOrder", taskOrder);
	// values.put("unixtimestamp", Integer.valueOf(unixtimestamp));
	//
	// this.addLog(values);
	// }
	//
	// /*
	// * @see
	// *
	// com.haowan.logger.service.LogService#addLogTaskComplete(java.lang.String,
	// * java.lang.String, java.lang.String, java.lang.String)
	// */
	// @Override
	// public void addLogTaskComplete(String serverId, String playerId, String
	// taskId, String unixtimestamp) {
	// String currentGame =
	// ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
	//
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("eventId", "U6002");
	// values.put("game", currentGame);
	// values.put("serverId", serverId);
	// values.put("playerId", playerId);
	// values.put("taskId", taskId);
	// values.put("unixtimestamp", Integer.valueOf(unixtimestamp));
	//
	// this.addLog(values);
	// }
	//
	// /*
	// * @see
	// * com.haowan.logger.service.LogService#addLogTaskFail(java.lang.String,
	// * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	// */
	// @Override
	// public void addLogTaskFail(String serverId, String playerId, String
	// taskId, String reason, String unixtimestamp) {
	// String currentGame =
	// ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
	//
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("eventId", "U6003");
	// values.put("game", currentGame);
	// values.put("serverId", serverId);
	// values.put("playerId", playerId);
	// values.put("taskId", taskId);
	// values.put("reason", reason);
	// values.put("unixtimestamp", Integer.valueOf(unixtimestamp));
	//
	// this.addLog(values);
	// }
	//
	// /*
	// * @see
	// * com.haowan.logger.service.LogService#addLogStartLevel(java.lang.String,
	// * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	// */
	// @Override
	// public void addLogStartLevel(String serverId, String playerId, String
	// levelId, String levelOrder,
	// String unixtimestamp) {
	// String currentGame =
	// ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
	//
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("eventId", "U7001");
	// values.put("game", currentGame);
	// values.put("serverId", serverId);
	// values.put("playerId", playerId);
	// values.put("levelId", levelId);
	// values.put("levelOrder", levelOrder);
	// values.put("unixtimestamp", Integer.valueOf(unixtimestamp));
	//
	// this.addLog(values);
	// }
	//
	// /*
	// * @see
	// *
	// com.haowan.logger.service.LogService#addLogLevelComplete(java.lang.String
	// * , java.lang.String, java.lang.String, java.lang.String)
	// */
	// @Override
	// public void addLogLevelComplete(String serverId, String playerId, String
	// levelId, String unixtimestamp) {
	// String currentGame =
	// ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
	//
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("eventId", "U7002");
	// values.put("game", currentGame);
	// values.put("serverId", serverId);
	// values.put("playerId", playerId);
	// values.put("levelId", levelId);
	// values.put("unixtimestamp", Integer.valueOf(unixtimestamp));
	//
	// this.addLog(values);
	// }
	//
	// /*
	// * @see
	// * com.haowan.logger.service.LogService#addLogLevelFail(java.lang.String,
	// * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	// */
	// @Override
	// public void addLogLevelFail(String serverId, String playerId, String
	// levelId, String reason, String unixtimestamp) {
	// String currentGame =
	// ConfigurationReader.getInstance().getString(ConfigurationKey.CURRENT_GAME_ID_KEY);
	//
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("eventId", "U7002");
	// values.put("game", currentGame);
	// values.put("serverId", serverId);
	// values.put("playerId", playerId);
	// values.put("reason", reason);
	// values.put("unixtimestamp", Integer.valueOf(unixtimestamp));
	//
	// this.addLog(values);
	// }

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogGainAssets(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogGainAssets(String fgi, String serverId, String playerId,
			String fedId, String assetsId, String assetsName,
			String assetsType, String assetsLevel, String reason,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.OBTAIN_RES_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("assetsId", assetsId);
		values.put("assetsName", assetsName);
		values.put("assetsType", assetsType);
		values.put("assetsLevel", assetsLevel);
		values.put("reason", reason);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogAssetsLevelUp(java.lang.String
	 * , java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String,java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogAssetsLevelUp(String fgi, String serverId,
			String playerId, String fedId, String assetsId, String assetsName,
			String assetsType, String assetsLevel, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.RES_UPGRADE_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("assetsId", assetsId);
		values.put("assetsName", assetsName);
		values.put("assetsType", assetsType);
		values.put("assetsLevel", assetsLevel);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogLoseAssets(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogLoseAssets(String fgi, String serverId, String playerId,
			String fedId, String assetsId, String assetsName,
			String assetsType, String reason, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.LOSE_RES_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("assetsId", assetsId);
		values.put("assetsName", assetsName);
		values.put("assetsType", assetsType);
		values.put("reason", reason);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogServerOnline(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogServerOnline(String serverId, String concurrent,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId",
				Constants.GameEventPool.SERVER_ONLINE_STATS_EVENT_ID);
		values.put("game", currentGame);
		values.put("serverId", serverId);
		values.put("concurrent", concurrent);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogServerMemoryUsage(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogServerMemoryUsage(String serverId, String ram,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId",
				Constants.GameEventPool.SERVER_MEMORY_USAGE_EVENT_ID);
		values.put("game", currentGame);
		values.put("serverId", serverId);
		values.put("ram", ram);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogServerCPUUsage(java.lang.String
	 * , java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogServerCPUUsage(String serverId, String cpu,
			String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.SERVER_CPU_USAGE_EVENT_ID);
		values.put("game", currentGame);
		values.put("serverId", serverId);
		values.put("cpu", cpu);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/**
	 * 功能描述：采集日志。
	 * 
	 * @param values
	 *            当前日志集合。
	 * @return 日志记录结果。
	 */
	private ErrorCode addLog(Map<String, Object> values) {
		ErrorCode code = ErrorCode.SUCCESS;
                /*
		if (null == values || values.isEmpty()) {
			return ErrorCode.INVALID_PARAMETER_ERROR;
		}

		// 写入当前日志
		try {
			MongoDBHelper.getInstance().addLog(values);
		} catch (Exception e) {
			if (e instanceof MongoException.Network) {
				try {
					backLogCache.put(values);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				code = ErrorCode.CONNECT_DB_FAIL;
			} else {
				code = ErrorCode.SAVE_LOG_ERROR;
			}

			logger.error(code.getDesc(), e);
		}
                */
                logger.error("这里在写入MongoDB日志，由于MongoDB 环境没有配置，写入失败，请配置MongoDB环境");
		return code;
	}

	/**
	 * 功能描述：启动日志监控器。
	 * 
	 */
	private void startLogMoninter() {
		String logFilePath = "game_log";
		String logTempFileName = ConfigurationReader.getInstance().getString(
				ConfigurationKey.BACK_LOG_TEMP_PATH);
		if (null != logTempFileName && !logTempFileName.isEmpty()
				&& !new File(logTempFileName).exists()) {
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.mkdir();
			}

			String fileName = "backup.dat";
			logTempFileName = logFilePath + File.separatorChar + fileName;
		}

		File file = new File(logTempFileName);
		file.setReadable(true);
		file.setWritable(true);

		while (!backLogCache.isEmpty()) {
			// 将日志批量写入临时备份文件
			this.writeLogToFile(file);
		}

		// 数据库连接正常时，将缓存日志写入数据库
		while (file.length() > 0) {
			boolean isConnection = MongoDBHelper.getInstance().ping();
			// 将日志备份文件中的日志写入数据库
			if (isConnection) {
				this.saveLogFromFile(file);
			}
		}
	}

	/**
	 * 功能描述：将日志写入备份文件。
	 * 
	 * @param file
	 *            待写入文件对象。
	 */
	private void writeLogToFile(File file) {
		try {
			// 缓存日志信息
			FileOutputStream fos = new FileOutputStream(file, true);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(backLogCache);

			oos.flush();
			oos.close();
			fos.close();

			backLogCache.clear();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能描述：将日志备份文件中的日志写入数据库。
	 * 
	 * @param file
	 *            当前日志备份文件对象。
	 */
	@SuppressWarnings("unchecked")
	private void saveLogFromFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			LinkedBlockingQueue<Object> backLogList = (LinkedBlockingQueue<Object>) ois
					.readObject();

			boolean check = false;
			try {
				for (int i = 0; i < backLogList.size(); i++) {
					Object obj = backLogList.poll();
					if (obj instanceof BasicDBObject) {
						MongoDBHelper.getInstance().addLog((BasicDBObject) obj);
					} else if (obj instanceof Map) {
						MongoDBHelper.getInstance().addLog(
								(Map<String, Object>) obj);
					}
				}
				check = true;
			} catch (Exception e) {

			} finally {
				ois.close();
				fis.close();
				if (check) {
					file.delete();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogFuncJoin(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogFuncJoin(String fgi, String serverId, String playerId,
			String fedId, String type, String group, String module,
			String order, String unixtimestamp) {
		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.RECEIVE_TASK_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("type", type);
		values.put("group", group);
		values.put("module", module);
		values.put("order", order);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);

	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogFuncComplete(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogFuncComplete(String fgi, String serverId,
			String playerId, String fedId, String type, String group,
			String module, String order, String unixtimestamp) {

		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.TASK_FINISH_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("type", type);
		values.put("group", group);
		values.put("module", module);
		values.put("order", order);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogFuncFailed(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addLogFuncFailed(String fgi, String serverId, String playerId,
			String fedId, String type, String group, String module,
			String order, String reason, String unixtimestamp) {

		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId", Constants.GameEventPool.TASK_FAIL_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("type", type);
		values.put("group", group);
		values.put("module", module);
		values.put("order", order);
		values.put("reason", reason);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#initGameLogger(java.lang.String,
	 * java.lang.String, int, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void initGameLogger(String gameId, String mdbHost, String mdbPort,
			String mdbName, String mdbUserName, String mdbPwd) {
		ConfigurationReader.getInstance().setProperties(gameId, mdbHost,
				mdbPort, mdbName, mdbUserName, mdbPwd);
	}

	/*
	 * @see
	 * com.haowan.logger.service.LogService#addLogPlayerBehavior(java.lang.String
	 * , java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogPlayerBehavior(String fgi, String serverId,
			String playerId, String fedId, String type, String group,
			String event, String time_spent, String order, String param,
			String unixtimestamp) {

		String currentGame = ConfigurationReader.getInstance().getString(
				ConfigurationKey.CURRENT_GAME_ID_KEY);

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventId",
				Constants.GameEventPool.CLIENT_PALYER_BEHAVIOR_EVENT_ID);
		values.put("game", currentGame);
		values.put("fgi", fgi);
		values.put("serverId", serverId);
		values.put("playerId", playerId);
		values.put("fedId", fedId);
		values.put("type", type);
		values.put("group", group);
		values.put("event", event);
		values.put("time_spent", time_spent);
		values.put("order", order);
		values.put("param", param);
		values.put("unixtimestamp", Integer.valueOf(unixtimestamp));

		this.addLog(values);
	}

}
