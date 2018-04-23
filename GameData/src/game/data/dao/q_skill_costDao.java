/**
 * Auto generated, do not edit it
 *
 * q_skill_cost
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_skill_costBean;

public class q_skill_costDao
{
    public List<q_skill_costBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_skill_costBean> list = session.selectList("q_skill_cost.selectAll");
            return list;
        }
    }
}
