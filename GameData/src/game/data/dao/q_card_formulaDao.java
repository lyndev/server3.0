/**
 * Auto generated, do not edit it
 *
 * q_card_formula
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_card_formulaBean;

public class q_card_formulaDao
{
    public List<q_card_formulaBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_card_formulaBean> list = session.selectList("q_card_formula.selectAll");
            return list;
        }
    }
}
