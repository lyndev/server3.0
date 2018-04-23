/**
 * Auto generated, do not edit it
 *
 * q_equipment
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_equipmentBean;
import game.data.dao.q_equipmentDao;

public class q_equipmentContainer
{
    private List<q_equipmentBean> list;
    private final Map<Integer, q_equipmentBean> map = new HashMap<>();
    private final q_equipmentDao dao = new q_equipmentDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_equipmentBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_equipmentBean bean = (q_equipmentBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_equipmentBean> getList()
    {
        return list;
    }

    public Map<Integer, q_equipmentBean> getMap()
    {
        return map;
    }
}
