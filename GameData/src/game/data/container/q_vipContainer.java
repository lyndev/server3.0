/**
 * Auto generated, do not edit it
 *
 * q_vip
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_vipBean;
import game.data.dao.q_vipDao;

public class q_vipContainer
{
    private List<q_vipBean> list;
    private final Map<Integer, q_vipBean> map = new HashMap<>();
    private final q_vipDao dao = new q_vipDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_vipBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_vipBean bean = (q_vipBean) iter.next();
            map.put(bean.getQ_level(), bean);
        }
    }

    public List<q_vipBean> getList()
    {
        return list;
    }

    public Map<Integer, q_vipBean> getMap()
    {
        return map;
    }
}
