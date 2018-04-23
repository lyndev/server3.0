/**
 * Auto generated, do not edit it
 *
 * q_skill
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_skillBean;

public class q_skillDao
{
    public List<q_skillBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_skillBean> list = session.selectList("q_skill.selectAll");
            return list;
        }
    }
}
