/**
 * Auto generated, do not edit it
 *
 * q_buy_cost
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_buy_costBean;

public class q_buy_costDao
{
    public List<q_buy_costBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_buy_costBean> list = session.selectList("q_buy_cost.selectAll");
            return list;
        }
    }
}
