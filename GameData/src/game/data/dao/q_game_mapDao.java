/**
 * Auto generated, do not edit it
 *
 * q_game_map
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_game_mapBean;

public class q_game_mapDao
{
    public List<q_game_mapBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_game_mapBean> list = session.selectList("q_game_map.selectAll");
            return list;
        }
    }
}
