/**
 * Auto generated, do not edit it
 *
 * q_global
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_globalBean;
import game.data.dao.q_globalDao;

public class q_globalContainer
{
    private List<q_globalBean> list;
    private final Map<Integer, q_globalBean> map = new HashMap<>();
    private final q_globalDao dao = new q_globalDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_globalBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_globalBean bean = (q_globalBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_globalBean> getList()
    {
        return list;
    }

    public Map<Integer, q_globalBean> getMap()
    {
        return map;
    }
}
