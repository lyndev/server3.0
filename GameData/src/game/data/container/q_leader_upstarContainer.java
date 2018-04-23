/**
 * Auto generated, do not edit it
 *
 * q_leader_upstar
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_leader_upstarBean;
import game.data.dao.q_leader_upstarDao;

public class q_leader_upstarContainer
{
    private List<q_leader_upstarBean> list;
    private final Map<Integer, q_leader_upstarBean> map = new HashMap<>();
    private final q_leader_upstarDao dao = new q_leader_upstarDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_leader_upstarBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_leader_upstarBean bean = (q_leader_upstarBean) iter.next();
            map.put(bean.getQ_order(), bean);
        }
    }

    public List<q_leader_upstarBean> getList()
    {
        return list;
    }

    public Map<Integer, q_leader_upstarBean> getMap()
    {
        return map;
    }
}
