/**
 * Auto generated, do not edit it
 *
 * q_resource_produce_monit
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_resource_produce_monitBean;
import game.data.dao.q_resource_produce_monitDao;

public class q_resource_produce_monitContainer
{
    private List<q_resource_produce_monitBean> list;
    private final Map<Integer, q_resource_produce_monitBean> map = new HashMap<>();
    private final q_resource_produce_monitDao dao = new q_resource_produce_monitDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_resource_produce_monitBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_resource_produce_monitBean bean = (q_resource_produce_monitBean) iter.next();
            map.put(bean.getQ_res_type(), bean);
        }
    }

    public List<q_resource_produce_monitBean> getList()
    {
        return list;
    }

    public Map<Integer, q_resource_produce_monitBean> getMap()
    {
        return map;
    }
}
