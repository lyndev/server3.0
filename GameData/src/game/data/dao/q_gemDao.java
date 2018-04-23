/**
 * Auto generated, do not edit it
 *
 * q_gem
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_gemBean;

public class q_gemDao
{
    public List<q_gemBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_gemBean> list = session.selectList("q_gem.selectAll");
            return list;
        }
    }
}
