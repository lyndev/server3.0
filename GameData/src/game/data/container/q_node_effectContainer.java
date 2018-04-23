/**
 * Auto generated, do not edit it
 *
 * q_node_effect
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_node_effectBean;
import game.data.dao.q_node_effectDao;

public class q_node_effectContainer
{
    private List<q_node_effectBean> list;
    private final Map<Integer, q_node_effectBean> map = new HashMap<>();
    private final q_node_effectDao dao = new q_node_effectDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_node_effectBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_node_effectBean bean = (q_node_effectBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_node_effectBean> getList()
    {
        return list;
    }

    public Map<Integer, q_node_effectBean> getMap()
    {
        return map;
    }
}
