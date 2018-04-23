package com.haowan.logger.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.common.platform.config.ConfigurationReader;

public class demo {

	public static void main(String[] args) {

		ConfigurationReader.getInstance().setProperties("", "", "", "", "", "");
		ServicesFactory.getSingleVerifyService().initLibrary(
				"171:18011:test:553", "553", "szdc");
		ServicesFactory.getSingleVerifyService().setActiveCodeListener(
				new ActiveCodeListener() {

					@Override
					public void queryActiveCodeCallback(String str,
							String code, String accessToken, String serverId,
							String playerId) {
						// TODO Auto-generated method stub

					}

					@Override
					public void decodeActiveCodeCallback(String str,
							String code, String accessToken, String serverId,
							String playerId) {
						// TODO Auto-generated method stub
						String abc = str;
						String c = code;

						JSONTokener jsonParser = new JSONTokener(str);
						try {
							Object obj = jsonParser.nextValue();

							if (obj instanceof JSONObject) {
								JSONObject person = (JSONObject) obj;
								if (person.get("error_code").equals("P1111")) {
									ServicesFactory.getSingleVerifyService()
											.addConfirmActiveCode(code,
													accessToken, serverId,
													playerId);
									return;
								} else {
									System.out.println("???decode error: " + code
											+ "    " + str);
									return;
								}
							}
						} catch (JSONException e) {
							return;
						}
					}

					@Override
					public void confirmActiveCodeCallback(String str,
							String code, String accessToken, String serverId,
							String playerId) {
						// TODO Auto-generated method stub

						System.out.println("confirm" + str);
						return;
					}
				});

		String code = "";
		for (int i = 0; i < 100000000; ++i) {
			code = "";
			for (int j = 0; j < 8; ++j) {

				int num = (int) (Math.random() * 31);
				Object[] cArry = mapCode.keySet().toArray();
				code += (char) cArry[num];
			}
			ServicesFactory
					.getSingleVerifyService()
					.addDecodeActiveCode(
							code,
							"MTcxOjE4MDExOnRlc3Q6NTUzLDU1MywyNUFCREQ5My02ODVELTRFNzMtQjQ5Qy1GOTBBRTczODZGNUIsMTQxNTI3Nzk5NSwyOWYyZTM4MC01ZGFhLTEyYmYtNWU3MC0yOTA1OWFlNWZkMGIsYXV0aHw5NzY4ZTNiYzYxY2I2MWRiOTJiYzk4ZjA2ZDI0Zjg5MzUwNzZmNzk2",
							"1", "1");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final static Map<Character, Integer> mapCode = new HashMap<Character, Integer>();

	static {
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
}
