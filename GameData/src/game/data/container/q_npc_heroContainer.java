/**
 * Auto generated, do not edit it
 *
 * q_npc_hero
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_npc_heroBean;
import game.data.dao.q_npc_heroDao;

public class q_npc_heroContainer
{
    private List<q_npc_heroBean> list;
    private final Map<Integer, q_npc_heroBean> map = new HashMap<>();
    private final q_npc_heroDao dao = new q_npc_heroDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_npc_heroBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_npc_heroBean bean = (q_npc_heroBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_npc_heroBean> getList()
    {
        return list;
    }

    public Map<Integer, q_npc_heroBean> getMap()
    {
        return map;
    }
}
