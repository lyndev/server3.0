package game.server.logic.robot;

import game.core.util.SimpleRandom;
import game.data.GameDataManager;
import game.data.bean.q_act_nameBean;
import game.server.exception.ServerException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * <b>机器人姓名容器.</b>
 * <p>
 * 机器人姓名的随机组合，玩家创建角色时也可以使用.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class RobotNames
{
    private static final Logger logger = Logger.getLogger(RobotNames.class);
    private static final List<String> surnames = new ArrayList<>(); // 姓氏
    private static final List<String> names = new ArrayList<>(); // 名字
    private static final SimpleRandom random = new SimpleRandom();

    public static synchronized void load()
    {
        logger.info("load RobotNames");
        surnames.clear();
        names.clear();
        List<q_act_nameBean> list = GameDataManager.getInstance().q_act_nameContainer.getList();
        if (list != null)
        {
            for (q_act_nameBean obj : list)
            {
                if (obj.getQ_surname() != null && !obj.getQ_surname().isEmpty())
                {
                    surnames.add(obj.getQ_surname());
                }
                if (obj.getQ_name() != null && !obj.getQ_name().isEmpty())
                {
                    names.add(obj.getQ_name());
                }
            }
        }

        if (surnames.isEmpty())
        {
            throw new ServerException("Robot random surnames is empty!");
        }
        if (names.isEmpty())
        {
            throw new ServerException("Robot random names is empty!");
        }
    }

    public static synchronized String getRandomName()
    {
        if (surnames.size() > 0 && names.size() > 0)
        {
            return surnames.get(random.next(1, surnames.size()) - 1)
                    + names.get(random.next(1, names.size()) - 1);
        }
        else
        {
            return null;
        }
    }
}
