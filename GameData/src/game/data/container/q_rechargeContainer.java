/**
 * Auto generated, do not edit it
 *
 * q_recharge
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_rechargeBean;
import game.data.dao.q_rechargeDao;

public class q_rechargeContainer
{
    private List<q_rechargeBean> list;
    private final Map<Integer, q_rechargeBean> map = new HashMap<>();
    private final q_rechargeDao dao = new q_rechargeDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_rechargeBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_rechargeBean bean = (q_rechargeBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_rechargeBean> getList()
    {
        return list;
    }

    public Map<Integer, q_rechargeBean> getMap()
    {
        return map;
    }
}
