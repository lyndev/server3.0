/**
 * Auto generated, do not edit it
 *
 * q_oper_activity
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_oper_activityBean;
import game.data.dao.q_oper_activityDao;

public class q_oper_activityContainer
{
    private List<q_oper_activityBean> list;
    private final Map<Integer, q_oper_activityBean> map = new HashMap<>();
    private final q_oper_activityDao dao = new q_oper_activityDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_oper_activityBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_oper_activityBean bean = (q_oper_activityBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_oper_activityBean> getList()
    {
        return list;
    }

    public Map<Integer, q_oper_activityBean> getMap()
    {
        return map;
    }
}
