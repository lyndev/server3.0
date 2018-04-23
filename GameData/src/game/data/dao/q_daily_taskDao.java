/**
 * Auto generated, do not edit it
 *
 * q_daily_task
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_daily_taskBean;

public class q_daily_taskDao
{
    public List<q_daily_taskBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_daily_taskBean> list = session.selectList("q_daily_task.selectAll");
            return list;
        }
    }
}
