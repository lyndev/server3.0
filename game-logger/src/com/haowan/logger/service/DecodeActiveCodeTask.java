package com.haowan.logger.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.haowan.logger.service.impl.VerifyServiceImpl.S_FC_PlatformInfo;

public class DecodeActiveCodeTask implements Runnable {//doing...

	private String m_accessToken;
	private String m_code;
	private String m_serverId;
	private String m_playerId;
	private S_FC_PlatformInfo m_platformInfo;
	private ActiveCodeListener mListener;
	
	private final static Map<Character,Integer> mapCode = new HashMap<Character, Integer>();

	static{
		mapCode.put('2', 0);
		mapCode.put('d', 1);
		mapCode.put('e', 2);
		mapCode.put('f', 3);
		mapCode.put('4', 4);
		mapCode.put('k', 5);
		mapCode.put('m', 6);
		mapCode.put('n', 7);
		mapCode.put('p', 8);
		mapCode.put('6', 9);
		mapCode.put('7', 10);
		mapCode.put('a', 11);
		mapCode.put('b', 12);
		mapCode.put('r', 13);
		mapCode.put('s', 14);
		mapCode.put('g', 15);
		mapCode.put('5', 16);
		mapCode.put('h', 17);
		mapCode.put('i', 18);
		mapCode.put('3', 19);
		mapCode.put('j', 20);
		mapCode.put('t', 21);
		mapCode.put('c', 22);
		mapCode.put('q', 23);
		mapCode.put('u', 24);
		mapCode.put('v', 25);
		mapCode.put('w', 26);
		mapCode.put('8', 27);
		mapCode.put('x', 28);
		mapCode.put('y', 29);
		mapCode.put('z', 30);
		mapCode.put('9', 31);	
	}
	

	public DecodeActiveCodeTask(S_FC_PlatformInfo platformInfo, String code,
			String accessToken, String serverId, String playerId, ActiveCodeListener listen) {
		m_platformInfo = platformInfo;
		m_code = code;
		m_accessToken = accessToken;
		m_serverId = serverId;
		m_playerId = playerId;
		mListener = listen;
	}

	static int i = 0;

	private static final Logger LOG = Logger.getLogger("DecodeActiveCodeTask");

	//code由月+日+时+分组成，decode检测各位是否符合格式。
	private boolean isCodeValid() {
	
		if (m_code.length() < 5) {
			return false;
		}
		
		for (int i = 0; i < m_code.length(); ++i) {
			if (!mapCode.containsKey(m_code.charAt(i)))
				return false;
		}
		
		int month = mapCode.get(m_code.charAt(0));
		int day = mapCode.get(m_code.charAt(1));
		int hour = mapCode.get(m_code.charAt(2));
		
		int min = mapCode.get(m_code.charAt(3))*10 +  mapCode.get(m_code.charAt(4));
		
		boolean bMonthValid =  (month >= 1 && month <= 12);
		boolean bDayValid = false;
		if (month == 2) {
			bDayValid = (day >= 1 && day <= 29);
		} else {
			bDayValid = (day >= 1 && day <= 31);
		}
		boolean bHourValid = (hour >= 0 && hour <= 23);
		boolean bMinValid = (min >= 0 && min <= 59);
		
		return (bMonthValid && bDayValid && bHourValid && bMinValid);
	}
	
	@Override
	public void run() {
				
		if (isCodeValid()) {
			mListener.decodeActiveCodeCallback("{\"error_code\":\"P1111\"}", m_code, m_accessToken, m_serverId, m_playerId);
			return;
		} else {
			mListener.decodeActiveCodeCallback("{\"error_code\":\"PC1001\"}", m_code, m_accessToken, m_serverId, m_playerId);
			return;
		}
	}

	private String genErrorCodeJson(String errCode) {
		try {
			JSONObject person = new JSONObject();
			
			person.put("error_code", errCode);
			person.put("access_token", m_accessToken);
			person.put("code", m_code);
			person.put("playerid", m_playerId);
			person.put("serverid", m_serverId);
			
			return person.toString();
		} catch (JSONException e) {
			// TODO: handle exception
			LOG.error("response format not json!");
		}
		return "{\"error_code\":\"C1001\"}";
	}


}
