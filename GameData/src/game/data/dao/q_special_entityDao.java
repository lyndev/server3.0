/**
 * Auto generated, do not edit it
 *
 * q_special_entity
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_special_entityBean;

public class q_special_entityDao
{
    public List<q_special_entityBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_special_entityBean> list = session.selectList("q_special_entity.selectAll");
            return list;
        }
    }
}
