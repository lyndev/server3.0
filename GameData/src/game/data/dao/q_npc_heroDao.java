/**
 * Auto generated, do not edit it
 *
 * q_npc_hero
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_npc_heroBean;

public class q_npc_heroDao
{
    public List<q_npc_heroBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_npc_heroBean> list = session.selectList("q_npc_hero.selectAll");
            System.out.print("1111");
            return list;
        }
    }
}
