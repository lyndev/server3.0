package game.server.db.game.dao;

import game.core.script.ScriptManager;
import game.server.db.DBFactory;
import game.server.db.game.bean.RankBean;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrator
 */
public class RankDao
{
    /**
     * 插入排行数据
     *
     * @param rankBean
     * @return
     */
    public static int insert(RankBean rankBean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("rank.insert", rankBean);
            DBFactory.GAME_DB.getLogger().info("添加排行：" + rankBean.getUserId());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int updateNoLog(RankBean rankBean)
    {
        return update(rankBean, false);
    }

    public static int updateWithLog(RankBean rankBean)
    {
        return update(rankBean, true);
    }

    /**
     * 更新排行数据
     * 
     * @param rankBean
     * @param hasLog 是否写log
     * @return 
     */
    public static int update(RankBean rankBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.update("rank.update", rankBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("回存排行 userId: [" + rankBean.getUserId() + "], " + "roleId: [" + rankBean.getRoleId() + "],"
                        + "roleName : [" + rankBean.getRoleName()+ "]," + "roleHead : [" + rankBean.getRoleHead()+ "]," + "roleLevel : [" + rankBean.getRoleLevel()+ "],"
                        + "achievementPoint : [" + rankBean.getAchievementPoint()+ "]," + "achievementLastModify: [" + rankBean.getAchievementLastModify()+ "], "
                        + "funMission1Score: [" + rankBean.getFunMission1Score()+ "], " + "funMission1LastModify: [" + rankBean.getFunMission1LastModify()+ "],"
                        + "funMission2Score: [" + rankBean.getFunMission2Score()+ "], " + "funMission2LastModify: [" + rankBean.getFunMission2LastModify()+ "]");
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
     * 更新成就排行名次
     * 
     * @param rankBean
     * @param hasLog
     * @return 
     */
    public static int updateAchievementRank(RankBean rankBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.update("rank.updateAchievementRank", rankBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("回存成就排行名次 userId: [" + rankBean.getUserId() + "], " + "achievementRank : [" + rankBean.getAchievementRank()+ "]");
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
     * 更新趣味关卡1排行名次
     * 
     * @param rankBean
     * @param hasLog
     * @return 
     */
    public static int updateFunMission1Rank(RankBean rankBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.update("rank.updateFunMission1Rank", rankBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("回存趣味关卡1排行名次 userId: [" + rankBean.getUserId() + "], " + "funMission1Rank : [" + rankBean.getFunMission1Rank()+ "]");
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
     * 更新趣味关卡2排行名次
     * 
     * @param rankBean
     * @param hasLog
     * @return 
     */
    public static int updateFunMission2Rank(RankBean rankBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.update("rank.updateFunMission2Rank", rankBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("回存趣味关卡2排行名次 userId: [" + rankBean.getUserId() + "], " + "funMission2Rank : [" + rankBean.getFunMission2Rank()+ "]");
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
     * 成就排行榜
     */
    private static volatile List<RankBean> achievementRank = null;
    public static void selectAchievementRank()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
                {
                    List<RankBean> tmpRank = session.selectList("rank.selectAchievementRank");
                    int rank = 1;
                    for (RankBean bean : tmpRank)
                    {
                        bean.setAchievementRank(rank++); //设置排名
                        updateAchievementRank(bean, true); //回存到数据库
                        Player olPlayer = PlayerManager.getPlayerByUserId(bean.getUserId());
                        if (olPlayer != null){//通知在线玩家,登陆玩家应该自己来取
                            //TODO:
                        }
                    }
                    achievementRank = tmpRank;
                }
                catch (Exception e)
                {
                    DBFactory.GAME_DB.getLogger().error(e);
                }
            }
        }).start();
    }

    public static List<RankBean> getAchievementRankList()
    {
        return achievementRank;
    }
    
    /**
     * 趣味关卡1排行榜
     */
    private static volatile List<RankBean> funMission1Rank = null;
    public static void selectFunMission1Rank()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
                {
                    List<RankBean> tmpRank = session.selectList("rank.selectFunMission1Rank");
                    int rank = 1;
                    for (RankBean bean : tmpRank)
                    {
                        bean.setFunMission1Rank(rank <= 999 ? rank : 0);
                        ++rank;
                        updateFunMission1Rank(bean, true); //回存到数据库
                        Player olPlayer = PlayerManager.getPlayerByUserId(bean.getUserId());
                        if (olPlayer != null)//通知在线玩家,登陆玩家应该自己来取
                        {
                            //TODO:
                        }
                    }
                    funMission1Rank = tmpRank;
                }
                catch (Exception e)
                {
                    DBFactory.GAME_DB.getLogger().error(e);
                }
                
                ScriptManager.getInstance().call(1009, "fake rank data");
            }
        }).start();
    }

    public static List<RankBean> getFunMission1RankList()
    {
        return funMission1Rank;
    }

    public static void setFunMission1Rank(List<RankBean> funMission1Rank)
    {
        RankDao.funMission1Rank = funMission1Rank;
    }
    
    /**
     * 趣味关卡2排行榜
     */
    private static volatile List<RankBean> funMission2Rank = null;
    public static void selectFunMission2Rank()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
                {
                    List<RankBean> tmpRank = session.selectList("rank.selectFunMission2Rank");
                    int rank = 1;
                    for (RankBean bean : tmpRank)
                    {
                        bean.setFunMission2Rank(rank <= 999 ? rank : 0);
                        ++rank;
                        updateFunMission2Rank(bean, true); //回存到数据库
                        Player olPlayer = PlayerManager.getPlayerByUserId(bean.getUserId());
                        if (olPlayer != null){//通知在线玩家,登陆玩家应该自己来取
                             //TODO:
                        }
                    }
                    funMission2Rank = tmpRank;
                }
                catch (Exception e)
                {
                    DBFactory.GAME_DB.getLogger().error(e);
                }
                
                ScriptManager.getInstance().call(1010, "fake rank data");
            }
        }).start();
    }

    public static List<RankBean> getFunMission2RankList()
    {
        return funMission2Rank;
    }

    public static void setFunMission2Rank(List<RankBean> funMission2Rank)
    {
        RankDao.funMission2Rank = funMission2Rank;
    }
    
    public static void initRankLists()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            achievementRank = session.selectList("rank.initAchievementRank");
            if(achievementRank.isEmpty())
                selectAchievementRank();
            
            funMission1Rank = session.selectList("rank.initFunMission1Rank");
            if(funMission1Rank.isEmpty())
                selectFunMission1Rank();
            
            funMission2Rank = session.selectList("rank.initFunMission2Rank");
            if(funMission2Rank.isEmpty())
                selectFunMission2Rank();
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
    }
}
