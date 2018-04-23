/**
 * Auto generated, do not edit it
 *
 * q_expedition_node_group
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_expedition_node_groupBean;

public class q_expedition_node_groupDao
{
    public List<q_expedition_node_groupBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_expedition_node_groupBean> list = session.selectList("q_expedition_node_group.selectAll");
            return list;
        }
    }
}
