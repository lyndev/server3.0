package com.haowan.logger.service;

public interface ActiveCodeListener {
	
	//激活码回调
	public void decodeActiveCodeCallback(String str, String code, String accessToken, String serverId, String playerId);
	public void queryActiveCodeCallback(String str, String code, String accessToken, String serverId, String playerId);
	public void confirmActiveCodeCallback(String str, String code, String accessToken, String serverId, String playerId);
}
