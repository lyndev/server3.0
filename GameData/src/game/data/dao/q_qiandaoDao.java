/**
 * Auto generated, do not edit it
 *
 * q_qiandao
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_qiandaoBean;

public class q_qiandaoDao
{
    public List<q_qiandaoBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_qiandaoBean> list = session.selectList("q_qiandao.selectAll");
            return list;
        }
    }
}
