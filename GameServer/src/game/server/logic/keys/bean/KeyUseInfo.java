package game.server.logic.keys.bean;

/**
 *
 * <b>激活码/兑换码的使用信息.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class KeyUseInfo
{

    private String token; // 从平台SDK中获取的访问令牌

    private String key; // 激活码/兑换码的唯一标识符

    private String playerId; // 使用者的玩家角色ID

    private String serverId; // 使用者所属的区服ID

    @Override
    public String toString()
    {
        return "[{key=" + key + "}, {playerId=" + playerId
                + "}, {serverId=" + serverId + "}, {token=" + token + "}]";
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public String getServerId()
    {
        return serverId;
    }

    public void setServerId(String serverId)
    {
        this.serverId = serverId;
    }

}
