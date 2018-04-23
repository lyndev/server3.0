package com.web.logic.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.web.logic.construct.Mail;
import com.web.logic.construct.ServerInfo;
import com.web.utils.GMHttpResponse;
import com.web.utils.URLUtils;

@Service("mailService")
public class MailService {

	private static final Logger logger = Logger.getLogger(MailService.class);

	@Autowired
	ServerService serverService;

	/**
	 * 加载某个服务器的所有配置表
	 * 
	 * @param serverInfo
	 */
	public String sendMail(List<ServerInfo> servers, Mail mail) {
		if (servers == null || servers.isEmpty()) {
			logger.error("从代理服务器获取游戏服务器列表错误。");
			return "error";
		}
		for (ServerInfo info : servers) {
			// close-关闭，open-打开，debug-调试，hide-隐藏
			if (!info.getStatus().equals("close")
					|| !info.getStatus().equals("hide")) {
				String params = null;
				try {
					params = URLEncoder.encode(JSONObject.toJSONString(mail),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String url = URLUtils.getUrl(info.getIp(), info.getHttpPort(), "sendmail", params);
				return GMHttpResponse.doSendHttpGetResponse(url);
			} else {
				logger.warn("IP:" + info.getIp() + ", port:" + info.getPort()
						+ ", status:" + info.getStatus());
			}
		}
		return "error";
	}

}
