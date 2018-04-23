/**
 * Auto generated, do not edit it
 *
 * q_item
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_itemBean;
import game.data.dao.q_itemDao;

public class q_itemContainer
{
    private List<q_itemBean> list;
    private final Map<Integer, q_itemBean> map = new HashMap<>();
    private final q_itemDao dao = new q_itemDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_itemBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_itemBean bean = (q_itemBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_itemBean> getList()
    {
        return list;
    }

    public Map<Integer, q_itemBean> getMap()
    {
        return map;
    }
}
