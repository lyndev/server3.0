/**
 * Auto generated, do not edit it
 *
 * q_npc_monster
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_npc_monsterBean;

public class q_npc_monsterDao
{
    public List<q_npc_monsterBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_npc_monsterBean> list = session.selectList("q_npc_monster.selectAll");
            return list;
        }
    }
}
