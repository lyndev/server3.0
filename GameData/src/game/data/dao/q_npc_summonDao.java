/**
 * Auto generated, do not edit it
 *
 * q_npc_summon
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_npc_summonBean;

public class q_npc_summonDao
{
    public List<q_npc_summonBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_npc_summonBean> list = session.selectList("q_npc_summon.selectAll");
            return list;
        }
    }
}
