/**
 * Auto generated, do not edit it
 *
 * q_hero_exp
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_hero_expBean;

public class q_hero_expDao
{
    public List<q_hero_expBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_hero_expBean> list = session.selectList("q_hero_exp.selectAll");
            return list;
        }
    }
}
