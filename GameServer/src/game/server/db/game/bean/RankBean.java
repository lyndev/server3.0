package game.server.db.game.bean;

import java.util.Date;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author Administrator
 */
public class RankBean
{
    private String userId;
    private String roleId;
    private String roleName;
    private int roleHead;
    private int roleLevel;

    //成就排行字段
    private int achievementPoint;
    private Date achievementLastModify;
    private int achievementRank;

    //天降宝箱排行字段
    private int funMission1Score;
    private Date funMission1LastModify;
    private int funMission1Rank;

    //趣味跑酷排行字段
    private int funMission2Score;
    private Date funMission2LastModify;
    private int funMission2Rank;

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public int getAchievementPoint()
    {
        return achievementPoint;
    }

    public void setAchievementPoint(int achievementPoint)
    {
        this.achievementPoint = achievementPoint;
    }

    public Date getAchievementLastModify()
    {
        return achievementLastModify;
    }

    public void setAchievementLastModify(Date achievementLastModify)
    {
        this.achievementLastModify = achievementLastModify;
    }

    public int getFunMission1Score()
    {
        return funMission1Score;
    }

    public void setFunMission1Score(int funMission1Score)
    {
        this.funMission1Score = funMission1Score;
    }

    public Date getFunMission1LastModify()
    {
        return funMission1LastModify;
    }

    public void setFunMission1LastModify(Date funMission1LastModify)
    {
        this.funMission1LastModify = funMission1LastModify;
    }

    public int getFunMission2Score()
    {
        return funMission2Score;
    }

    public void setFunMission2Score(int funMission2Score)
    {
        this.funMission2Score = funMission2Score;
    }

    public Date getFunMission2LastModify()
    {
        return funMission2LastModify;
    }

    public void setFunMission2LastModify(Date funMission2LastModify)
    {
        this.funMission2LastModify = funMission2LastModify;
    }

    public int getAchievementRank()
    {
        return achievementRank;
    }

    public void setAchievementRank(int achievementRank)
    {
        this.achievementRank = achievementRank;
    }

    public int getFunMission1Rank()
    {
        return funMission1Rank;
    }

    public void setFunMission1Rank(int funMission1Rank)
    {
        this.funMission1Rank = funMission1Rank;
    }

    public int getFunMission2Rank()
    {
        return funMission2Rank;
    }

    public int getMissionRank(int missionId)
    {
        if (missionId == 1)
            return getFunMission1Rank();
        else if (missionId == 2)
            return getFunMission2Rank();
        else
            return 0;
    }

    public void setFunMission2Rank(int funMission2Rank)
    {
        this.funMission2Rank = funMission2Rank;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public int getRoleHead()
    {
        return roleHead;
    }

    public void setRoleHead(int roleHead)
    {
        this.roleHead = roleHead;
    }

    public int getRoleLevel()
    {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel)
    {
        this.roleLevel = roleLevel;
    }
}
