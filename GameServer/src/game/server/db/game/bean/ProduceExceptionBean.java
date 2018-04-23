package game.server.db.game.bean;

import java.util.Date;

/**
 * 资源或者物品产出的异常记录.
 * 
 * @author wangJingWei
 */
public class ProduceExceptionBean
{

    private int id;
    private String roleId;
    private int type;
    private int target;
    private int count;
    private Date startTime;
    private Date endTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getTarget()
    {
        return target;
    }

    public void setTarget(int target)
    {
        this.target = target;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

}
