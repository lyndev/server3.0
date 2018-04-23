/**
 * @date 2014/10/8
 * @author ChenLong
 */
package game.server.db.game.bean;

/**
 *
 * @author ChenLong
 */
public class FriendDataBean
{
    private String roleId;
    private String userId;
    private String friendData;

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

    public String getFriendData()
    {
        return friendData;
    }

    public void setFriendData(String friendData)
    {
        this.friendData = friendData;
    }
}
