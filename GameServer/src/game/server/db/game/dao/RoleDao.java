/**
 * @date 2014/4/14
 * @author ChenLong
 */
package game.server.db.game.dao;

import game.core.util.UUIDUtils;
import game.server.db.DBFactory;
import game.server.db.game.bean.FriendDataBean;
import game.server.db.game.bean.RoleBean;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author ChenLong
 */
public class RoleDao
{

    /**
     * 插入帐号
     *
     * @param roleBean
     * @return
     */
    public static int insert(RoleBean roleBean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("role.insert", roleBean);
//            DBFactory.GAME_DB.getLogger().info("新建角色：" + roleBean.getRoleName());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int updateNoLog(RoleBean roleBean)
    {
        return update(roleBean, false);
    }

    public static int updateWithLog(RoleBean roleBean)
    {
        return update(roleBean, true);
    }

    public static int update(RoleBean roleBean, boolean hasLog)
    {
        if (roleBean.isCompressed())
        {
            try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
            {
                int rows = session.update("role.update", roleBean);
                if (hasLog)
                    DBFactory.GAME_DB.getLogger().info("回存角色 roleId: [" + roleBean.getRoleId() + "], " + "roleName: [" + roleBean.getRoleName() + "]");
                session.commit();
                return rows;
            }
            catch (Exception e)
            {
                DBFactory.GAME_DB.getLogger().error(e);
            }
        }
        else
        {
            DBFactory.GAME_DB.getLogger().error("回存角色数据未压缩 roleId: [" + roleBean.getRoleId() + "], " + "roleName: [" + roleBean.getRoleName() + "]");
        }
        return 0;
    }

    /**
     * 通过用户名(userName)查询UserBean信息
     *
     * @param roleName
     * @return
     */
    public static RoleBean selectByRoleName(String roleName)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("roleName", roleName);
            RoleBean roleBean = (RoleBean) session.selectOne("role.selectByRoleName", map);
            return roleBean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 通过角色ID(roleId)查询UserBean信息
     *
     * @param roleId
     * @return
     */
    public static RoleBean selectByRoleId(UUID roleId)
    {
        return selectByRoleId(UUIDUtils.toCompactString(roleId));
    }

    /**
     * 通过角色ID(roleId)查询UserBean信息
     *
     * @param roleId
     * @return
     */
    public static RoleBean selectByRoleId(String roleId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("roleId", roleId);
            RoleBean roleBean = (RoleBean) session.selectOne("role.selectByRoleId", map);
            return roleBean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 通过角色ID(roleId)查询UserBean信息
     *
     * @param userId
     * @return
     */
    public static RoleBean selectByUserId(UUID userId)
    {
        return selectByUserId(UUIDUtils.toCompactString(userId));
    }

    /**
     * 通过角色ID(roleId)查询UserBean信息
     *
     * @param userId
     * @return
     */
    public static RoleBean selectByUserId(String userId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            RoleBean roleBean = (RoleBean) session.selectOne("role.selectByUserId", map);
            return roleBean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 查询所有User
     *
     * @return
     */
    public static List<RoleBean> selectAll()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<RoleBean> roles = session.selectList("role.selectAll");
            return roles;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    public static List<FriendDataBean> selectAllFriendData()
    {
        List<FriendDataBean> list = new LinkedList<>();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            list = session.selectList("role.selectAllFriendData");
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return list;
    }

    public static int updateFriendData(FriendDataBean friendDataBean)
    {
        int rows = 0;
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            rows = session.update("role.updateFriendData", friendDataBean);
            session.commit();
            DBFactory.GAME_DB.getLogger().info("回存好友数据 roleId: [" + friendDataBean.getRoleId() + "]");
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return rows;
    }

    public static int updateMiscData(RoleBean roleBean)
    {
        if (roleBean.isCompressed())
        {
            try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
            {
                int rows = session.update("role.updateMiscData", roleBean);
                session.commit();
                return rows;
            }
            catch (Exception e)
            {
                DBFactory.GAME_DB.getLogger().error(e);
            }
        }
        else
        {
            DBFactory.GAME_DB.getLogger().error("更新MiscData数据未压缩 roleId: [" + roleBean.getRoleId() + "], " + "roleName: [" + roleBean.getRoleName() + "]");
        }
        return 0;
    }
}
