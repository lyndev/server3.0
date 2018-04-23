/**
 * Auto generated, do not edit it
 *
 * q_language
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_languageBean;

public class q_languageDao
{
    public List<q_languageBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_languageBean> list = session.selectList("q_language.selectAll");
            return list;
        }
    }
}
