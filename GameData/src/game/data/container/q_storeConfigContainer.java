/**
 * Auto generated, do not edit it
 *
 * q_storeConfig
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_storeConfigBean;
import game.data.dao.q_storeConfigDao;

public class q_storeConfigContainer
{
    private List<q_storeConfigBean> list;
    private final Map<Integer, q_storeConfigBean> map = new HashMap<>();
    private final q_storeConfigDao dao = new q_storeConfigDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_storeConfigBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_storeConfigBean bean = (q_storeConfigBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_storeConfigBean> getList()
    {
        return list;
    }

    public Map<Integer, q_storeConfigBean> getMap()
    {
        return map;
    }
}
