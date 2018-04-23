/**
 * Auto generated, do not edit it
 *
 * q_resource_produce_monit
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_resource_produce_monitBean;

public class q_resource_produce_monitDao
{
    public List<q_resource_produce_monitBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_resource_produce_monitBean> list = session.selectList("q_resource_produce_monit.selectAll");
            return list;
        }
    }
}
