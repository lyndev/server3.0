package com.haowan.logger.service;

/**
 * 功能描述：<code>VerifyService</code>用于定义用户认证相关服务接口。
 * <p>
 *
 * @author   zhou
 * @version  1.0, 2014年6月17日 上午10:41:00
 * @since    Common-Platform/ 1.0
 */
public interface VerifyService {

    /**
     * 
     * 功能描述：初始化认证服务。
     *
     * @param clientId          客户端ID。
     * @param platformType      渠道名。
     * @param dataCenterNode    数据中心.
     * @return                  初始化是否成功。
     */
	public boolean initLibrary(String clientId, String platformType, String dataCenterNode);	
	
	/**
	 * 功能描述：关闭任务服务。
	 *
	 */
	public void shutdownLibrary();	
	
	/**
	 * 功能描述：添加订单认证。
	 *
	 * @param order            订单ID。
	 * @param accessToken      用户认证Token.
	 */
	public void addOrder(String order, String accessToken);
	
	/**
	 * 功能描述：用户登录认证。
	 *
	 * @param accessToken      用户认证Token.
	 */
	public void addLogin(String accessToken);	
	
	/**
	 * 功能描述：获取登录结果。
	 *
	 * @return 登录结果信息。
	 */
	public String getLoginResult();
	
	/**
	 * 功能描述：获取支付结果。
	 *
	 * @return 支付结果信息。
	 */
	public String getPayResult();
	
	
	/*激活码共分三步：1.decode;2.query;3.confirm*/
	/**
	 * 功能描述：设置激活码的监听回调listener
	 * @param listener 监听回调listener实例
	 */
	public void setActiveCodeListener(ActiveCodeListener listener);
	
	public void addDecodeActiveCode(String code, String accessToken, String serverId, String playerId);
	
	public void addQueryActiveCode(String code, String accessToken, String serverId, String playerId);
	
	public void addConfirmActiveCode(String code, String accessToken, String serverId, String playerId);
}
