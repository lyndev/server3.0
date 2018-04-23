/**
 * @date 2014/4/14
 * @author ChenLong
 */
package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.UserBean;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.bean.XFDetailBean;
import game.server.logic.login.service.LoginService;
import game.server.logic.support.RoleView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class UserDao
{
        private static Logger log = Logger.getLogger(UserDao.class);
    /**
     * 插入帐号
     *
     * @param userBean
     * @return
     */
    public static int insert(UserBean userBean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("user.insert", userBean);
//            DBFactory.GAME_DB.getLogger().info("新建用户：" + userBean.getUserName());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    /**
     * 通过用户ID(userID)查询UserBean信息
     *
     * @param userId
     * @return
     */
    public static UserBean selectById(String userId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            UserBean user = (UserBean) session.selectOne("user.selectById", map);
            return user;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 通过用户名(userName)查询UserBean信息
     *
     * @param userName
     * @return
     */
    public static UserBean selectByUserName(String userName)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", userName);
            UserBean user = (UserBean) session.selectOne("user.selectByName", map);
            return user;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 联表查询user/role数据
     *
     * @param userName
     * @return
     */
    public static UserRoleBean selectJoinByName(String userName)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", userName);
            UserRoleBean userRoleBean = (UserRoleBean) session.selectOne("user.selectJoinByName", map);
            return userRoleBean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    public static List<RoleView> selectAllJoinRole()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<RoleView> roleViews = session.selectList("user.selectAllJoinRole");
            return roleViews;
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
    public static List<UserBean> selectAll()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<UserBean> users = session.selectList("user.selectAll");
            return users;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 插入帐号
     *
     * @param users
     * @return
     */
    public static int insertBatch(List<UserBean> users)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("user.insertBatch", users);
//            for (UserBean u : users)
//                DBFactory.GAME_DB.getLogger().info("新建用户：" + u.getUserId());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    /**
     * 插入帐号
     *
     * @param users
     * @return
     */
    public static int insertListBatch(List<List<UserBean>> users)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int sumRows = 0;
            for (List<UserBean> ul : users)
            {
                sumRows += session.insert("testLog1.insertBatch1", ul);
            }
//            for (UserBean u : users)
//                DBFactory.GAME_DB.getLogger().info("新建用户：" + u.getUserId());
            session.commit();
            return sumRows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    /**
     * 查询所有机器人数据.
     *
     * @return
     */
    public static List<UserRoleBean> selectAllRobots()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<UserRoleBean> robots = session.selectList("user.selecJoinAllRobots");
            return robots;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 查询所有玩家数据.
     *
     * @return
     */
    public static List<UserRoleBean> selectAllPlayers()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            //List<UserRoleBean> players = session.selectList("user.selecJoinAll");
            String sql = "SELECT u.userId, u.userName, r.fgi, r.platformId, r.serverId, "
                    + "r.roleId, r.roleName, r.roleLevel, r.vipLevel, r.roleHead, r.isRobot, r.miscData \n"
                    + "FROM role r inner join user u on u.userId = r.userId  ";
            Connection conn = session.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            List<UserRoleBean> list = new LinkedList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                UserRoleBean bean = new UserRoleBean();

                bean.setUserId(rs.getString("userId"));
                bean.setUserName(rs.getString("userName"));
                bean.setFgi(rs.getString("fgi"));
                bean.setPlatformId(rs.getInt("platformId"));
                bean.setServerId(rs.getInt("serverId"));
                bean.setRoleId(rs.getString("roleId"));
                bean.setRoleName(rs.getString("roleName"));
                bean.setRoleLevel(rs.getInt("roleLevel"));
                bean.setVipLevel(rs.getInt("vipLevel"));
                bean.setRoleHead(rs.getInt("roleHead"));
                bean.setIsRobot(rs.getInt("isRobot"));
                bean.setMiscData(rs.getString("miscData"));

                list.add(bean);
            }
            return list;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 查询指定玩家数据.
     *
     * @param userId
     * @return
     */
    public static UserRoleBean selectPlayerByUserId(String userId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            return session.selectOne("user.selectJoinByUserId", userId);
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

}
