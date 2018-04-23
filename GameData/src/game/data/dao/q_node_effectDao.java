/**
 * Auto generated, do not edit it
 *
 * q_node_effect
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_node_effectBean;

public class q_node_effectDao
{
    public List<q_node_effectBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_node_effectBean> list = session.selectList("q_node_effect.selectAll");
            return list;
        }
    }
}
