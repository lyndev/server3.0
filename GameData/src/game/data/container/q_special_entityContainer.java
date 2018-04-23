/**
 * Auto generated, do not edit it
 *
 * q_special_entity
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_special_entityBean;
import game.data.dao.q_special_entityDao;

public class q_special_entityContainer
{
    private List<q_special_entityBean> list;
    private final Map<Integer, q_special_entityBean> map = new HashMap<>();
    private final q_special_entityDao dao = new q_special_entityDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_special_entityBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_special_entityBean bean = (q_special_entityBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_special_entityBean> getList()
    {
        return list;
    }

    public Map<Integer, q_special_entityBean> getMap()
    {
        return map;
    }
}
