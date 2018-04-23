/**
 * Auto generated, do not edit it
 *
 * q_arena_ranking_reward
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_arena_ranking_rewardBean;

public class q_arena_ranking_rewardDao
{
    public List<q_arena_ranking_rewardBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_arena_ranking_rewardBean> list = session.selectList("q_arena_ranking_reward.selectAll");
            return list;
        }
    }
}
