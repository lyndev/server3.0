/**
 * Auto generated, do not edit it
 *
 * q_box
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_boxBean;

public class q_boxDao
{
    public List<q_boxBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_boxBean> list = session.selectList("q_box.selectAll");
            return list;
        }
    }
}
