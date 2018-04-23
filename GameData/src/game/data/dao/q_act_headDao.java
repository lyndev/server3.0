/**
 * Auto generated, do not edit it
 *
 * q_act_head
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_act_headBean;

public class q_act_headDao
{
    public List<q_act_headBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_act_headBean> list = session.selectList("q_act_head.selectAll");
            return list;
        }
    }
}
