/**
 * Auto generated, do not edit it
 *
 * q_npc_summon
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_npc_summonBean;
import game.data.dao.q_npc_summonDao;

public class q_npc_summonContainer
{
    private List<q_npc_summonBean> list;
    private final Map<Integer, q_npc_summonBean> map = new HashMap<>();
    private final q_npc_summonDao dao = new q_npc_summonDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_npc_summonBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_npc_summonBean bean = (q_npc_summonBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_npc_summonBean> getList()
    {
        return list;
    }

    public Map<Integer, q_npc_summonBean> getMap()
    {
        return map;
    }
}
