/**
 * Auto generated, do not edit it
 *
 * q_action_time
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_action_timeBean;

public class q_action_timeDao
{
    public List<q_action_timeBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_action_timeBean> list = session.selectList("q_action_time.selectAll");
            return list;
        }
    }
}
