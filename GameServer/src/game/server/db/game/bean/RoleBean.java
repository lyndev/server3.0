/**
 * @date 2014/4/14
 * @author ChenLong
 */
package game.server.db.game.bean;

import game.server.util.MiscUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author ChenLong
 */
public class RoleBean
{
    private boolean compressed = false; // 已压缩过？

    private String roleId;
    private String userId;
    private String fgi = StringUtils.EMPTY;
    private String fedId = StringUtils.EMPTY;
    private int platformId;
    private int serverId;
    private String roleName;
    private int roleLevel;
    private int roleExp;
    private int roleHead;
    private int vipLevel = 0;
    private int gmLevel;
    private int isRobot;
    private String headURL;
    private int sex = 0;
    private String miscData;
    private String soleId = "";

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getDigest() throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        if (miscData.trim().isEmpty() || Base64.isBase64(miscData))
            throw new IllegalStateException("miscData isEmpty or has compressed, miscData: [" + miscData + "]");
        String data = ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
        String md5 = DigestUtils.md5Hex(data);
        return md5;
    }

    public RoleBean compress()
    {
        if (!miscData.trim().isEmpty() && !Base64.isBase64(miscData))
        {
            String plainMiscData = miscData;
            try
            {
                miscData = MiscUtils.compressText(plainMiscData);
                compressed = true;
            }
            catch (UnsupportedEncodingException ex)
            {
                MiscUtils.logError("UnsupportedEncodingException", ex);
            }
        }
        return this;
    }

    public RoleBean uncompress()
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

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
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

    public int getVipLevel()
    {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel)
    {
        this.vipLevel = vipLevel;
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

    public int getRoleExp()
    {
        return roleExp;
    }

    public void setRoleExp(int roleExp)
    {
        this.roleExp = roleExp;
    }

    public int getRoleHead()
    {
        return roleHead;
    }

    public void setRoleHead(int roleHead)
    {
        this.roleHead = roleHead;
    }

    public String getMiscData()
    {
        return miscData;
    }

    public void setMiscData(String miscData)
    {
        this.miscData = miscData;
    }

    public int getGmLevel()
    {
        return gmLevel;
    }

    public void setGmLevel(int gmLevel)
    {
        if (gmLevel < 0 || gmLevel > 2)
        {
            return;
        }
        this.gmLevel = gmLevel;
    }

    public int getIsRobot()
    {
        return isRobot;
    }

    public void setIsRobot(int isRobot)
    {
        this.isRobot = isRobot;
    }

    public boolean isCompressed()
    {
        return compressed;
    }
    
    public void setHeadURL(String headURL){
        this.headURL = headURL;
    }
    
    public String getHeadURL(){
        return this.headURL;
    }
    public void setSex(int sex){
        this.sex = sex;
    }
    
    public int geSex(){
        return this.sex;
    }
    
    public void setSoleId(String soleId){
        this.soleId = soleId;
    }
    
    public String getSoleId(){
        return this.soleId;
    }  
}
