/**
 * Auto generated, do not edit it
 *
 * q_storeConfig
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_storeConfigBean;

public class q_storeConfigDao
{
    public List<q_storeConfigBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_storeConfigBean> list = session.selectList("q_storeConfig.selectAll");
            return list;
        }
    }
}
