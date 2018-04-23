/**
 * Auto generated, do not edit it
 *
 * q_box_items
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_box_itemsBean;
import game.data.dao.q_box_itemsDao;

public class q_box_itemsContainer
{
    private List<q_box_itemsBean> list;
    private final Map<Integer, q_box_itemsBean> map = new HashMap<>();
    private final q_box_itemsDao dao = new q_box_itemsDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_box_itemsBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_box_itemsBean bean = (q_box_itemsBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_box_itemsBean> getList()
    {
        return list;
    }

    public Map<Integer, q_box_itemsBean> getMap()
    {
        return map;
    }
}
