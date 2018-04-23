/**
 * Auto generated, do not edit it
 *
 * q_demon_soul
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_demon_soulBean;

public class q_demon_soulDao
{
    public List<q_demon_soulBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_demon_soulBean> list = session.selectList("q_demon_soul.selectAll");
            return list;
        }
    }
}
