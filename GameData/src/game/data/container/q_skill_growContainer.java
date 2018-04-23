/**
 * Auto generated, do not edit it
 *
 * q_skill_grow
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_skill_growBean;
import game.data.dao.q_skill_growDao;

public class q_skill_growContainer
{
    private List<q_skill_growBean> list;
    private final Map<String, q_skill_growBean> map = new HashMap<>();
    private final q_skill_growDao dao = new q_skill_growDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_skill_growBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_skill_growBean bean = (q_skill_growBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_skill_growBean> getList()
    {
        return list;
    }

    public Map<String, q_skill_growBean> getMap()
    {
        return map;
    }
}
