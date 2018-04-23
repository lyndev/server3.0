/**
 * Auto generated, do not edit it
 *
 * q_exchange_coin
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_exchange_coinBean;

public class q_exchange_coinDao
{
    public List<q_exchange_coinBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_exchange_coinBean> list = session.selectList("q_exchange_coin.selectAll");
            return list;
        }
    }
}
