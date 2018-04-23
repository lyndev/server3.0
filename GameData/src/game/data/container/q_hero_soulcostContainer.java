/**
 * Auto generated, do not edit it
 *
 * q_hero_soulcost
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_hero_soulcostBean;
import game.data.dao.q_hero_soulcostDao;

public class q_hero_soulcostContainer
{
    private List<q_hero_soulcostBean> list;
    private final Map<Integer, q_hero_soulcostBean> map = new HashMap<>();
    private final q_hero_soulcostDao dao = new q_hero_soulcostDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_hero_soulcostBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_hero_soulcostBean bean = (q_hero_soulcostBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_hero_soulcostBean> getList()
    {
        return list;
    }

    public Map<Integer, q_hero_soulcostBean> getMap()
    {
        return map;
    }
}
