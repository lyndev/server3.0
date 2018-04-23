/**
 * Auto generated, do not edit it
 *
 * q_filterword
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_filterwordBean;
import game.data.dao.q_filterwordDao;

public class q_filterwordContainer
{
    private List<q_filterwordBean> list;
    private final Map<String, q_filterwordBean> map = new HashMap<>();
    private final q_filterwordDao dao = new q_filterwordDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_filterwordBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_filterwordBean bean = (q_filterwordBean) iter.next();
            map.put(bean.getQ_country(), bean);
        }
    }

    public List<q_filterwordBean> getList()
    {
        return list;
    }

    public Map<String, q_filterwordBean> getMap()
    {
        return map;
    }
}
