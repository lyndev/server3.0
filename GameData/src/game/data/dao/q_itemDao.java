/**
 * Auto generated, do not edit it
 *
 * q_item
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_itemBean;

public class q_itemDao
{
    public List<q_itemBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_itemBean> list = session.selectList("q_item.selectAll");
            return list;
        }
    }
}
