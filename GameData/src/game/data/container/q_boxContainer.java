/**
 * Auto generated, do not edit it
 *
 * q_box
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_boxBean;
import game.data.dao.q_boxDao;

public class q_boxContainer
{
    private List<q_boxBean> list;
    private final Map<Integer, q_boxBean> map = new HashMap<>();
    private final q_boxDao dao = new q_boxDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_boxBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_boxBean bean = (q_boxBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_boxBean> getList()
    {
        return list;
    }

    public Map<Integer, q_boxBean> getMap()
    {
        return map;
    }
}
