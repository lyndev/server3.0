/**
 * Auto generated, do not edit it
 *
 * q_gem
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_gemBean;
import game.data.dao.q_gemDao;

public class q_gemContainer
{
    private List<q_gemBean> list;
    private final Map<Integer, q_gemBean> map = new HashMap<>();
    private final q_gemDao dao = new q_gemDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_gemBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_gemBean bean = (q_gemBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_gemBean> getList()
    {
        return list;
    }

    public Map<Integer, q_gemBean> getMap()
    {
        return map;
    }
}
