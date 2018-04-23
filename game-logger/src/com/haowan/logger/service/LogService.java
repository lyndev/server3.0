package com.haowan.logger.service;

import org.json.JSONException;

/**
 * 功能描述：<code>LogService</code>用于定义游戏日志采集相关服务接口。
 * <p>
 * 
 * @author andy.zheng 
 * @version 1.0, 2014年6月17日 上午10:39:03
 * @since Common-Platform/ 1.0
 */
public interface LogService {

	/**
	 * 功能描述：游戏日志采集通用接口。
	 * <p>
	 * 支持自定义游戏事件。
	 * 
	 * 
	 * @param totalJson
	 *            以JSON格式组织的游戏事件信息。
	 * @throws JSONException
	 */
	public void addCommonLog1(String totalJson) throws JSONException;

	/**
	 * 功能描述：游戏日志采集通用接口。
	 * <p>
	 * 支持自定义游戏事件。
	 * 
	 * @param eventId
	 *            当前事件ID。
	 * @param eventJson
	 *            以JSON格式组织的游戏事件信息。
	 * @throws JSONException
	 */
	public void addCommonLog2(String eventId, String eventJson)
			throws JSONException;

	/**
	 * 功能描述：采集用户登录日志。
	 * 
	 * @param client
	 *            客户端ID。
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param platform
	 *            渠道标志。
	 * @param fedId
	 *            好玩平台用户唯一标识，由登陆验证verfiy返回account字段。
	 * @param game_uid
	 *            游戏内部账户唯一标识（如果没有可不传，有则必须传 注：建议游戏内部也可以直接使用fedId作为玩家帐号识别标识)
	 * @param platform_uid
	 *            platform_uid：渠道(如UC,91)用户标识(客户端获取)。
	 * @param gender
	 *            玩家性别。
	 * @param age
	 *            玩家年龄。
	 * @param version_res
	 *            游戏资源版本。
	 * @param version_exe
	 *            游戏执行端版本。
	 * @param device
	 *            唯一设备ID。
	 * @param ip
	 *            IP地址。
	 * @param unixtimestamp
	 *            登录时间戳.(以秒为单位)
	 */
	public void addLogLogin(String client, String fgi, String platform,
			String fedId, String game_uid, String platform_uid, String gender,
			String age, String version_res, String version_exe, String device,
			String ip, String unixtimestamp);

	/**
	 * 功能描述：采集用户登出日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param fedId
	 *            用户ID，全局多设备间玩家唯一标识。
	 * @param time_spent
	 *            本次在线时长。(以秒为单位)
	 * @param unixtimestamp
	 *            用户登出时间戳.(以秒为单位)
	 */
	public void addLogLogout(String fgi, String fedId, String time_spent,
			String unixtimestamp);

	/**
	 * 功能描述：采集用户创建角色日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID,对于无区服游戏,请统一设置一个GSID,如:0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            好玩平台用户唯一标识，由登录验证verfiy返回account字段
	 * @param name
	 *            角色名称。
	 * @param level
	 *            角色等级。一般请填1
	 * @param gender
	 *            角色性别。请填中文"男"和"女",没有请留空
	 * @param faction
	 *            角色职业。推荐直接填中文,没有请留空
	 * @param camp
	 *            阵营/国家。推荐直接填中文,没有请留空
	 * @param unixtimestamp
	 *            角色创建时间戳.(以秒为单位)
	 */
	public void addLogCreateRole(String fgi, String serverId, String playerId,
			String fedId, String name, String level, String gender,
			String faction, String camp, String unixtimestamp);

