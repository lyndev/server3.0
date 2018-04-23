/**
 * Auto generated, do not edit it
 *
 * q_score_reward
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_score_rewardBean;

public class q_score_rewardDao
{
    public List<q_score_rewardBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_score_rewardBean> list = session.selectList("q_score_reward.selectAll");
            return list;
        }
    }
}
