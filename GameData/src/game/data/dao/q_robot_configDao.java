/**
 * Auto generated, do not edit it
 *
 * q_robot_config
 */
package game.data.dao;

import game.data.GameDataManager;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import game.data.bean.q_robot_configBean;

public class q_robot_configDao
{
    public List<q_robot_configBean> select()
    {
        try
        (SqlSession session = GameDataManager.getInstance().getSqlSessionFactory().openSession())
        {
            List<q_robot_configBean> list = session.selectList("q_robot_config.selectAll");
            return list;
        }
    }
}
