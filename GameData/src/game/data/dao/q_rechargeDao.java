/**
 * Auto generated, do not edit it
 *
 * q_recharge
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_rechargeBean;

public class q_rechargeDao
{
    public List<q_rechargeBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_rechargeBean> list = session.selectList("q_recharge.selectAll");
            return list;
        }
    }
}
