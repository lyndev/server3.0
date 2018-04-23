/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge.bean;

import game.message.RechargeMessage;
import game.message.RechargeMessage.ReqRechargeVerify;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author ChenLong
 */
public class OrderBean
{
    private final String order;
    private final long userId;
    private final long roleId;
    private final RechargeMessage.ReqRechargeVerify reqMsg;
    private String result = StringUtils.EMPTY;

    public OrderBean(String order, long userId, long roleId, ReqRechargeVerify reqMsg)
    {
        this.order = order;
        this.userId = userId;
        this.roleId = roleId;
        this.reqMsg = reqMsg;
    }

    @Override
    public String toString()
    {
        return "order: " + order + ", userId = " + userId + ", roleId = " + roleId + ", reqMsg: " + reqMsg.toString();
    }

    public String getOrder()
    {
        return order;
    }

    public long getUserId()
    {
        return userId;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public ReqRechargeVerify getReqMsg()
    {
        return reqMsg;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }
}
