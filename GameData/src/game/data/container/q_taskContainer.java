/**
 * Auto generated, do not edit it
 *
 * q_task
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_taskBean;
import game.data.dao.q_taskDao;

public class q_taskContainer
{
    private List<q_taskBean> list;
    private final Map<Integer, q_taskBean> map = new HashMap<>();
    private final q_taskDao dao = new q_taskDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_taskBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_taskBean bean = (q_taskBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_taskBean> getList()
    {
        return list;
    }

    public Map<Integer, q_taskBean> getMap()
    {
        return map;
    }
}
