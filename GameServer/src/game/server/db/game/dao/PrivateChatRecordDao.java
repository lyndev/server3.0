package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.PrivateChatRecordBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author ZouZhaopeng
 */
public class PrivateChatRecordDao
{
    public static int insert(PrivateChatRecordBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("privateChatRecord.insert", bean);
            DBFactory.GAME_DB.getLogger().info("插入私聊数据: " + bean.getConversationId() + ", timestamp: " + bean.getTimestamp());
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertBatch(List<PrivateChatRecordBean> list)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            if (list == null || list.isEmpty())
                return 0;
            int r = session.insert("privateChatRecord.insertBatch", list);
            DBFactory.GAME_DB.getLogger().info("插入私聊数据: " + list.size() + " 条");
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertAudit(PrivateChatRecordBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("privateChatRecord.insertAudit", bean);
            DBFactory.GAME_DB.getLogger().info("插入私聊数据: " + bean.getConversationId() + ", timestamp: " + bean.getTimestamp());
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertAuditBatch(List<PrivateChatRecordBean> list)
    {
        if (list == null || list.isEmpty())
            return 0;
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("privateChatRecord.insertAuditBatch", list);
            DBFactory.GAME_DB.getLogger().info("插入私聊审计数据: " + list.size() + " 条");
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int delete(String conversationId, long timestamp)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("conversationId", conversationId);
            param.put("timestamp", timestamp);
            int rows = session.delete("privateChatRecord.delete", param);

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
            rows = session.delete("privateChatRecord.deleteAll");
            session.commit();
            DBFactory.GAME_DB.getLogger().info("清空privatechatrecord/privatechatrecordaudit表");
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return rows;
    }

    public static PrivateChatRecordBean selectOne(String conversationId, long timestamp)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("conversationId", conversationId);
            param.put("timestamp", timestamp);
            PrivateChatRecordBean bean = session.selectOne("privateChatRecord.selectOne", param);
            session.commit();
            return bean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    public static List<PrivateChatRecordBean> selectAll(String conversationId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("conversationId", conversationId);
            List<PrivateChatRecordBean> list = session.selectList("privateChatRecord.selectAll", param);
            session.commit();
            return list;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

}
