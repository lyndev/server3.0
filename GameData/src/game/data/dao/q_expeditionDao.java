/**
 * Auto generated, do not edit it
 *
 * q_expedition
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_expeditionBean;

public class q_expeditionDao
{
    public List<q_expeditionBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_expeditionBean> list = session.selectList("q_expedition.selectAll");
            return list;
        }
    }
}
