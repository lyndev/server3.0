package com.web.logic.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.web.logic.construct.ServerInfo;
import com.web.logic.service.ServerService;

@Controller
public class ServerController {
	
	private static final Logger logger = Logger.getLogger(ServerController.class);
	
	@Autowired
	ServerService serverService;
	
	@ResponseBody
	@RequestMapping(value="serverList",method = RequestMethod.POST)
	public String getServerList(){
		List<ServerInfo> list = serverService.getServerList();
		if(list != null && !list.isEmpty()){
			return JSONArray.toJSONString(list);
		}
		return "error";
	}
	
	@ResponseBody
	@RequestMapping(value="stopserver",method = RequestMethod.POST)
	public String stopServer(@RequestParam Map<String, String> map){
		String server = map.get("server");
		if (server == null || server.equals("") ) {
			logger.error("前端传来的参数错误，有空参数。");
			return "参数有为空的字段";
		}
		
		int serverId = Integer.valueOf(server);
		String result = serverService.stopServer(serverId);
		if (result != null) {
			return result;
		}
		return "error";
	}
}	
