/**
 * Auto generated, do not edit it
 *
 * q_level_package
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_level_packageBean;

public class q_level_packageDao
{
    public List<q_level_packageBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_level_packageBean> list = session.selectList("q_level_package.selectAll");
            return list;
        }
    }
}
