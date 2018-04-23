/**
 * Auto generated, do not edit it
 *
 * q_color_attr
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_color_attrBean;

public class q_color_attrDao
{
    public List<q_color_attrBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_color_attrBean> list = session.selectList("q_color_attr.selectAll");
            return list;
        }
    }
}
