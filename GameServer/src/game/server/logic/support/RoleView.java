package game.server.logic.support;

/**
 *
 * @author ZouZhaopeng
 */
public class RoleView implements Cloneable
{
    private String userId;
    private String userName;
    private String soleId;
    private String roleId;
    private String roleName;
    
    private int roleLevel;
    private int vipLevel;
    private long createTime;
    private int xfLevel;

    private int pvpRank;
    private int serverId;
    private int platformId;
    private int isRobot = 0;
    
    private long monthCardExpiryDate = 0; // 月卡有效期
    private boolean online = false; // 在线?

    // 注意：新加 可变/自定 成员需要添加clone()
    @Override
    public RoleView clone() throws CloneNotSupportedException
    {
        // call Object.clone()
        RoleView cloned = (RoleView) super.clone();

        // clone mutable fields
        return cloned;
    }

    public RoleView()
    {
    }

    public RoleView(String userId, String userName, String soleId, String roleId, String roleName,
            int roleLevel, int vipLevel, int serverId, int platformId)
    {
        this.userId = userId;
        this.userName = userName;
        this.soleId = soleId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleLevel = roleLevel;
        this.vipLevel = vipLevel;
        this.serverId = serverId;
        this.platformId = platformId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public String getSoleId()
    {
        return soleId;
    }
        
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setSoleId(String soleId)
    {
        this.soleId = soleId;
    }
      
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
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

    public int getVipLevel()
    {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public int getXfLevel()
    {
        return xfLevel;
    }

    public void setXfLevel(int xfLevel)
    {
        this.xfLevel = xfLevel;
    }

    public int getPvpRank()
    {
        return pvpRank;
    }

    public void setPvpRank(int pvpRank)
    {
        this.pvpRank = pvpRank;
    }

    public int getServerId()
    {
        return serverId;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public int getPlatformId()
    {
        return platformId;
    }

    public void setPlatformId(int platformId)
    {
        this.platformId = platformId;
    }

    public int getIsRobot()
    {
        return isRobot;
    }

    public void setIsRobot(int isRobot)
    {
        this.isRobot = isRobot;
    }

    public long getMonthCardExpiryDate()
    {
        return monthCardExpiryDate;
    }

    public void setMonthCardExpiryDate(long monthCardExpiryDate)
    {
        this.monthCardExpiryDate = monthCardExpiryDate;
    }

    public boolean isOnline()
    {
        return online;
    }

    public void setOnline(boolean online)
    {
        this.online = online;
    }
}
