/**
 * Auto generated, do not edit it
 *
 * q_fight_power
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_fight_powerBean;
import game.data.dao.q_fight_powerDao;

public class q_fight_powerContainer
{
    private List<q_fight_powerBean> list;
    private final Map<Integer, q_fight_powerBean> map = new HashMap<>();
    private final q_fight_powerDao dao = new q_fight_powerDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_fight_powerBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_fight_powerBean bean = (q_fight_powerBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_fight_powerBean> getList()
    {
        return list;
    }

    public Map<Integer, q_fight_powerBean> getMap()
    {
        return map;
    }
}
