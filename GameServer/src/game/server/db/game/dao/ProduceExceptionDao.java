package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.ProduceExceptionBean;
import org.apache.ibatis.session.SqlSession;

public class ProduceExceptionDao
{

    public static int insert(ProduceExceptionBean bean)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("produceException.insert", bean);
            DBFactory.GAME_DB.getLogger().info("记录资源/物品产出异常！");
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

}
