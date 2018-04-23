/**
 * Auto generated, do not edit it
 *
 * q_resource_mission
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_resource_missionBean;

public class q_resource_missionDao
{
    public List<q_resource_missionBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_resource_missionBean> list = session.selectList("q_resource_mission.selectAll");
            return list;
        }
    }
}
