/**
 * Auto generated, do not edit it
 *
 * q_mission_chapter
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_mission_chapterBean;

public class q_mission_chapterDao
{
    public List<q_mission_chapterBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_mission_chapterBean> list = session.selectList("q_mission_chapter.selectAll");
            return list;
        }
    }
}
