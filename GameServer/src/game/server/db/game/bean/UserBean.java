/**
 * @date 2014/4/14
 * @author ChenLong
 */
package game.server.db.game.bean;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author ChenLong
 */
public class UserBean
{
    private String userName;
    private String userId;
    private String soleId;

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
}
