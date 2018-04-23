package com.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

@SuppressWarnings("deprecation")
public class GMHttpResponse {

	private static final Logger logger = Logger.getLogger(GMHttpResponse.class);

	// private static synchronized HttpClient getHttpClient() {
	// return new DefaultHttpClient();
	// }

	/**
	 * post 请求（暂时不用）
	 * 
	 * @param url
	 * @param postData
	 *            参数
	 * @return
	 */
	@SuppressWarnings({ "unused", "resource" })
	private static String doSendHttpPostResponse(String url,
			HashMap<String, String> postData) {
		logger.info("游戏后台POST请求url："+url+", data = "+postData.toString());
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		InputStream content = null;
		// 创建参数队列

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		if (postData != null) {
			Set<String> keys = postData.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String rkey = (String) i.next();
				formparams
						.add(new BasicNameValuePair(rkey, postData.get(rkey)));
			}
		}

		UrlEncodedFormEntity uefEntity;
		HttpClient httpClient = null;
		try {

			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);

			HttpResponse response;
			httpClient = new DefaultHttpClient();
			response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				content = entity.getContent();
				String ReturnStr = convertStreamToString(content);
				if (ReturnStr.length() > 0)// 有成功字样就代表充值成功
				{
					return ReturnStr;
				}
			}

		} catch (ClientProtocolException e) {
			logger.error(e, e);
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1, e1);
		} catch (IOException e) {
			logger.error(e, e);
		} finally {

			// 关闭连接,释放资源
			if (content != null) {
				try {
					content.close();
					content = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e, e);
				}
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return null;
	}

	/**
	 * Get 请求
	 * 
	 * @param url
	 * @return
	 */
	public static String doSendHttpGetResponse(String url) {
		logger.info("游戏后台GET请求url："+url);
		@SuppressWarnings("resource")
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		InputStream content = null;
		try {
			HttpResponse response = client.execute(httpGet);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			HttpEntity entity = response.getEntity();
			content = entity.getContent();
			String ReturnStr = convertStreamToString(content).trim();
			if (ReturnStr.length() > 0) {
				return ReturnStr;
			}

//		} catch (IllegalStateException e) {
//			logger.error(e, e);
		} catch (IOException e) {
			logger.error(e, e);
			return "未能连接到游戏服务，可能是未开启";
		} finally {
			if (null != content) {
				try {
					content.close();
					content = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e, e);
				}
			}
			if (client != null)
				client.getConnectionManager().shutdown();
		}

		return null;
	}

	// 读取字节流，输出为字符串
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error(e, e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error(e, e);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String url = URLUtils.getUrl("192.168.1.105", 8889, "onlinenum", null);
		System.err.println(doSendHttpGetResponse(url));
	}

}
