/**
 * Auto generated, do not edit it
 *
 * q_act_name
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_act_nameBean;
import game.data.dao.q_act_nameDao;

public class q_act_nameContainer
{
    private List<q_act_nameBean> list;
    private final Map<Integer, q_act_nameBean> map = new HashMap<>();
    private final q_act_nameDao dao = new q_act_nameDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_act_nameBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_act_nameBean bean = (q_act_nameBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_act_nameBean> getList()
    {
        return list;
    }

    public Map<Integer, q_act_nameBean> getMap()
    {
        return map;
    }
}
