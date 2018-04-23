/**
 * Auto generated, do not edit it
 *
 * q_expedition_node_group
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_expedition_node_groupBean;
import game.data.dao.q_expedition_node_groupDao;

public class q_expedition_node_groupContainer
{
    private List<q_expedition_node_groupBean> list;
    private final Map<Integer, q_expedition_node_groupBean> map = new HashMap<>();
    private final q_expedition_node_groupDao dao = new q_expedition_node_groupDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_expedition_node_groupBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_expedition_node_groupBean bean = (q_expedition_node_groupBean) iter.next();
            map.put(bean.getQ_player_level(), bean);
        }
    }

    public List<q_expedition_node_groupBean> getList()
    {
        return list;
    }

    public Map<Integer, q_expedition_node_groupBean> getMap()
    {
        return map;
    }
}
