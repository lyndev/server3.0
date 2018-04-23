/**
 * Auto generated, do not edit it
 *
 * q_skill
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_skillBean;
import game.data.dao.q_skillDao;

public class q_skillContainer
{
    private List<q_skillBean> list;
    private final Map<Integer, q_skillBean> map = new HashMap<>();
    private final q_skillDao dao = new q_skillDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_skillBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_skillBean bean = (q_skillBean) iter.next();
            map.put(bean.getQ_skill_id(), bean);
        }
    }

    public List<q_skillBean> getList()
    {
        return list;
    }

    public Map<Integer, q_skillBean> getMap()
    {
        return map;
    }
}
