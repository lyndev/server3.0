/**
 * Auto generated, do not edit it
 *
 * q_aiConfig
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_aiConfigBean;
import game.data.dao.q_aiConfigDao;

public class q_aiConfigContainer
{
    private List<q_aiConfigBean> list;
    private final Map<Integer, q_aiConfigBean> map = new HashMap<>();
    private final q_aiConfigDao dao = new q_aiConfigDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_aiConfigBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_aiConfigBean bean = (q_aiConfigBean) iter.next();
            map.put(bean.getQ_aiID(), bean);
        }
    }

    public List<q_aiConfigBean> getList()
    {
        return list;
    }

    public Map<Integer, q_aiConfigBean> getMap()
    {
        return map;
    }
}
