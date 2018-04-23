package game.core.db;

import game.core.logging.LogExecutor;
import game.core.util.StringUtils;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author wangJingWei
 */
public class DbTest
{

    public static void main(String[] args)
    {
        DbTest test = new DbTest();

        User user = new User();
        user.setName("test_" + StringUtils.random(5));
        test.addUser(user);

        UserLog userLog = new UserLog();
        userLog.setDescription("create user[" + user.getName() + "]");
        LogExecutor.addTask(userLog);
    }

    public void addUser(User user)
    {
        SqlSession session = DbFactory.GAME_DB.getSessionFactory().openSession();
        try
        {
            session.insert("user.insert", user);
            DbFactory.GAME_DB.getLogger().info("新建用户：" + user.getName());
            session.commit();
        }
        catch (Exception e)
        {
            DbFactory.GAME_DB.getLogger().error(e);
        }
        finally
        {
            session.close();
        }
    }

}
