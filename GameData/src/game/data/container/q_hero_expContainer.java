/**
 * Auto generated, do not edit it
 *
 * q_hero_exp
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_hero_expBean;
import game.data.dao.q_hero_expDao;

public class q_hero_expContainer
{
    private List<q_hero_expBean> list;
    private final Map<Integer, q_hero_expBean> map = new HashMap<>();
    private final q_hero_expDao dao = new q_hero_expDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_hero_expBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_hero_expBean bean = (q_hero_expBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_hero_expBean> getList()
    {
        return list;
    }

    public Map<Integer, q_hero_expBean> getMap()
    {
        return map;
    }
}
