/**
 * Auto generated, do not edit it
 *
 * q_guide
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_guideBean;

public class q_guideDao
{
    public List<q_guideBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_guideBean> list = session.selectList("q_guide.selectAll");
            return list;
        }
    }
}
