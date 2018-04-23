/**
 * Auto generated, do not edit it
 *
 * q_effect
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_effectBean;
import game.data.dao.q_effectDao;

public class q_effectContainer
{
    private List<q_effectBean> list;
    private final Map<Integer, q_effectBean> map = new HashMap<>();
    private final q_effectDao dao = new q_effectDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_effectBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_effectBean bean = (q_effectBean) iter.next();
            map.put(bean.getQ_effect_id(), bean);
        }
    }

    public List<q_effectBean> getList()
    {
        return list;
    }

    public Map<Integer, q_effectBean> getMap()
    {
        return map;
    }
}
