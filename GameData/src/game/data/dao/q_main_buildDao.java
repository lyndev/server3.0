/**
 * Auto generated, do not edit it
 *
 * q_main_build
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_main_buildBean;

public class q_main_buildDao
{
    public List<q_main_buildBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_main_buildBean> list = session.selectList("q_main_build.selectAll");
            return list;
        }
    }
}
