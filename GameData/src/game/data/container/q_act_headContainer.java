/**
 * Auto generated, do not edit it
 *
 * q_act_head
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_act_headBean;
import game.data.dao.q_act_headDao;

public class q_act_headContainer
{
    private List<q_act_headBean> list;
    private final Map<Integer, q_act_headBean> map = new HashMap<>();
    private final q_act_headDao dao = new q_act_headDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_act_headBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_act_headBean bean = (q_act_headBean) iter.next();
            map.put(bean.getQ_acthead_id(), bean);
        }
    }

    public List<q_act_headBean> getList()
    {
        return list;
    }

    public Map<Integer, q_act_headBean> getMap()
    {
        return map;
    }
}
