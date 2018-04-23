/**
 * Auto generated, do not edit it
 *
 * q_special_equip
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_special_equipBean;

public class q_special_equipDao
{
    public List<q_special_equipBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_special_equipBean> list = session.selectList("q_special_equip.selectAll");
            return list;
        }
    }
}
