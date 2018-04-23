/**
 * Auto generated, do not edit it
 *
 * q_world_boss
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_world_bossBean;
import game.data.dao.q_world_bossDao;

public class q_world_bossContainer
{
    private List<q_world_bossBean> list;
    private final Map<Integer, q_world_bossBean> map = new HashMap<>();
    private final q_world_bossDao dao = new q_world_bossDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_world_bossBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_world_bossBean bean = (q_world_bossBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_world_bossBean> getList()
    {
        return list;
    }

    public Map<Integer, q_world_bossBean> getMap()
    {
        return map;
    }
}
