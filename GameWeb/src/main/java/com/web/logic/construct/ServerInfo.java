package com.web.logic.construct;

import com.alibaba.fastjson.JSONObject;

public class ServerInfo {

	private int serverId;

	private String name;

	private String ip;

	private int port;
	
	private int httpPort;
	
	private String status; //close-关闭，open-打开，debug-调试，hide-隐藏
	
	private String label; //recommend-推荐，new-新服，hot-火爆，full-爆满，general-般

	public void fromJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			this.serverId = jsonObject.getIntValue("serverid");
			this.name = jsonObject.getString("name");
			this.ip = jsonObject.getString("ip");
			this.port = jsonObject.getIntValue("port");
			this.status = jsonObject.getString("status");
			this.label = jsonObject.getString("label");
			this.httpPort = 8889;
		}
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
