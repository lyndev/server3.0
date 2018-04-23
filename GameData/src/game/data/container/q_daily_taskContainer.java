/**
 * Auto generated, do not edit it
 *
 * q_daily_task
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_daily_taskBean;
import game.data.dao.q_daily_taskDao;

public class q_daily_taskContainer
{
    private List<q_daily_taskBean> list;
    private final Map<Integer, q_daily_taskBean> map = new HashMap<>();
    private final q_daily_taskDao dao = new q_daily_taskDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_daily_taskBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_daily_taskBean bean = (q_daily_taskBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_daily_taskBean> getList()
    {
        return list;
    }

    public Map<Integer, q_daily_taskBean> getMap()
    {
        return map;
    }
}
