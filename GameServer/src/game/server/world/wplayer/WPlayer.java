package game.server.world.wplayer;

import game.server.logic.struct.Player;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Administrator
 */
public class WPlayer
{
    private Integer lineId;
    private long userId;
    private long roleId;
    private String userName;
    private String roleName;
    private int platformId;
    private int serverId;
    private int roleLevel;
    private int roleHead;
    private final boolean isRobot;
    private IoSession session;
    private IoSession udpSession;
    private long lastConnectTime; // 上次连接/断线时间
    private int denyChatTimestamp; //禁聊结束时间戳, 到此时间自动解除禁聊. 如果denyChatTimestamp > 当前时间, 禁止聊天
    private long createRoleTime;

    public WPlayer(Player player)
    {
        this.lineId = player.getLineId();
        this.userId = player.getUserId();
        this.roleId = player.getRoleId();
        this.userName = player.getUserName();
        this.roleName = player.getRoleName();
        this.roleLevel = player.getRoleLevel();
        this.roleHead = player.getRoleHead();
        this.isRobot = player.isRobot();
        this.platformId = player.getPlatformId();
        this.serverId = player.getServerId();
        this.session = player.getSession();
        this.lastConnectTime = player.getLastConnectTime(); // 上次连接时间
        this.createRoleTime = player.getCreateRoleTime(); // 角色创建时间
        this.denyChatTimestamp = player.getDenyChatTimestamp();
        this.udpSession = null;
    }

    public Integer getLineId()
    {
        return lineId;
    }

    public void setLineId(Integer lineId)
    {
        this.lineId = lineId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public int getRoleLevel()
    {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel)
    {
        this.roleLevel = roleLevel;
    }

    public int getRoleHead()
    {
        return roleHead;
    }

    public void setRoleHead(int roleHead)
    {
        this.roleHead = roleHead;
    }

    public boolean isRobot()
    {
        return isRobot;
    }

    public int getPlatformId()
    {
        return platformId;
    }

    public void setPlatformId(int platformId)
    {
        this.platformId = platformId;
    }

    public int getServerId()
    {
        return serverId;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public IoSession getSession()
    {
        return session;
    }
    
    public void setSession(IoSession session)
    {
        this.session = session;
    }

    public IoSession getUDPSession(IoSession session){
        return udpSession;
    }
    
    public void setUDPSession(IoSession session){
        this.udpSession = session;
    }
    
    public long getLastConnectTime()
    {
        return lastConnectTime;
    }

    public void setLastConnectTime(long lastConnectTime)
    {
        this.lastConnectTime = lastConnectTime;
    }

    public long getCreateRoleTime()
    {
        return createRoleTime;
    }

    public void setCreateRoleTime(long createRoleTime)
    {
        this.createRoleTime = createRoleTime;
    }

    public int getDenyChatTimestamp()
    {
        return denyChatTimestamp;
    }

    public void setDenyChatTimestamp(int denyChatTimestamp)
    {
        this.denyChatTimestamp = denyChatTimestamp;
    }
}
