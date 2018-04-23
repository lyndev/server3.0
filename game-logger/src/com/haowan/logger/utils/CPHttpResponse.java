package com.haowan.logger.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class CPHttpResponse {
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient customerHttpClient;
	private static final int CONNECTION_TIME_OUT = 30000;
	private static final int SO_TIME_OUT = 20000;
	private static final int TIME_OUT = 10000;

	public static synchronized HttpClient getHttpClient() {
		if (null == customerHttpClient) {
			try {

				HttpParams params = new BasicHttpParams();
				// 设置一些基本参数
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);
				HttpProtocolParams
						.setUserAgent(
								params,
								"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
										+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
				// 超时设置
				/* 从连接池中取连接的超时时间 */
				ConnManagerParams.setTimeout(params, TIME_OUT);
				/* 连接超时 */
				HttpConnectionParams.setConnectionTimeout(params,
						CONNECTION_TIME_OUT);
				/* 请求超时 */
				HttpConnectionParams.setSoTimeout(params, SO_TIME_OUT);

				// 设置我们的HttpClient支持HTTP和HTTPS两种模式
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());

				trustStore.load(null, null);

				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				// schReg.register(new Scheme("https", SSLSocketFactory
				// .getSocketFactory(), 443));
				schReg.register(new Scheme("https", sf, 443));

				// 使用线程安全的连接管理来创建HttpClient
				ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
						params, schReg);
				customerHttpClient = new DefaultHttpClient(conMgr, params);

				//设置重连机制
				((DefaultHttpClient) customerHttpClient)
						.setHttpRequestRetryHandler(new CPHttpRequestRetryHandler());

			} catch (ConnectionPoolTimeoutException e) {// 获取连接超时

			} catch (ConnectTimeoutException e) {// 请求连接超时

			} catch (SocketTimeoutException e) {// 读取超时

			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return customerHttpClient;
	}

	public static String doSendHttpPostResponse(String url,
			HashMap<String, String> postData) {
		HttpClient client = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		InputStream content = null;

		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

		if (postData != null) {
			Set<String> keys = postData.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String rkey = (String) i.next();
				pairs.add(new BasicNameValuePair(rkey, postData.get(rkey)));
			}
		}
		try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					"utf-8");
			httpPost.setEntity(p_entity);
			HttpResponse response = client.execute(httpPost);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			HttpEntity entity = response.getEntity();
			content = entity.getContent();
			String ReturnStr = convertStreamToString(content).trim();
			if (ReturnStr.length() > 0)// 有成功字样就代表充值成功
			{
				return ReturnStr;
			}
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (null != content) {
				try {
					content.close();
					content = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static String doSendHttpGetResponse(String url) {
		HttpClient client = getHttpClient();
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
			if (ReturnStr.length() > 0)// 有成功字样就代表充值成功
			{
				return ReturnStr;
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != content) {
				try {
					content.close();
					content = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 鑾峰緱MD5鎽樿绠楁硶鐨�MessageDigest 瀵硅薄
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 浣跨敤鎸囧畾鐨勫瓧鑺傛洿鏂版憳瑕�
			mdInst.update(btInput);
			// 鑾峰緱瀵嗘枃
			byte[] md = mdInst.digest();
			// 鎶婂瘑鏂囪浆鎹㈡垚鍗佸叚杩涘埗鐨勫瓧绗︿覆褰㈠紡
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	/**
	 * 检查url是否可用
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isHostEnabled(String url) {
		HttpClient client = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == 200) {
				return true;
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean isHttp(String url) {
		return !url.startsWith("https");
	}
}