	/**
	 * 功能描述：采集角色登录日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色唯一识别标识。(无角色ID则设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param name
	 *            角色名称。
	 * @param level
	 *            角色等级,一般请填1
	 * @param gender
	 *            角色性别,请填中文"男"和"女",没有请留空
	 * @param faction
	 *            角色职业,推荐直接填中文,没有请留空
	 * @param camp
	 *            阵营/国家,推荐直接填中文,没有请留空
	 * @param unixtimestamp
	 *            角色登录时间戳.(以秒为单位)
	 */
	public void addLogRoleLogin(String fgi, String serverId, String playerId,
			String fedId, String name, String level, String gender,
			String faction, String camp, String unixtimestamp);

	/**
	 * 功能描述：采集角色登出日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            好玩平台用户唯一标识，由登录验证verfiy返回account字段
	 * @param time_spent
	 *            本次在线时长.(以秒为单位)
	 * @param unixtimestamp
	 *            角色登出时间戳.(以秒为单位)
	 */
	public void addLogRoleLogout(String fgi, String serverId, String playerId,
			String fedId, String time_spent, String unixtimestamp);

	/**
	 * 功能描述： 采集角色删除日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            好玩平台用户唯一标识，由登录验证verfiy返回account字段
	 * @param unixtimestamp
	 *            角色删除时间戳.(以秒为单位)
	 */
	public void addLogRoleDelete(String fgi, String serverId, String playerId,
			String fedId, String unixtimestamp);

	/**
	 * 功能描述：采集角色等级更改日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            好玩平台用户唯一标识，由登录验证verfiy返回account字段
	 * @param level
	 *            角色等级。
	 * @param unixtimestamp
	 *            角色等级修改时间戳.(以秒为单位)
	 */
	public void addLogChangeRoleLevel(String fgi, String serverId,
			String playerId, String fedId, String level, String unixtimestamp);

	/**
	 * 功能描述：采集角色名设置的日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param playerName
	 *            角色名。
	 * @param unixtimestamp
	 *            角色名设置时间戳.(以秒为单位)
	 */
	public void addLogSetRoleName(String fgi, String serverId, String playerId,
			String fedId, String playerName, String unixtimestamp);

	/**
	 * 功能描述：采集角色性别设置的日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param gender
	 *            角色性别，请填中文“男”/“女”
	 * @param unixtimestamp
	 *            角色性别设置时间戳.(以秒为单位)
	 */
	public void addLogSetRoleGender(String fgi, String serverId,
			String playerId, String fedId, String gender, String unixtimestamp);

	/**
	 * 功能描述：采集角色职业设置日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param faction
	 *            角色职业,推荐直接填中文
	 * @param unixtimestamp
	 *            角色职业设置时间戳.(以秒为单位)
	 */
	public void addLogSetRoleFaction(String fgi, String serverId,
			String playerId, String fedId, String faction, String unixtimestamp);

	/**
	 * 功能描述：采集角色阵营设置日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param camp
	 *            角色阵营,推荐直接填中文
	 * @param unixtimestamp
	 *            角色阵营设置时间戳.(以秒为单位)
	 */
	public void addLogSetRoleCamp(String fgi, String serverId, String playerId,
			String fedId, String camp, String unixtimestamp);

