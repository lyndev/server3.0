/**
 * Auto generated, do not edit it
 *
 * q_item_produce_monit
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_item_produce_monitBean;
import game.data.dao.q_item_produce_monitDao;

public class q_item_produce_monitContainer
{
    private List<q_item_produce_monitBean> list;
    private final Map<Integer, q_item_produce_monitBean> map = new HashMap<>();
    private final q_item_produce_monitDao dao = new q_item_produce_monitDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_item_produce_monitBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_item_produce_monitBean bean = (q_item_produce_monitBean) iter.next();
            map.put(bean.getQ_item_id(), bean);
        }
    }

    public List<q_item_produce_monitBean> getList()
    {
        return list;
    }

    public Map<Integer, q_item_produce_monitBean> getMap()
    {
        return map;
    }
}
