package com.web.logic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.logic.construct.ServerInfo;
import com.web.utils.GMHttpResponse;
import com.web.utils.URLUtils;

@Service("serverService")
public class ServerService {

	private static final Logger logger = Logger.getLogger(ServerService.class);

	private final static String AGENT_URL = "http://123.59.44.65:8089/ServerListDebug.txt";

	/**
	 * 获取服务器列表
	 * @return
	 */
	public List<ServerInfo> getServerList() {
		Map<Integer, ServerInfo> servers = getServerInfos();
		if (servers == null || servers.isEmpty()) {
			return null;
		}
		List<ServerInfo> result = new ArrayList<ServerInfo>();
		for (Entry<Integer, ServerInfo> entry : servers.entrySet()) {
			if (entry.getValue() != null) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	public ServerInfo getServerInfo(int Id) {
		// 先刷新一下，保证是代理上最新的数据
		// this.reloadServerList();
		Map<Integer, ServerInfo> servers = getServerInfos();
		if (servers == null || servers.isEmpty()) {
			return null;
		}
		return servers.get(Id);
	}

	private Map<Integer, ServerInfo> getServerInfos() {
		// 获取json
		String serverStr = GMHttpResponse.doSendHttpGetResponse(AGENT_URL);
		if (serverStr == null || serverStr.equals("")
				|| serverStr.length() == 0) {
			logger.error("从代理服务器获取到的服务器数据为空。url = " + AGENT_URL);
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(serverStr);
		if (!jsonObject.getString("message").equals("success")) {
			logger.error("代理服务状态不对。url = " + AGENT_URL);
			return null;
		}
		Map<Integer, ServerInfo> servers = new HashMap<Integer, ServerInfo>();
		JSONObject serverJson = jsonObject.getJSONObject("data");
		JSONArray jsonArray = serverJson.getJSONArray("list");
		for (Object obj : jsonArray) {
			JSONObject jsonObj = (JSONObject) obj;
			ServerInfo info = new ServerInfo();
			info.fromJson(jsonObj);
			servers.put(info.getServerId(), info);
		}
		return servers;
	}
	
	/**
	 * 停服指令
	 * @param serverId
	 * @return
	 */
	public String stopServer(int serverId){
		ServerInfo serverInfo = getServerInfo(serverId);
		if (serverInfo == null) {
			logger.error("服务器不存在。id=" + serverId);
			return "服务器不存在";
		}
		if (!serverInfo.getStatus().equals("close")
				|| !serverInfo.getStatus().equals("hide")) {
			String url = URLUtils.getUrl(serverInfo.getIp(),
					serverInfo.getHttpPort(), "stopserver", null);
			return GMHttpResponse.doSendHttpGetResponse(url);
		} else {
			logger.warn("IP:" + serverInfo.getIp() + ", port:"
					+ serverInfo.getPort() + ", status:"
					+ serverInfo.getStatus());
		}
		
		return null;
	}
}
// {
// "error_code": "P1111",
// "message": "success",
// "level": "error",
// "data": {
// "list": [
// {
// "name": "agent",
// "status": "open",
// "label": "general",
// "white_key": "",
// "ip": "123.59.44.67",
// "port": "20002",
// "serverid": "1",
// "version": "1.0.0"
// }
// ]
// }
// }

