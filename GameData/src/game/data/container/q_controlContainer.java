/**
 * Auto generated, do not edit it
 *
 * q_control
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_controlBean;
import game.data.dao.q_controlDao;

public class q_controlContainer
{
    private List<q_controlBean> list;
    private final Map<String, q_controlBean> map = new HashMap<>();
    private final q_controlDao dao = new q_controlDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_controlBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_controlBean bean = (q_controlBean) iter.next();
            map.put(bean.getQ_functionId(), bean);
        }
    }

    public List<q_controlBean> getList()
    {
        return list;
    }

    public Map<String, q_controlBean> getMap()
    {
        return map;
    }
}