	/**
	 * 功能描述：采集VIP等级设置日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param vip
	 *            VIP等级。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogSetVipLevel(String fgi, String serverId, String playerId,
			String fedId, String vip, String unixtimestamp);

	/**
	 * 功能描述：采集用户签到日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param sign
	 *            连续签到天数。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogSign(String fgi, String serverId, String playerId,
			String fedId, String sign, String unixtimestamp);

	/**
	 * 功能描述：采集用户绑定第三方账号的日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param accountType
	 *            第三方账号类型(电话/QQ/微信/facebook)
	 * @param account
	 *            第三方账号。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogBindThirdAccount(String fgi, String serverId,
			String playerId, String fedId, String accountType, String account,
			String unixtimestamp);

	/**
	 * 功能描述：采集角色转移日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            转移前的区服ID。
	 * @param playerId
	 *            转移前的角色ID。
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param fgi2
	 *            FGI。
	 * @param serverId2
	 *            转移后的区服ID。
	 * @param playerId2
	 *            转移后的角色ID。
	 * @param fedId2
	 *            转移后的fedID.
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogRoleTransfer(String fgi, String serverId,
			String playerId, String fedId, String fgi2, String serverId2,
			String playerId2, String fedId2, String unixtimestamp);

	/**
	 * 功能描述：采集用户充值购买日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param orderId
	 *            充值订单ID。
	 * @param amount
	 *            充值金额。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogRechargeSuccess(String fgi, String serverId,
			String playerId, String fedId, String orderId, String amount,
			String unixtimestamp);

	/**
	 * 功能描述：采集用户货币变化日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param reasonType
	 *            玩家货币变化原因的类型（大类），推荐填写中文
	 * @param reasonGroup
	 *            玩家货币变化原因的分组（小类），推荐填写中文
	 * @param reason
	 *            玩家获得货币原因。
	 * @param coinType
	 *            货币类型。
	 * @param coinNum
	 *            玩家货币变化数量。
	 * @param left
	 *            玩家剩余虚拟币数量。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogCoinChange(String fgi, String serverId, String playerId,
			String fedId, String reasonType, String reasonGroup, String reason,
			String coinType, String coinNum, String left, String unixtimestamp);

	/**
	 * 功能描述：采集用户商城购买日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)。
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param itemType
	 *            道具类型。
	 * @param itemId
	 *            道具ID。
	 * @param itemCnt
	 *            购买道具数量。
	 * @param coinType
	 *            购买道具使用的货币类型。
	 * @param coinNum
	 *            购买道具消耗的货币数量。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogStoreBuy(String fgi, String serverId, String playerId,
			String fedId, String itemType, String itemId, String itemCnt,
			String coinType, String coinNum, String unixtimestamp);

	/**
	 * 功能描述：采集道具变化日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param reasonType
	 *            玩家货币变化原因的类型（大类），推荐填写中文
	 * @param reasonGroup
	 *            玩家货币变化原因的分组（小类），推荐填写中文
	 * @param reason
	 *            获得道具原因。
	 * @param itemType
	 *            道具类型。
	 * @param itemId
	 *            道具ID/名称。
	 * @param uniqueId：道具唯一ID，如果没有请留空。
	 * @param itemCnt
	 *            获得道具数量（正整数为产生，负整数为消耗）。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogPropsChange(String fgi, String serverId, String playerId,
			String fedId, String reasonType, String reasonGroup, String reason,
			String itemType, String itemId,String uniqueId, String itemCnt, String unixtimestamp);

	// /**
	// * 功能描述：采集用户接受任务日志。
	// *
	// * @param fgi 游戏和渠道的关联ID。
	// * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	// * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
	// * @param taskId 任务ID。
	// * @param taskType 任务类型。
	// * @param taskOrder 任务序号。
	// * @param unixtimestamp 时间戳.(以秒为单位)
	// */
	// public void addLogReceiveTask(String fgi,String serverId, String
	// playerId, String taskId, String taskType, String taskOrder, String
	// unixtimestamp);
	//
	// /**
	// * 功能描述：采集用户任务完成日志。
	// *
	// * @param fgi 游戏和渠道的关联ID。
	// * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	// * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
	// * @param taskId 任务ID。
	// * @param unixtimestamp 时间戳.(以秒为单位)
	// */
	// public void addLogTaskComplete(String fgi,String serverId, String
	// playerId, String taskId, String unixtimestamp);
	//
	// /**
	// * 功能描述：采集任务失败日志。
	// *
	// * @param fgi 游戏和渠道的关联ID。
	// * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	// * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
	// * @param taskId 任务ID。
	// * @param reason 任务失败原因。
	// * @param unixtimestamp 时间戳.(以秒为单位)
	// */
	// public void addLogTaskFail(String fgi,String serverId, String playerId,
	// String taskId, String reason, String unixtimestamp);
	//
	// /**
	// * 功能描述：采集用户开始关卡日志。
	// *
	// * @param fgi 游戏和渠道的关联ID。
	// * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	// * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
	// * @param levelId 关卡名/ID.
	// * @param levelOrder 关卡序号。
	// * @param unixtimestamp 时间戳.(以秒为单位)
	// */
	// public void addLogStartLevel(String fgi,String serverId, String playerId,
	// String levelId, String levelOrder, String unixtimestamp);
	//
	// /***
	// * 功能描述：采集用户关卡完成日志。
	// *
	// * @param fgi 游戏和渠道的关联ID。
	// * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	// * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
	// * @param levelOrder 关卡序号。
	// * @param unixtimestamp 时间戳.(以秒为单位)
	// */
	// public void addLogLevelComplete(String fgi,String serverId, String
	// playerId, String levelId, String unixtimestamp);
	//
	// /**
	// * 功能描述：采集用户关卡失败日志。
	// *
	// * @param fgi 游戏和渠道的关联ID。
	// * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	// * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
	// * @param levelId 关卡名/ID.
	// * @param reason 关卡失败原因。
	// * @param unixtimestamp 时间戳.(以秒为单位)
	// */
	// public void addLogLevelFail(String fgi,String serverId, String playerId,
	// String levelId, String reason, String unixtimestamp);

