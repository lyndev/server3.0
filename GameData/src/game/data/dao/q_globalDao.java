/**
 * Auto generated, do not edit it
 *
 * q_global
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_globalBean;

public class q_globalDao
{
    public List<q_globalBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_globalBean> list = session.selectList("q_global.selectAll");
            return list;
        }
    }
}
