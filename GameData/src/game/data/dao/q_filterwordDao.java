/**
 * Auto generated, do not edit it
 *
 * q_filterword
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_filterwordBean;

public class q_filterwordDao
{
    public List<q_filterwordBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_filterwordBean> list = session.selectList("q_filterword.selectAll");
            return list;
        }
    }
}
