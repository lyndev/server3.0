/**
 * Auto generated, do not edit it
 *
 * q_box_items
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_box_itemsBean;

public class q_box_itemsDao
{
    public List<q_box_itemsBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_box_itemsBean> list = session.selectList("q_box_items.selectAll");
            return list;
        }
    }
}
