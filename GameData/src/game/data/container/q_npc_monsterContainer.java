/**
 * Auto generated, do not edit it
 *
 * q_npc_monster
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_npc_monsterBean;
import game.data.dao.q_npc_monsterDao;

public class q_npc_monsterContainer
{
    private List<q_npc_monsterBean> list;
    private final Map<Integer, q_npc_monsterBean> map = new HashMap<>();
    private final q_npc_monsterDao dao = new q_npc_monsterDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_npc_monsterBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_npc_monsterBean bean = (q_npc_monsterBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_npc_monsterBean> getList()
    {
        return list;
    }

    public Map<Integer, q_npc_monsterBean> getMap()
    {
        return map;
    }
}
