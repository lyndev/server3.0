package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.WorldChatRecordBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author ZouZhaopeng
 */
public class WorldChatRecordDao
{
    public static int insert(WorldChatRecordBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("worldChatRecord.insert", bean);
            DBFactory.GAME_DB.getLogger().info("插入世界聊天数据: timestamp: " + bean.getTimestamp());
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertBatch(List<WorldChatRecordBean> list)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            if (list == null || list.isEmpty())
                return 0;
            int r = session.insert("worldChatRecord.insertBatch", list);
            DBFactory.GAME_DB.getLogger().info("插入世界聊天数据: timestamp: " + list.size() + " 条");
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertAudit(WorldChatRecordBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("worldChatRecord.insertAudit", bean);
            DBFactory.GAME_DB.getLogger().info("插入世界聊天数据: timestamp: " + bean.getTimestamp());
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertAuditBatch(List<WorldChatRecordBean> list)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            if (list == null || list.isEmpty())
                return 0;
            int r = session.insert("worldChatRecord.insertAuditBatch", list);
            DBFactory.GAME_DB.getLogger().info("插入世界聊天审计数据: timestamp: " + list.size() + " 条");
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int delete(long timestamp)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("timestamp", timestamp);
            int rows = session.delete("worldChatRecord.delete", param);

            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int deleteAll()
    {
        int rows = 0;
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            rows = session.delete("worldChatRecord.deleteAll");
            session.commit();
            DBFactory.GAME_DB.getLogger().info("清空worldchatrecord/worldchatrecordaudit表");
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return rows;
    }

    public static WorldChatRecordBean selectOne(long timestamp)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("timestamp", timestamp);
            WorldChatRecordBean bean = session.selectOne("worldChatRecord.selectOne", param);
            session.commit();
            return bean;
        }
        catch(Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    public static List<WorldChatRecordBean> selectAll()
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<WorldChatRecordBean> list = session.selectList("worldChatRecord.selectAll");
            session.commit();
            return list;
        }
        catch(Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }
}
