/**
 * Auto generated, do not edit it
 *
 * q_monster_pos
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_monster_posBean;

public class q_monster_posDao
{
    public List<q_monster_posBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_monster_posBean> list = session.selectList("q_monster_pos.selectAll");
            return list;
        }
    }
}
