/**
 * Auto generated, do not edit it
 *
 * q_store_pack
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_store_packBean;

public class q_store_packDao
{
    public List<q_store_packBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_store_packBean> list = session.selectList("q_store_pack.selectAll");
            return list;
        }
    }
}
