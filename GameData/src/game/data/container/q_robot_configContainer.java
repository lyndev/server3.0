/**
 * Auto generated, do not edit it
 *
 * q_robot_config
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_robot_configBean;
import game.data.dao.q_robot_configDao;

public class q_robot_configContainer
{
    private List<q_robot_configBean> list;
    private final Map<Integer, q_robot_configBean> map = new HashMap<>();
    private final q_robot_configDao dao = new q_robot_configDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_robot_configBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_robot_configBean bean = (q_robot_configBean) iter.next();
            map.put(bean.getQ_initial_ranking(), bean);
        }
    }

    public List<q_robot_configBean> getList()
    {
        return list;
    }

    public Map<Integer, q_robot_configBean> getMap()
    {
        return map;
    }
}
