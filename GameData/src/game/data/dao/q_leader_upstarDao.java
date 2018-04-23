/**
 * Auto generated, do not edit it
 *
 * q_leader_upstar
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_leader_upstarBean;

public class q_leader_upstarDao
{
    public List<q_leader_upstarBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_leader_upstarBean> list = session.selectList("q_leader_upstar.selectAll");
            return list;
        }
    }
}
