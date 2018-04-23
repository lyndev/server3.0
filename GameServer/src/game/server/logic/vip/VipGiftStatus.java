/**
 * @date 2014/9/17
 * @author ChenLong
 */
package game.server.logic.vip;

/**
 * VIP礼包状态
 *
 * @author ChenLong
 */
public enum VipGiftStatus
{
    NULL(0),
    BOUGHT(2);

    private final int value;

    private VipGiftStatus(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static VipGiftStatus getStatusFromValue(int value)
    {
        VipGiftStatus ret = null;
        for (VipGiftStatus status : VipGiftStatus.values())
        {
            if (status.getValue() == value)
            {
                ret = status;
                break;
            }
        }
        return ret;
    }
}
