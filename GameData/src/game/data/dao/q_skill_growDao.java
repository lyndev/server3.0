/**
 * Auto generated, do not edit it
 *
 * q_skill_grow
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_skill_growBean;

public class q_skill_growDao
{
    public List<q_skill_growBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_skill_growBean> list = session.selectList("q_skill_grow.selectAll");
            return list;
        }
    }
}
