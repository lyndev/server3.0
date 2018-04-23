/**
 * Auto generated, do not edit it
 *
 * q_monster_level_attribute
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_monster_level_attributeBean;

public class q_monster_level_attributeDao
{
    public List<q_monster_level_attributeBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_monster_level_attributeBean> list = session.selectList("q_monster_level_attribute.selectAll");
            return list;
        }
    }
}
