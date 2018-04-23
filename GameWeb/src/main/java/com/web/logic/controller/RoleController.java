package com.web.logic.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.logic.service.RoleService;

@Controller
public class RoleController {

	private static final Logger logger = Logger.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@ResponseBody
	@RequestMapping(value = "gmlevel", method = RequestMethod.POST)
	public String changeGmLevelList(@RequestParam Map<String, String> map) {
		String userName = map.get("userName");
		String server = map.get("server");
		String level = map.get("level");
		if (userName == null || userName.equals("") || server == null
				|| server.equals("") || level == null || level.equals("")) {
			logger.error("前端传来的参数错误，有空参数。");
			return "参数有为空的字段";
		}

		int serverId = Integer.valueOf(server);
		int gmLevel = Integer.valueOf(level);
		String result = roleService.changeGmLevel(serverId, gmLevel, userName);
		if (result != null) {
			return result;
		}
		return "OK";
	}
	
	@ResponseBody
	@RequestMapping(value = "onlinenum", method = RequestMethod.POST)
	public String getOnlineNum(@RequestParam Map<String, String> map) {
		String server = map.get("server");
		if (server == null || server.equals("") ) {
			logger.error("前端传来的参数错误，有空参数。");
			return "参数有为空的字段";
		}

		int serverId = Integer.valueOf(server);
		String result = roleService.getOnlineNm(serverId);
		if (result != null) {
			return result;
		}
		return "error";
	}
}
