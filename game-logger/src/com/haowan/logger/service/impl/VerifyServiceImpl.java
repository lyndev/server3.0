package com.haowan.logger.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.haowan.logger.executor.ExecutorServices;
import com.haowan.logger.service.ActiveCodeListener;
import com.haowan.logger.service.ConfirmActiveCodeTask;
import com.haowan.logger.service.DecodeActiveCodeTask;
import com.haowan.logger.service.LoginTask;
import com.haowan.logger.service.OrderTask;
import com.haowan.logger.service.QueryActiveCodeTask;
import com.haowan.logger.service.VerifyService;
import com.haowan.logger.utils.CPHttpResponse;


public class VerifyServiceImpl implements VerifyService {
	
	private static final Logger LOG=Logger.getLogger("VerifyServiceImpl");
	
	//平台的参数值定义
	public class S_FC_PlatformInfo {
		public String func_ClientID; //游戏ＩＤ值
		public String func_access_token; //访问token
		public String func_refresh_token; //刷新token
		public String func_pay_url; //平台查询得到的支付地址
		public String func_auth_url; //平台查询得到的登录验证地址
		public String func_activecode_url; //平台激活码验证地址
		//public String func_log_url; //平台日志系统地址
		public String func_current_userid; //平台当前的用户ＩＤ值
		public String func_current_userName; //渠道当前的用户ＩＤ值
		public String func_platform_name; //渠道的名字定义
		public String func_even_node; //数据中心的节点名字
		public String func_pay_json; //支付的数组列表，给玩家的
		public String fun_pay_callback;
		public String chan_ext_data;
		public String fed_id; //系统唯一标识
	//与游戏相关的

	//初始化平台参数
		public S_FC_PlatformInfo() {
			func_ClientID = "";
			func_access_token = "";
			func_refresh_token = "";
			func_pay_url = "";
			func_auth_url = "";
			func_activecode_url = "";
			//func_log_url = "";
			func_current_userid = "";
			func_current_userName = "";
			func_platform_name = "";
			func_even_node = "";
			func_pay_json = "";
			fun_pay_callback = "";
			chan_ext_data = "";
		}
	}

	private int initTimes = 0;
	private static final int INITTIMES_MAX = 5;
	private static final String EVE_ADDRESS = "http://eve.funcell123.com/config/%s/%s";
	private S_FC_PlatformInfo m_platformInfo = new S_FC_PlatformInfo();
	private static BlockingQueue<String> m_loginResultQueue = new LinkedBlockingQueue<String>();
	private static BlockingQueue<String> m_payResultQueue = new LinkedBlockingQueue<String>();
	private ActiveCodeListener mActiveCodeListener = null;

	public static void addLoginResultQueue(final String result)
	{
		try {
			m_loginResultQueue.put(result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("login result queue put error!!!");
		}
	}
	
	public static void addPayResultQueue(final String result)
	{
		try {
			m_payResultQueue.put(result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("login result queue put error!!!");
		}
	}
	
	@Override
	public boolean initLibrary(String clientId, String platformType, String dataCenterNode) {
		
		return platform_init(clientId, platformType, dataCenterNode);
	}

	@Override
	public void shutdownLibrary() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOrder(String order, String accessToken) {
		// TODO Auto-generated method stub
		ExecutorServices.submit(new OrderTask(m_platformInfo, order, accessToken));
	}

	@Override
	public void addLogin(String accessToken) {
		// TODO Auto-generated method stub
		ExecutorServices.submit(new LoginTask(m_platformInfo, accessToken));
	}
	

	@Override
	public String getLoginResult() {
		// TODO Auto-generated method stub
		String ret;
		try {
			ret = m_loginResultQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		if (ret == null)
			return "";
		else
			return ret;
	}
	
	@Override
	public String getPayResult() {
		// TODO Auto-generated method stub
		String ret;
		try {
			ret = m_payResultQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		if (ret == null)
			return "";
		else
			return ret;
	}
	
	@Override
	public void setActiveCodeListener(ActiveCodeListener listener) {
		// TODO Auto-generated method stub
		mActiveCodeListener = listener;
	}
	
	boolean platform_init(String clientId, String platformType, String dataCenterNode) {
		m_platformInfo.func_ClientID = clientId;
		m_platformInfo.func_even_node = dataCenterNode;
		m_platformInfo.func_platform_name = platformType;
		initTimes = 0;
		return platform_eveInit();
	}
	
	private boolean platform_eveInit() {
		initTimes += 1;

		if (initTimes > INITTIMES_MAX) {
			return false;
		}

		String strUrl = "";
		strUrl = String.format(EVE_ADDRESS, m_platformInfo.func_ClientID, m_platformInfo.func_even_node);
		System.out.print(strUrl);
		
		String strResponse = "";
		try
		{
			strResponse = CPHttpResponse.doSendHttpGetResponse(strUrl);
		} catch (Exception e)
		{
			return false;
		}
		
		if(null==strResponse||strResponse.isEmpty()){
			return false;
		}
		
		JSONTokener jsonParser = new JSONTokener(strResponse);
		JSONObject person;
		try {
			person = (JSONObject) jsonParser.nextValue();
			m_platformInfo.func_pay_url = person.getString("pay");
			m_platformInfo.func_auth_url = person.getString("auth");
			m_platformInfo.func_activecode_url = person.getString("activation");
			if (!m_platformInfo.func_auth_url.isEmpty() && !m_platformInfo.func_pay_url.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			LOG.error("json format error", e);
			return platform_eveInit();
		}
	}
	


	@Override
	public void addDecodeActiveCode(String code, String accessToken,
			String serverId, String playerId) {
		// TODO Auto-generated method stub
		if (mActiveCodeListener == null) {
			LOG.error("请先调用setActiveCodeListener");
			return;
		} else {
			ExecutorServices.submit(new DecodeActiveCodeTask(m_platformInfo, code, accessToken, serverId, playerId, mActiveCodeListener));
		}
	}

	@Override
	public void addQueryActiveCode(String code, String accessToken,
			String serverId, String playerId) {
		// TODO Auto-generated method stub
		if (mActiveCodeListener == null) {
			LOG.error("请先调用setActiveCodeListener");
			return;
		} else {
			ExecutorServices.submit(new QueryActiveCodeTask(m_platformInfo, code, accessToken, serverId, playerId, mActiveCodeListener));
		}
	}

	@Override
	public void addConfirmActiveCode(String code, String accessToken,
			String serverId, String playerId) {
		// TODO Auto-generated method stub
		if (mActiveCodeListener == null) {
			LOG.error("请先调用setActiveCodeListener");
			return;
		} else {
			ExecutorServices.submit(new ConfirmActiveCodeTask(m_platformInfo, code, accessToken, serverId, playerId, mActiveCodeListener));
		}
	}
}
