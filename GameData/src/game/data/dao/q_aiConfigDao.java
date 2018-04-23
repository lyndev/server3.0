/**
 * Auto generated, do not edit it
 *
 * q_aiConfig
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_aiConfigBean;

public class q_aiConfigDao
{
    public List<q_aiConfigBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_aiConfigBean> list = session.selectList("q_aiConfig.selectAll");
            return list;
        }
    }
}
