/**
 * Auto generated, do not edit it
 *
 * q_act_name
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_act_nameBean;

public class q_act_nameDao
{
    public List<q_act_nameBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_act_nameBean> list = session.selectList("q_act_name.selectAll");
            return list;
        }
    }
}
