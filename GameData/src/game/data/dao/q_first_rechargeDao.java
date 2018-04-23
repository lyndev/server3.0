/**
 * Auto generated, do not edit it
 *
 * q_first_recharge
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_first_rechargeBean;

public class q_first_rechargeDao
{
    public List<q_first_rechargeBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_first_rechargeBean> list = session.selectList("q_first_recharge.selectAll");
            return list;
        }
    }
}
