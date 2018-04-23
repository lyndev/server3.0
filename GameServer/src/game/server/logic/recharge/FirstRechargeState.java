/**
 * @date 2014/9/25
 * @author ChenLong
 */
package game.server.logic.recharge;

/**
 * 首充状态
 *
 * @author ChenLong
 */
public enum FirstRechargeState
{
    CAN_NOT_GET(1, "不能领取"),
    CAN_GET(2, "可以领取"),
    HAS_GOT(3, "已经领取");

    private final int value;
    private final String desc;

    FirstRechargeState(int value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public int getValue()
    {
        return value;
    }

    public String getDesc()
    {
        return desc;
    }

    public static FirstRechargeState getType(int value)
    {
        FirstRechargeState ret = null;
        for (FirstRechargeState type : FirstRechargeState.values())
        {
            if (type.getValue() == value)
            {
                ret = type;
                break;
            }
        }
        return ret;
    }
}
