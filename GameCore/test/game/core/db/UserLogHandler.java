package game.core.db;

import game.core.logging.LogHandler;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrator
 */
public class UserLogHandler extends LogHandler
{

    @Override
    public void action()
    {
        SqlSession session = DbFactory.LOG_DB.getSessionFactory().openSession();
        try
        {
            UserLog userLog = (UserLog) getLog();
            session.insert("userLog.insert", userLog);
            DbFactory.LOG_DB.getLogger().info("新建用户日志：" + userLog.getDescription());
            session.commit();
        }
        catch (Exception e)
        {
            DbFactory.LOG_DB.getLogger().error(e);
        }
        finally
        {
            session.close();
        }
    }

}
