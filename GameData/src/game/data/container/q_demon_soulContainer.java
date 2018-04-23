/**
 * Auto generated, do not edit it
 *
 * q_demon_soul
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_demon_soulBean;
import game.data.dao.q_demon_soulDao;

public class q_demon_soulContainer
{
    private List<q_demon_soulBean> list;
    private final Map<Integer, q_demon_soulBean> map = new HashMap<>();
    private final q_demon_soulDao dao = new q_demon_soulDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_demon_soulBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_demon_soulBean bean = (q_demon_soulBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_demon_soulBean> getList()
    {
        return list;
    }

    public Map<Integer, q_demon_soulBean> getMap()
    {
        return map;
    }
}
