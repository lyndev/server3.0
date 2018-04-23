/**
 * Auto generated, do not edit it
 *
 * q_skill_buff_level
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_skill_buff_levelBean;

public class q_skill_buff_levelDao
{
    public List<q_skill_buff_levelBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_skill_buff_levelBean> list = session.selectList("q_skill_buff_level.selectAll");
            return list;
        }
    }
}
