/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.db.game.bean;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author Administrator
 */
public class XFBaseBean
{
    private String buildId;                                                      // 仙府基地id
    private String userId;                                                       // 玩家id
    private int accBuildNum;                                                // 加速升级次数
    private int orderNum;                                                   // 仙府序列
    private int isRobot;                                                        // 是否是机器人仙府
    private String miscData;                                               // 其他数据（仙府、资源建筑）

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

    public String getBuildId()
    {
        return buildId;
    }

    public void setBuildId(String buildId)
    {
        this.buildId = buildId;
    }

    public int getAccBuildNum()
    {
        return accBuildNum;
    }

    public void setAccBuildNum(int accBuildNum)
    {
        this.accBuildNum = accBuildNum;
    }

    public int getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(int orderNum)
    {
        this.orderNum = orderNum;
    }

    public int getIsRobot()
    {
        return isRobot;
    }

    public void setIsRobot(int isRobot)
    {
        this.isRobot = isRobot;
    }

    public String getMiscData()
    {
        return miscData;
    }

    public void setMiscData(String miscData)
    {
        this.miscData = miscData;
    }

}
