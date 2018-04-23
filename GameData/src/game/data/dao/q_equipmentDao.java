/**
 * Auto generated, do not edit it
 *
 * q_equipment
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_equipmentBean;

public class q_equipmentDao
{
    public List<q_equipmentBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_equipmentBean> list = session.selectList("q_equipment.selectAll");
            return list;
        }
    }
}
