/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import game.server.logic.recharge.bean.OrderBean;
import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.message.RechargeMessage;
import game.server.logic.line.GameLineManager;
import game.server.logic.player.PlayerManager;
import game.server.logic.recharge.handler.VerifyOrderResultHandler;
import game.server.logic.struct.Player;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 充值服务 验证充值单有效性
 *
 * @author ChenLong
 */
public class RechargeService extends CommandProcessor
{
    private static final RechargeService instance;
    private static final Logger logger = Logger.getLogger(RechargeService.class);

    private final Map<String, OrderBean> rechargBeans = new HashMap<>();

    static
    {
        instance = new RechargeService();
    }

    private RechargeService()
    {
        super(RechargeService.class.getSimpleName());
    }

    public static RechargeService getInstance()
    {
        return instance;
    }

    /**
     * 请求验证订单
     *
     * @param player
     * @param reqMsg
     */
    public void addVerifyOrderCommand(Player player, RechargeMessage.ReqRechargeVerify reqMsg)
    {
        OrderBean bean = new OrderBean(reqMsg.getStrBillNO(), player.getUserId(), player.getRoleId(), reqMsg);
        addCommand(new ICommand()
        {
            private OrderBean bean = null;

            public ICommand set(OrderBean bean)
            {
                this.bean = bean;
                return this;
            }

            @Override
            public void action()
            {
                OrderBean beanHas = rechargBeans.get(bean.getOrder());
                if (beanHas == null)
                {
                    rechargBeans.put(bean.getReqMsg().getStrBillNO(), bean);
                    ServicesFactory.getSingleVerifyService().addOrder(bean.getReqMsg().getStrBillNO(), bean.getReqMsg().getStrAccessToken());
                }
                else
                {
                    String msg = "Order has verifying, roleId = " + bean.getRoleId();
                    if (beanHas.getRoleId() != bean.getRoleId() || beanHas.getUserId() != bean.getUserId())
                        msg += "bean: " + bean.toString() + ", beanHas: " + beanHas.toString();
                    logger.error(msg);
                }
            }
        }.set(bean));
    }

    /**
     * RechargeResultGetter返回验证结果
     *
     * @param result
     */
    public void addVerifyOrderResultCommand(String result)
    {
        addCommand(new ICommand()
        {
            private String result;

            public ICommand set(String result)
            {
                this.result = result;
                return this;
            }

            @Override
            public void action()
            {
                JSONObject jsonObj = null;
                // 平台说这个结果有可能是 json对象{} 也可能是 一个json数组包着的一个json对象[{}], 艹 F***
                try
                {
                    jsonObj = JSON.parseObject(result);
                }
                catch (Exception ex)
                {
                }
                if (jsonObj == null)
                {
                    try
                    {
                        JSONArray jsonArray = JSON.parseArray(result);
                        if (!jsonArray.isEmpty())
                            jsonObj = jsonArray.getJSONObject(0);
                    }
                    catch (Exception ex)
                    {
                    }
                }
                if (jsonObj != null)
                {
                    String orderid = jsonObj.getString("orderid");
                    if (orderid != null && !orderid.trim().isEmpty())
                    {
                        OrderBean bean = rechargBeans.get(orderid);
                        if (bean != null)
                        {
                            try
                            {
                                bean.setResult(jsonObj.toJSONString());
                                Player player = PlayerManager.getPlayerByUserId(bean.getUserId());
                                if (player != null)
                                {
                                    int line = player.getLineId();
                                    GameLineManager.getInstance().addCommand(line, new VerifyOrderResultHandler(bean));
                                }
                                else
                                {
                                    logger.error("cannot get player object, userId = " + bean.getUserId());
                                }
                            }
                            finally
                            {
                                rechargBeans.remove(orderid);
                            }
                        }
                        else
                        {
                            logger.error("bean == null");
                        }
                    }
                    else
                    {
                        logger.error("result orderid: [" + orderid + "]");
                    }
                }
                else
                {
                    logger.error("jsonObj == null, result: [" + result + "]");
                }
            }
        }.set(result));
    }

    @Override

    public void writeError(String message)
    {
        logger.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        logger.error(message, t);
    }
}
