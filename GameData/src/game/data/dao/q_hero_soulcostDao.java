/**
 * Auto generated, do not edit it
 *
 * q_hero_soulcost
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_hero_soulcostBean;

public class q_hero_soulcostDao
{
    public List<q_hero_soulcostBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_hero_soulcostBean> list = session.selectList("q_hero_soulcost.selectAll");
            return list;
        }
    }
}
