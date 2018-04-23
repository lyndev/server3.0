/**
 * Auto generated, do not edit it
 *
 * q_first_recharge
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_first_rechargeBean;
import game.data.dao.q_first_rechargeDao;

public class q_first_rechargeContainer
{
    private List<q_first_rechargeBean> list;
    private final Map<Integer, q_first_rechargeBean> map = new HashMap<>();
    private final q_first_rechargeDao dao = new q_first_rechargeDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_first_rechargeBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_first_rechargeBean bean = (q_first_rechargeBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_first_rechargeBean> getList()
    {
        return list;
    }

    public Map<Integer, q_first_rechargeBean> getMap()
    {
        return map;
    }
}
