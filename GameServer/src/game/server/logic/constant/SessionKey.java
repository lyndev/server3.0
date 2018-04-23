/**
 * @date 2014/5/15
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 *
 * @author ChenLong
 */
public enum SessionKey
{
    GAME_LINE(true), // 所在线
    FGI(true), // 渠道ID
    PLATFORM_ID(true), // 平台ID
    SERVER_ID(true), // 服务器ID
    USER_NAME(true), // 用户名
    USER_ID(true), // 用户ID
    LOGIN_TYPE(false), // 登录类型
    CLIENT(true), // 平台U2002登录日志需要参数client
    DEVICE(true), // 平台U2002登录日志需要参数device
    PLATFORM_UID(true), // 平台U2002登录日志需要参数platform_uid
    CLIENT_VER(true),
    LOGIN_MSG_PROC(false); // 是否已经处理过登录消息

    public boolean needCopy;

    SessionKey(boolean needCopy)
    {
        this.needCopy = needCopy;
    }
}
