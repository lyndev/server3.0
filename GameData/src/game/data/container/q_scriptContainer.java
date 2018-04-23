/**
 * Auto generated, do not edit it
 *
 * q_script
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_scriptBean;
import game.data.dao.q_scriptDao;

public class q_scriptContainer
{
    private List<q_scriptBean> list;
    private final Map<Integer, q_scriptBean> map = new HashMap<>();
    private final q_scriptDao dao = new q_scriptDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_scriptBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_scriptBean bean = (q_scriptBean) iter.next();
            map.put(bean.getQ_script_id(), bean);
        }
    }

    public List<q_scriptBean> getList()
    {
        return list;
    }

    public Map<Integer, q_scriptBean> getMap()
    {
        return map;
    }
}
