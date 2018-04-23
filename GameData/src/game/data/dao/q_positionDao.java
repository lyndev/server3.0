/**
 * Auto generated, do not edit it
 *
 * q_position
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_positionBean;

public class q_positionDao
{
    public List<q_positionBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_positionBean> list = session.selectList("q_position.selectAll");
            return list;
        }
    }
}
