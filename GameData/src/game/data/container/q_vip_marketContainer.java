/**
 * Auto generated, do not edit it
 *
 * q_vip_market
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_vip_marketBean;
import game.data.dao.q_vip_marketDao;

public class q_vip_marketContainer
{
    private List<q_vip_marketBean> list;
    private final Map<Integer, q_vip_marketBean> map = new HashMap<>();
    private final q_vip_marketDao dao = new q_vip_marketDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_vip_marketBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_vip_marketBean bean = (q_vip_marketBean) iter.next();
            map.put(bean.getQ_pack_id(), bean);
        }
    }

    public List<q_vip_marketBean> getList()
    {
        return list;
    }

    public Map<Integer, q_vip_marketBean> getMap()
    {
        return map;
    }
}