	/**
	 * 功能描述：采集用户获得资产日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param assetsId
	 *            资源唯一ID。
	 * @param assetsName
	 *            资源名称,推荐填写中文
	 * @param assetsType
	 *            资源类型,推荐填写中文。如：宠物/坐骑/卡牌/英雄等
	 * @param assetsLevel
	 *            资源等级,如分X阶X星 请用小数表示
	 * @param reason
	 *            获得原因,推荐填写中文
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogGainAssets(String fgi, String serverId, String playerId,
			String fedId, String assetsId, String assetsName,
			String assetsType, String assetsLevel, String reason,
			String unixtimestamp);

	/**
	 * 功能描述：采集用户资产升级日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param assetsId
	 *            资源唯一ID。
	 * @param assetsName
	 *            资源名称。
	 * @param assetsType
	 *            资源类型,推荐填写中文。如：宠物/坐骑/卡牌/英雄等
	 * @param assetsLevel
	 *            资源等级，推荐填写中文
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogAssetsLevelUp(String fgi, String serverId,
			String playerId, String fedId, String assetsId, String assetsName,
			String assetsType, String assetsLevel, String unixtimestamp);

	/**
	 * 功能描述：采集用户失去资产日志。
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param assetsId
	 *            资源唯一ID。
	 * @param assetsName
	 *            资源名称。
	 * @param assetsType
	 *            资源类型,推荐填写中文。如：宠物/坐骑/卡牌/英雄等
	 * @param reason
	 *            失去原因。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogLoseAssets(String fgi, String serverId, String playerId,
			String fedId, String assetsId, String assetsName,
			String assetsType, String reason, String unixtimestamp);

	/**
	 * 功能描述：采集服务器在线统计日志。
	 * 
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param concurrent
	 *            在线数量。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogServerOnline(String serverId, String concurrent,
			String unixtimestamp);

	/**
	 * 功能描述：采集服务器内存占用日志。
	 * 
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param ram
	 *            内存占用率。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogServerMemoryUsage(String serverId, String ram,
			String unixtimestamp);

	/**
	 * 功能描述：采集服务器CPU占用日志。
	 * 
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param cpu
	 *            CPU占用率。
	 * @param unixtimestamp
	 *            时间戳.(以秒为单位)
	 */
	public void addLogServerCPUUsage(String serverId, String cpu,
			String unixtimestamp);

