package com.haowan.logger.service;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.haowan.logger.service.impl.VerifyServiceImpl.S_FC_PlatformInfo;
import com.haowan.logger.utils.CPHttpResponse;

public class ConfirmActiveCodeTask implements Runnable {

	private String m_accessToken;
	private String m_code;
	private String m_serverId;
	private String m_playerId;
	private S_FC_PlatformInfo m_platformInfo;
	private ActiveCodeListener mListener;

	public ConfirmActiveCodeTask(S_FC_PlatformInfo platformInfo, String code,
			String accessToken, String serverId, String playerId, ActiveCodeListener listen) {
		m_platformInfo = platformInfo;
		m_code = code;
		m_accessToken = accessToken;
		m_serverId = serverId;
		m_playerId = playerId;
		mListener = listen;
	}

	static int i = 0;

	private static final Logger LOG = Logger.getLogger("ConfirmActiveCodeTask");

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url = m_platformInfo.func_activecode_url; // doing...
		char c = url.charAt(url.length() - 1);
		if (c == '/' || c == '\\')
			url += "confirm";
		else
			url += "/confirm";
		// string url = "http://192.168.6.56/auth/verify";
		String strResponse = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("access_token", m_accessToken);
		data.put("code", m_code);
		data.put("playerid", m_playerId);
		data.put("serverid", m_serverId);
		
		strResponse = CPHttpResponse.doSendHttpPostResponse(url, data);
		if (null == strResponse || strResponse.isEmpty()) {
			mListener.confirmActiveCodeCallback(genErrorCodeJson("C1001"), m_code, m_accessToken, m_serverId, m_playerId);
			return;
		}

		JSONTokener jsonParser = new JSONTokener(strResponse);
		try {
			Object obj = jsonParser.nextValue();

			if (obj instanceof JSONObject) {
					mListener.confirmActiveCodeCallback(strResponse, m_code, m_accessToken, m_serverId, m_playerId);
					return;
			} else {
				LOG.error("response format not json!");
				mListener.confirmActiveCodeCallback(genErrorCodeJson("J1001"), m_code, m_accessToken, m_serverId, m_playerId);
				return;
			}
		} catch (JSONException e) {
			mListener.confirmActiveCodeCallback(genErrorCodeJson("J1001"), m_code, m_accessToken, m_serverId, m_playerId);
			return;
		}
	}

	private String genErrorCodeJson(String errCode) {
		try {
			JSONObject person = new JSONObject();
			person.put("error_code", errCode);
			return person.toString();
		} catch (JSONException e) {
			// TODO: handle exception
			LOG.error("response format not json!");
			return "{\"error_code\":\"J1001\"}";
		}
	}


}
