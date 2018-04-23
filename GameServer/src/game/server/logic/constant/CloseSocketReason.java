/**
 * @date 2014/8/1
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 *
 * @author ChenLong
 */
public enum CloseSocketReason
{
    UNKNOWN(0),
    REPEAT_LOGIN(1), // 重复登录, 顶号
    ACCOUNT_ABNORMAL(2), // 帐号异常
    SERVER_RESTART(3); // 服务器重启

    private final int value;

    CloseSocketReason(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
