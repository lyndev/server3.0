package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.AudioDataBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author ZouZhaopeng
 */
public class AudioDataDao
{
    public static int insert(AudioDataBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("audioData.insert", bean);
            DBFactory.GAME_DB.getLogger().info("插入语音消息数据: uuid: " + bean.getUuid());
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertBatch(List<AudioDataBean> list)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            if (list == null || list.isEmpty())
                return 0;
            int r = session.insert("audioData.insertBatch", list);
            DBFactory.GAME_DB.getLogger().info("插入语音消息数据: " + list.size() + " 条");
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertAudit(AudioDataBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int r = session.insert("audioData.insertAudit", bean);
            DBFactory.GAME_DB.getLogger().info("插入语音消息数据: uuid: " + bean.getUuid());
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int insertAuditBatch(List<AudioDataBean> list)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            if (list == null || list.isEmpty())
                return 0;
            int r = session.insert("audioData.insertAuditBatch", list);
            DBFactory.GAME_DB.getLogger().info("插入语音消息审查数据: " + list.size() + " 条");
            session.commit();
            return r;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int delete(String uuid)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("uuid", uuid);
            int rows = session.delete("audioData.delete", param);

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
            rows = session.delete("audioData.deleteAll");
            session.commit();
            DBFactory.GAME_DB.getLogger().info("清空audioData/audioDataAudit表");
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return rows;
    }

    public static AudioDataBean select(String uuid)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            Map<String, Object> param = new HashMap<>();
            param.put("uuid", uuid);
            AudioDataBean bean = session.selectOne("audioData.select", param);
            session.commit();
            return bean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

}
