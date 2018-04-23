/**
 * Auto generated, do not edit it
 *
 * q_gold_Lottery
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_gold_LotteryBean;

public class q_gold_LotteryDao
{
    public List<q_gold_LotteryBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_gold_LotteryBean> list = session.selectList("q_gold_Lottery.selectAll");
            return list;
        }
    }
}
