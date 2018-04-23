package game.server.logic.struct;

import game.core.util.DateUtils;
import game.core.util.UUIDUtils;
import java.util.UUID;

/**
 *
 * <b>令牌类.</b>
 * <p>
 * 范例1：<br>
 * 玩家成功登录后为其首次创建一个令牌，如果客户端与服务端断开连接，当客户端再次发起消息请求之前，需要重新连接服务器．<br>
 * 如果玩家信息还在缓存中，本次重连不用验证账号且不进入登录流程，直接根据玩家持有的令牌进行验证即可.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public final class Token
{

    private final static int TOKEN_TIMES = 5 * 60000; // 令牌有效期（单位：毫秒）

    private UUID value; // 令牌值

    private long times; // 过期时间

    public Token()
    {
        value = UUID.randomUUID();
        times = System.currentTimeMillis() + TOKEN_TIMES;
    }

    /**
     * 获取令牌值.
     *
     * @return
     */
    public String getValue()
    {
        return UUIDUtils.toCompactString(value);
    }
    
    public UUID getUUID()
    {
        return value;
    }

    /**
     * 尝试刷新令牌，只有当令牌过期才会刷新.
     *
     * @return 令牌刷新返回true，否则返回false.
     */
    public boolean flush()
    {
        long nowTime = System.currentTimeMillis();
        if (DateUtils.before(nowTime, times))
            return false;

        value = UUID.randomUUID();
        times = nowTime + TOKEN_TIMES;
        return true;
    }
    
    /**
     * 判断令牌是否超时
     * @return 
     */
    public boolean isTimeout()
    {
        return System.currentTimeMillis() > times;
    }

    @Override
    public String toString()
    {
        return "token=" + value + ", times="
                + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(times));
    }

}
