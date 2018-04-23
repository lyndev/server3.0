/**
 * Auto generated, do not edit it
 *
 * q_fight_power
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_fight_powerBean;

public class q_fight_powerDao
{
    public List<q_fight_powerBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_fight_powerBean> list = session.selectList("q_fight_power.selectAll");
            return list;
        }
    }
}
