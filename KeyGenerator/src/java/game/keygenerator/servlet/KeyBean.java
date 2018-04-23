package game.keygenerator.servlet;

import java.util.Date;

/**
 *
 * @author ZouZhaopeng
 */
public class KeyBean
{
    private long serialNo;
    private int serialType;
    private Date createTime;
    private int used;
    private Date useTime;
    private long roleId;

    public long getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(long serialNo)
    {
        this.serialNo = serialNo;
    }

    public int getSerialType()
    {
        return serialType;
    }

    public void setSerialType(int serialType)
    {
        this.serialType = serialType;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public int isUsed()
    {
        return used;
    }

    public void setUsed(int used)
    {
        this.used = used;
    }

    public Date getUseTime()
    {
        return useTime;
    }

    public void setUseTime(Date useTime)
    {
        this.useTime = useTime;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }
    
}
