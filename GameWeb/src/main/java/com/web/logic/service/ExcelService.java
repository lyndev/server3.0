package com.web.logic.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.logic.construct.ServerInfo;
import com.web.utils.GMHttpResponse;
import com.web.utils.URLUtils;

@Service("excelService")
public class ExcelService {

	private static final Logger logger = Logger.getLogger(ExcelService.class);

	@Autowired
	ServerService serverService;
	
	/**
	 * 加载所有服务器配置表
	 */
	public String reloadAllServerExcel() {
		List<ServerInfo> servers = serverService.getServerList();
		return reloadAllExcel(servers);
	}

	/**
	 * 加载某个服务器的所有配置表
	 * 
	 * @param serverInfo
	 */
	public String reloadAllExcel(List<ServerInfo> servers) {
		
		if (servers == null || servers.isEmpty()) {
			logger.error("从代理服务器获取游戏服务器列表错误。");
			return "从代理获取服务器列表错误";
		}
		for (ServerInfo info : servers) {
			// close-关闭，open-打开，debug-调试，hide-隐藏
			if (!info.getStatus().equals("close")
					|| !info.getStatus().equals("hide")) {
				String url = URLUtils.getUrl(info.getIp(), info.getHttpPort(), "reloadgamedata", null);
				return GMHttpResponse.doSendHttpGetResponse(url);
			} else {
				logger.warn("IP:" + info.getIp() + ", port:" + info.getPort()
						+ ", status:" + info.getStatus());
			}
		}
		return "error";
	}

}
