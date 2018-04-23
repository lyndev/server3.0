/**
 * Auto generated, do not edit it
 *
 * q_control
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_controlBean;

public class q_controlDao
{
    public List<q_controlBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_controlBean> list = session.selectList("q_control.selectAll");
            return list;
        }
    }
}
