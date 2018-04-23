/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.XFBaseBean;
import game.server.db.game.bean.XFDetailBean;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrator
 */
public class XFBaseDao
{
    /**
     * 插入仙府数据
     *
     * @param xfBaseBean
     * @param hasLog
     * @return
     */
    public static int insert(XFBaseBean xfBaseBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("xfBase.insert", xfBaseBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("新建仙府  userId: [" + xfBaseBean.getUserId() + "], " + "xfBaseId: [" + xfBaseBean.getBuildId() + "]");
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
     * 更新XFBaseBean信息
     *
     * @param xfBaseBean
     * @param hasLog
     * @return
     */
    public static int update(XFBaseBean xfBaseBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.update("xfBase.update", xfBaseBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("回存仙府 userId: [" + xfBaseBean.getUserId() + "], " + "xfBaseId: [" + xfBaseBean.getBuildId() + "]");
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
     * 通过角色id(userId)查询XFBaseBean信息
     *
     * @param userId
     * @return
     */
    public static XFBaseBean selectByUserId(String userId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            XFBaseBean xfBaseBean = (XFBaseBean) session.selectOne("xfBase.selectByUserId", map);
            return xfBaseBean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<XFBaseBean> selectAll()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            long currentTimeMillis = System.currentTimeMillis();
            List<XFBaseBean> list = session.selectList("xfBase.selectAll");
            //DBFactory.GAME_DB.getLogger().info("新建仙府  userId: [" + xfBaseBean.getUserId()+ "], " + "xfBaseId: [" + xfBaseBean.getBuildId()+ "]");
            return list;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    /**
     * 查询所有数据，将关联玩家的详细数据也一并查出，但未作解析.
     * 此处在大数据量情况下直接使用MyBatis的selectList可能出现超时MySQLTimeoutException异常，故才用JDBC游标查询
     *
     * @return
     */
    public static List<XFDetailBean> selecAllJoinPlayer()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            //List<XFDetailBean> list = session.selectList("xfBase.selecAllJoinPlayer");
            //DBFactory.GAME_DB.getLogger().info("新建仙府  userId: [" + xfBaseBean.getUserId()+ "], " + "xfBaseId: [" + xfBaseBean.getBuildId()+ "]");
            String sql = "SELECT xf.buildId, xf.userId, xf.accBuildNum, xf.orderNum, xf.isRobot, xf.miscData, r.roleName, r.roleHead, r.miscData as playerMiscData\n"
                    + "	FROM xfBase xf \n"
                    + "	inner join user u on u.userId = xf.userId\n"
                    + "	inner join role r on r.userId = u.userId";
            Connection conn = session.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            List<XFDetailBean> list = new LinkedList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                XFDetailBean bean = new XFDetailBean();
                bean.setBuildId(rs.getString("buildId"));
                bean.setUserId(rs.getString("userId"));
                bean.setAccBuildNum(rs.getInt("accBuildNum"));
                bean.setOrderNum(rs.getInt("orderNum"));
                bean.setIsRobot(rs.getInt("isRobot"));
                bean.setMiscData(rs.getString("miscData"));
                bean.setRoleName(rs.getString("roleName"));
                bean.setRoleHead(rs.getInt("roleHead"));
                bean.setPlayerMiscData(rs.getString("playerMiscData"));
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

    public static int updateNoLog(XFBaseBean xfBaseBean)
    {
        return update(xfBaseBean, false);
    }

    public static int updateWithLog(XFBaseBean xfBaseBean)
    {
        return update(xfBaseBean, true);
    }
}