	/**
	 * 
	 * 功能描述： 采集玩家接受任务、参与活动、开始关卡、使用功能日志
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param type
	 *            类型ID或者类型名称,推荐填写中文类型名称
	 * @param group
	 *            分组ID或者分组名,推荐填写中文分组名
	 * @param module
	 *            功能模块ID或者功能模块名称,推荐填写中文功能模块名称
	 * @param order
	 *            序号,用于数据展现时排序
	 * @param unixtimestamp
	 *            时间戳(以秒为单位)
	 */
	public void addLogFuncJoin(String fgi, String serverId, String playerId,
			String fedId, String type, String group, String module,
			String order, String unixtimestamp);

	/**
	 * 
	 * 功能描述： 采集玩家完成任务、完成活动、完成关卡日志
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param type
	 *            类型ID或者类型名称,推荐填写中文类型名称
	 * @param group
	 *            分组ID或者分组名,推荐填写中文分组名
	 * @param module
	 *            功能模块ID或者功能模块名称,推荐填写中文功能模块名称
	 * @param order
	 *            序号,用于数据展现时排序
	 * @param unixtimestamp
	 *            时间戳(以秒为单位)
	 */
	public void addLogFuncComplete(String fgi, String serverId,
			String playerId, String fedId, String type, String group,
			String module, String order, String unixtimestamp);

	/**
	 * 
	 * 功能描述： 采集玩家任务失败，关卡失败日志
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID,如：0
	 * @param playerId
	 *            角色编号(无角色ID的游戏请设置为game_uid)
	 * @param fedId
	 *            用户ID，由登录验证verfiy返回account字段
	 * @param type
	 *            类型ID或者类型名称,推荐填写中文类型名称
	 * @param group
	 *            分组ID或者分组名,推荐填写中文分组名
	 * @param module
	 *            功能模块ID或者功能模块名称,推荐填写中文功能模块名称
	 * @param order
	 *            序号,用于数据展现时排序
	 * @param reason
	 *            任务失败原因,推荐填写中文原因
	 * @param unixtimestamp
	 *            时间戳(以秒为单位)
	 */
	public void addLogFuncFailed(String fgi, String serverId, String playerId,
			String fedId, String type, String group, String module,
			String order, String reason, String unixtimestamp);

	/**
	 * 
	 * 功能描述：游戏日志系统初始化(推荐在系统启动切先于其他接口调用)
	 * 
	 * @param gameId
	 *            游戏ID
	 * @param mdbHost
	 *            host
	 * @param mdbPort
	 *            port
	 * @param mdbName
	 *            db name
	 * @param mdbUserName
	 *            username
	 * @param mdbPwd
	 *            password
	 */
	public void initGameLogger(String gameId, String mdbHost, String mdbPort,
			String mdbName, String mdbUserName, String mdbPwd);

	/**
	 * 
	 * 功能描述：玩家行为统计，包括行为概况、行为跟踪
	 * 
	 * @param fgi
	 *            游戏和渠道的关联ID。
	 * @param serverId
	 *            区服ID，对于无区服游戏，请统一设置一个GSID，如：0
	 * @param playerId
	 *            角色唯一识别标志(无角色ID的游戏请设置为accountId)
	 * @param fedId
	 *            全局多设备间唯一玩家识别标志，由登陆验证服务返回
	 * @param type
	 *            （大分类）类型Id或者类型名称，推荐填写中文类型名称
	 * @param group
	 *            （小分类）分组Id或者分组名，推荐填写中文分组名
	 * @param event
	 *            事件或模块名称，推荐填写中文
	 * @param time_spent
	 *            耗费时长（以秒为单位）
	 * @param order
	 *            序号，用于数据展现时排序
	 * @param param
	 *            其他参数（JSON格式）
	 * @param unixtimestamp
	 *            时间戳（以秒为单位）
	 */
	public void addLogPlayerBehavior(String fgi, String serverId,
			String playerId, String fedId, String type, String group,
			String event, String time_spent, String order, String param,
			String unixtimestamp);
}
