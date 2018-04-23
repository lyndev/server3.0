/**
 * @date 2014/5/19
 * @author ChenLong
 */
package game.server.db.game.bean;

import game.server.util.MiscUtils;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 用于user, role联表查询结果Bean
 *
 * @author ChenLong
 */
public class UserRoleBean
{
    // user field
    private String userId;
    private String userName;
    private String soleId;

    // role field
    private String roleId;
    private String roleName;
    private int roleLevel;
    private int roleHead;
    private int vipLevel;
    private int isRobot;
    private String fgi = StringUtils.EMPTY;
    private String fedId = StringUtils.EMPTY;
    private int platformId;
    private int serverId;
    private String miscData;
    private int roomNumber;
    private int roomType;

    public void splitUserRole(UserBean userBean, RoleBean roleBean)
    {
        // user
        userBean.setUserId(userId);
        userBean.setUserName(userName);
        userBean.setSoleId(soleId);

        // role
        roleBean.setRoleId(roleId);
        roleBean.setUserId(userId);
        roleBean.setRoleName(roleName);
        roleBean.setRoleLevel(roleLevel);
        roleBean.setRoleHead(roleHead);
        roleBean.setVipLevel(vipLevel);
        roleBean.setIsRobot(isRobot);
        roleBean.setFgi(fgi);
        roleBean.setFedId(fedId);
        roleBean.setPlatformId(platformId);
        roleBean.setServerId(serverId);
        roleBean.setMiscData(miscData);
    }

    public UserRoleBean compress()
    {
        if (!miscData.trim().isEmpty() && !Base64.isBase64(miscData))
        {
            String plainMiscData = miscData;
            try
            {
                miscData = MiscUtils.compressText(plainMiscData);
            }
            catch (UnsupportedEncodingException ex)
            {
                MiscUtils.logError("UnsupportedEncodingException", ex);
            }
        }
        return this;
    }

    public UserRoleBean uncompress()
    {
        if (!miscData.trim().isEmpty() && Base64.isBase64(miscData))
        {
            String compressedMiscData = miscData;
            try
            {
                miscData = MiscUtils.uncompressText(compressedMiscData);
            }
            catch (UnsupportedEncodingException ex)
            {
                MiscUtils.logError("UnsupportedEncodingException", ex);
            }
        }
        return this;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getSoleId()
    {
        return soleId;
    }

    public void setSoleId(String soleId)
    {
        this.soleId = soleId;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
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

    public int getVipLevel()
    {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public int getIsRobot()
    {
        return isRobot;
    }

    public void setIsRobot(int isRobot)
    {
        this.isRobot = isRobot;
    }

    public String getFgi()
    {
        return fgi;
    }

    public void setFgi(String fgi)
    {
        this.fgi = fgi;
    }

    public String getFedId()
    {
        return fedId;
    }

    public void setFedId(String fedId)
    {
        this.fedId = fedId;
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

    public String getMiscData()
    {
        return miscData;
    }

    public void setMiscData(String miscData)
    {
        this.miscData = miscData;
    }

    public void setRoomType(int roomType)
    {

    }

    public void setRoomNumber(int roomNumber)
    {
        this.roomNumber = roomNumber;
    }

    public int getRoomType()
    {
        return this.roomType;
    }

    public int getRoomNumber()
    {
        return this.roomNumber;
    }
}
