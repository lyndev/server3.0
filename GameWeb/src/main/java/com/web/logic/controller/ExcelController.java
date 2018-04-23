package com.web.logic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.logic.construct.ServerInfo;
import com.web.logic.service.ExcelService;
import com.web.logic.service.ServerService;

@Controller
public class ExcelController {

	private static final Logger logger = Logger
			.getLogger(ExcelController.class);

	@Autowired
	ServerService serverService;
	
	@Autowired
	ExcelService excelService;

	@ResponseBody
	@RequestMapping(value = "reloadExcel", method = RequestMethod.POST)
	public String getServerList(@RequestParam Map<String, String> map) {
		String sendType = map.get("sendType"); // 1-所有服务器，2-指定的服务器
		String result = null;
		if (sendType.equals("1")) {
			result = excelService.reloadAllServerExcel();
		} else if (sendType.equals("2")) {
			String strServer = map.get("servers");
			if (strServer == null || strServer.equals("")) {
				logger.error("前端未传服务器列表参数过来。");
				return "error";
			}
			String[] ids = strServer.split("_");
			if (ids == null || ids.length == 0) {
				logger.error("前端传过来的服务器列表参数有错。");
				return "error";
			}
			List<ServerInfo> serverInfos = new ArrayList<ServerInfo>();
			for (int i = 0; i < ids.length; i++) {
				ServerInfo info = serverService.getServerInfo(Integer
						.valueOf(ids[i]));
				if (info != null) {
					serverInfos.add(info);
				} else {
					logger.error("服务器信息不存在。id = " + ids[i]);
					return "error";
				}
			}
			result = excelService.reloadAllExcel(serverInfos);
		}
		if (result == null || result.isEmpty()) {
			return "error";
		}
		return result;
	}
}
