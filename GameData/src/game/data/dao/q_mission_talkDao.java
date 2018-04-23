/**
 * Auto generated, do not edit it
 *
 * q_mission_talk
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_mission_talkBean;

public class q_mission_talkDao
{
    public List<q_mission_talkBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_mission_talkBean> list = session.selectList("q_mission_talk.selectAll");
            return list;
        }
    }
}
