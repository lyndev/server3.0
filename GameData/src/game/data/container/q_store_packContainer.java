/**
 * Auto generated, do not edit it
 *
 * q_store_pack
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_store_packBean;
import game.data.dao.q_store_packDao;

public class q_store_packContainer
{
    private List<q_store_packBean> list;
    private final Map<Integer, q_store_packBean> map = new HashMap<>();
    private final q_store_packDao dao = new q_store_packDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_store_packBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_store_packBean bean = (q_store_packBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_store_packBean> getList()
    {
        return list;
    }

    public Map<Integer, q_store_packBean> getMap()
    {
        return map;
    }
}
