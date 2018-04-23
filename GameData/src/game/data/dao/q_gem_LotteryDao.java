/**
 * Auto generated, do not edit it
 *
 * q_gem_Lottery
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_gem_LotteryBean;

public class q_gem_LotteryDao
{
    public List<q_gem_LotteryBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_gem_LotteryBean> list = session.selectList("q_gem_Lottery.selectAll");
            return list;
        }
    }
}
