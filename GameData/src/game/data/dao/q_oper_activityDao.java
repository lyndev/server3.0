/**
 * Auto generated, do not edit it
 *
 * q_oper_activity
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_oper_activityBean;

public class q_oper_activityDao
{
    public List<q_oper_activityBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_oper_activityBean> list = session.selectList("q_oper_activity.selectAll");
            return list;
        }
    }
}
