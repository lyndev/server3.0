/**
 * Auto generated, do not edit it
 *
 * q_effect
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_effectBean;

public class q_effectDao
{
    public List<q_effectBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_effectBean> list = session.selectList("q_effect.selectAll");
            return list;
        }
    }
}
