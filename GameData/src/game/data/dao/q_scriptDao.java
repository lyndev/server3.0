/**
 * Auto generated, do not edit it
 *
 * q_script
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_scriptBean;

public class q_scriptDao
{
    public List<q_scriptBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_scriptBean> list = session.selectList("q_script.selectAll");
            return list;
        }
    }
}
