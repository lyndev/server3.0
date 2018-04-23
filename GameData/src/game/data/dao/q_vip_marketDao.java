/**
 * Auto generated, do not edit it
 *
 * q_vip_market
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_vip_marketBean;

public class q_vip_marketDao
{
    public List<q_vip_marketBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_vip_marketBean> list = session.selectList("q_vip_market.selectAll");
            return list;
        }
    }
}
