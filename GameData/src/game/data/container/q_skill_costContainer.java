/**
 * Auto generated, do not edit it
 *
 * q_skill_cost
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_skill_costBean;
import game.data.dao.q_skill_costDao;

public class q_skill_costContainer
{
    private List<q_skill_costBean> list;
    private final Map<Integer, q_skill_costBean> map = new HashMap<>();
    private final q_skill_costDao dao = new q_skill_costDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_skill_costBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_skill_costBean bean = (q_skill_costBean) iter.next();
            map.put(bean.getQ_skill_level(), bean);
        }
    }

    public List<q_skill_costBean> getList()
    {
        return list;
    }

    public Map<Integer, q_skill_costBean> getMap()
    {
        return map;
    }
}
