/**
 * Auto generated, do not edit it
 *
 * q_expedition
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_expeditionBean;
import game.data.dao.q_expeditionDao;

public class q_expeditionContainer
{
    private List<q_expeditionBean> list;
    private final Map<Integer, q_expeditionBean> map = new HashMap<>();
    private final q_expeditionDao dao = new q_expeditionDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_expeditionBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_expeditionBean bean = (q_expeditionBean) iter.next();
            map.put(bean.getQ_level_order_id(), bean);
        }
    }

    public List<q_expeditionBean> getList()
    {
        return list;
    }

    public Map<Integer, q_expeditionBean> getMap()
    {
        return map;
    }
}
