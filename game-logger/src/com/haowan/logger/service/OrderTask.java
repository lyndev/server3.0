package com.haowan.logger.service;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.haowan.logger.service.impl.VerifyServiceImpl;
import com.haowan.logger.service.impl.VerifyServiceImpl.S_FC_PlatformInfo;
import com.haowan.logger.utils.CPHttpResponse;

public class OrderTask implements Runnable {

	private String m_accessToken;
	private String m_order;
	private S_FC_PlatformInfo m_platformInfo;

	public OrderTask(S_FC_PlatformInfo platformInfo, String order,
			String accessToken) {
		m_platformInfo = platformInfo;
		m_order = order;
		m_accessToken = accessToken;
	}

	static int i = 0;

	private static final Logger LOG = Logger.getLogger("OrderTask");

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url = m_platformInfo.func_pay_url; // doing...
		char c = url.charAt(url.length() - 1);
		if (c == '/' || c == '\\')
			url += "confirm";
		else
			url += "/confirm";
		// string url = "http://192.168.6.56/auth/verify";
		String strResponse = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("access_token", m_accessToken);
		data.put("action", "handle");
		String orderJson = "";
		orderJson = String.format("[\"%s\"]", m_order);
		data.put("f_orders", orderJson);

		strResponse = CPHttpResponse.doSendHttpPostResponse(url, data);
		if (null == strResponse || strResponse.isEmpty()) {
			VerifyServiceImpl.addPayResultQueue(genErrorCodeJson("C1001"));
		}

		JSONTokener jsonParser = new JSONTokener(strResponse);
		try {
			Object obj = jsonParser.nextValue();

			if (obj instanceof JSONObject) {
				JSONObject person = (JSONObject) obj;
				if (person.isNull("error_code")) {
					VerifyServiceImpl.addPayResultQueue(strResponse);
				} else {
					person.put("access_token", m_accessToken);
					person.put("orderid", m_order);
					VerifyServiceImpl.addPayResultQueue(person.toString());
				}
			} else if (obj instanceof JSONArray) {
				JSONArray array = (JSONArray) obj;
				if (array.length() > 0) {
					Object personObj = array.get(0);
					if (personObj instanceof JSONObject) {
						JSONObject person = (JSONObject) personObj;
						if (person.isNull("error_code")) {
							VerifyServiceImpl.addPayResultQueue(strResponse);
						} else {
							person.put("access_token", m_accessToken);
							person.put("orderid", m_order);
							VerifyServiceImpl.addPayResultQueue(obj.toString());
						}
					} else {
						LOG.error("response format not json!");
					}
				} else {
					LOG.error("response format not json!");
				}
			} else {
				LOG.error("response format not json!");
			}
		} catch (JSONException e) {
			VerifyServiceImpl.addPayResultQueue(genErrorCodeJson("J1001"));
		}
	}

	private String genErrorCodeJson(String errCode) {
		try {
			JSONObject person = new JSONObject();
			person.put("access_token", m_accessToken);
			person.put("orderid", m_order);
			person.put("error_code", errCode);
			return person.toString();
		} catch (JSONException e) {
			// TODO: handle exception
			LOG.error("response format not json!");
		}
		return "{\"error_code\":\"C1001\"}";
	}


}
