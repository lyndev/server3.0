/**
 * Auto generated, do not edit it
 *
 * q_buff_new
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_buff_newBean;

public class q_buff_newDao
{
    public List<q_buff_newBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_buff_newBean> list = session.selectList("q_buff_new.selectAll");
            return list;
        }
    }
}
