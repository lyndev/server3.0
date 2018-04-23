/**
 * Auto generated, do not edit it
 *
 * q_action_time
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_action_timeBean;
import game.data.dao.q_action_timeDao;

public class q_action_timeContainer
{
    private List<q_action_timeBean> list;
    private final Map<Integer, q_action_timeBean> map = new HashMap<>();
    private final q_action_timeDao dao = new q_action_timeDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_action_timeBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_action_timeBean bean = (q_action_timeBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_action_timeBean> getList()
    {
        return list;
    }

    public Map<Integer, q_action_timeBean> getMap()
    {
        return map;
    }
}
