/**
 * Auto generated, do not edit it
 *
 * q_world_boss
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_world_bossBean;

public class q_world_bossDao
{
    public List<q_world_bossBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_world_bossBean> list = session.selectList("q_world_boss.selectAll");
            return list;
        }
    }
}
