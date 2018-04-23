/**
 * Auto generated, do not edit it
 *
 * q_task
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_taskBean;

public class q_taskDao
{
    public List<q_taskBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_taskBean> list = session.selectList("q_task.selectAll");
            return list;
        }
    }
}
