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

import com.web.logic.construct.Mail;
import com.web.logic.construct.ServerInfo;
import com.web.logic.service.MailService;
import com.web.logic.service.ServerService;

@Controller
public class MailController {

	private static final Logger logger = Logger.getLogger(MailController.class);

	@Autowired
	ServerService serverService;

	@Autowired
	MailService mailService;

	@ResponseBody
	@RequestMapping(value = "sendMail", method = RequestMethod.POST)
	public String getServerList(@RequestParam Map<String, String> map) {
		String serverId = map.get("serverIds");
		String sendType = map.get("type");
		String parameter = map.get("parameter");
		String sendTime = map.get("sendTime");
		String deadTime = map.get("deadTime");
		String title = map.get("title");
		String content = map.get("content");
		String senderName = map.get("senderName");
		String resStr = map.get("resStr");
		String itemStr = map.get("itemStr");
		if (sendType.isEmpty() || sendTime.isEmpty() || deadTime.isEmpty()
				|| title.isEmpty() || content.isEmpty() || senderName.isEmpty()) {
			logger.error("客户端发送回来的数据有错。");
			return "error";
		}
		int type = Integer.valueOf(sendType);

		String[] ids = serverId.split("_");
		if (ids.length == 0) {
			logger.error("发送的服务器列表不存在。");
			return "error";
		}
		List<ServerInfo> servers = new ArrayList<ServerInfo>();
		for (int i = 0; i < ids.length; i++) {
			ServerInfo info = serverService.getServerInfo(Integer
					.valueOf(ids[i]));
			if (info != null) {
				servers.add(info);
			} else {
				logger.error("服务器信息不存在。id = " + ids[i]);
				return "error";
			}
		}

		Mail mail = new Mail(type, parameter, sendTime, deadTime, title,
				content, senderName, resStr, itemStr);

		String result = mailService.sendMail(servers, mail);
		if (result == null || result.isEmpty()) {
			return "error";
		}
		return result;
	}
}
