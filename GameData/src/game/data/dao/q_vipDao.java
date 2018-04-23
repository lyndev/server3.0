/**
 * Auto generated, do not edit it
 *
 * q_vip
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_vipBean;

public class q_vipDao
{
    public List<q_vipBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_vipBean> list = session.selectList("q_vip.selectAll");
            return list;
        }
    }
}
