/**
 * Auto generated, do not edit it
 *
 * q_main_role
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_main_roleBean;

public class q_main_roleDao
{
    public List<q_main_roleBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_main_roleBean> list = session.selectList("q_main_role.selectAll");
            return list;
        }
    }
}
