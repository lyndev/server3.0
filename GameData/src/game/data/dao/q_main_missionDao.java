/**
 * Auto generated, do not edit it
 *
 * q_main_mission
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_main_missionBean;

public class q_main_missionDao
{
    public List<q_main_missionBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_main_missionBean> list = session.selectList("q_main_mission.selectAll");
            return list;
        }
    }
}
