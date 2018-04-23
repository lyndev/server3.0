package com.web.logic.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.logic.construct.ServerInfo;
import com.web.utils.GMHttpResponse;
import com.web.utils.URLUtils;

@Service("roleService")
public class RoleService {

	private static final Logger logger = Logger.getLogger(RoleService.class);

	@Autowired
	private ServerService serverService;

	/**
	 * 修改GM等级
	 * 
	 * @param serverId
	 * @param userName
	 * @return
	 */
	public String changeGmLevel(int serverId, int level, String userName) {
		ServerInfo serverInfo = serverService.getServerInfo(serverId);
		if (serverInfo == null) {
			logger.error("服务器不存在。id=" + serverId);
			return "服务器不存在";
		}
		if (!serverInfo.getStatus().equals("close")
				|| !serverInfo.getStatus().equals("hide")) {
			String url = URLUtils.getUrl(serverInfo.getIp(),
					serverInfo.getHttpPort(), "gmlevel", null);
			url = url + "&user=" + userName + "&level=" + level;
			return GMHttpResponse.doSendHttpGetResponse(url);
		} else {
			logger.warn("IP:" + serverInfo.getIp() + ", port:"
					+ serverInfo.getPort() + ", status:"
					+ serverInfo.getStatus());
		}
		return null;
	}
	
	/**
	 * 在线人数统计等级
	 * 
	 * @param serverId
	 * @param userName
	 * @return
	 */
	public String getOnlineNm(int serverId) {
		ServerInfo serverInfo = serverService.getServerInfo(serverId);
		if (serverInfo == null) {
			logger.error("服务器不存在。id=" + serverId);
			return "服务器不存在";
		}
		if (!serverInfo.getStatus().equals("close")
				|| !serverInfo.getStatus().equals("hide")) {
			String url = URLUtils.getUrl(serverInfo.getIp(),
					serverInfo.getHttpPort(), "onlinenum", null);
			return GMHttpResponse.doSendHttpGetResponse(url);
		} else {
			logger.warn("IP:" + serverInfo.getIp() + ", port:"
					+ serverInfo.getPort() + ", status:"
					+ serverInfo.getStatus());
		}
		return null;
	}
}
