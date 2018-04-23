package com.haowan.logger.service;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.haowan.logger.service.impl.VerifyServiceImpl;
import com.haowan.logger.service.impl.VerifyServiceImpl.S_FC_PlatformInfo;
import com.haowan.logger.utils.CPHttpResponse;

public class LoginTask implements Runnable {

	private S_FC_PlatformInfo m_platformInfo;
	private String m_accessToken;
	private static final Logger LOG = Logger.getLogger("LoginTask");

	public LoginTask(S_FC_PlatformInfo info, String accessToken) {
		m_platformInfo = info;
		m_accessToken = accessToken;
	}

	static int i = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub

		String url = m_platformInfo.func_auth_url;
		char c = url.charAt(url.length() - 1);
		if (c == '/' || c == '\\')
			url += "verify";
		else
			url += "/verify";
		// string url = "http://192.168.6.56/auth/verify";
		String strResponse = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("access_token", m_accessToken);
		try {
			strResponse = CPHttpResponse.doSendHttpPostResponse(url, data);
		} catch (Exception e) {
			LOG.error("platform login verify request error!: " + (++i));
		}
		if(null==strResponse||strResponse.isEmpty()){
			VerifyServiceImpl.addLoginResultQueue(genErrorCodeJson("C1001"));
		}

		JSONTokener jsonParser = new JSONTokener(strResponse);
		JSONObject person;
		try {
			person = (JSONObject) jsonParser.nextValue();
			if (person.isNull("error_code")) {
				VerifyServiceImpl.addLoginResultQueue(strResponse);
			} else {
				person.put("access_token", m_accessToken);
				VerifyServiceImpl.addLoginResultQueue(person.toString());
			}
		} catch (JSONException e) {
			// TODO: handle exception
			VerifyServiceImpl.addLoginResultQueue(genErrorCodeJson("J1001"));
		}
	}
	
	private String genErrorCodeJson(String errCode)
	{
		try
		{
			JSONObject person = new JSONObject();
			person.put("access_token", m_accessToken);
			person.put("error_code", errCode);
			return person.toString();
		} catch (JSONException e)
		{
		// TODO: handle exception
			LOG.error("response format not json!");
		}
		return "{\"error_code\":\"C1001\"}";
	}
}
