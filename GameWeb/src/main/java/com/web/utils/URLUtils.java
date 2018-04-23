package com.web.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class URLUtils {

	public static String getUrl(String ip, int port, String command,
			String params) {
		// http://ip:port/?command=reloadjavascript&param=1001,1005
		StringBuilder url = new StringBuilder();
		url.append("http://");
		url.append(ip + ":");
		url.append(port + "/?");
		url.append("command=" + command);
		String key = MD5Utils.md5(command);
		url.append("&key=" + key);
		if (params != null && !params.isEmpty()) {
			url.append("&param="+params);
		}
		return url.toString();
	}

	public static void main(String[] args) {
//		String url = getUrl("192.168.1.105", 8889, "reloadgamedata",null);
//		System.err.println(url+"&user=aaaa&level=aaaa");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long now = 1444839225000L;
//		long now = 1444877712313L;
//		long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		System.out.println(now + " = " + formatter.format(calendar.getTime()));
	}
}
