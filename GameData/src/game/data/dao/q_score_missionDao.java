/**
 * Auto generated, do not edit it
 *
 * q_score_mission
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_score_missionBean;

public class q_score_missionDao
{
    public List<q_score_missionBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_score_missionBean> list = session.selectList("q_score_mission.selectAll");
            return list;
        }
    }
}
