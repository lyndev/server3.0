/**
 * Auto generated, do not edit it
 *
 * q_buy_cost
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_buy_costBean;
import game.data.dao.q_buy_costDao;

public class q_buy_costContainer
{
    private List<q_buy_costBean> list;
    private final Map<Integer, q_buy_costBean> map = new HashMap<>();
    private final q_buy_costDao dao = new q_buy_costDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_buy_costBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_buy_costBean bean = (q_buy_costBean) iter.next();
            map.put(bean.getQ_buy_num(), bean);
        }
    }

    public List<q_buy_costBean> getList()
    {
        return list;
    }

    public Map<Integer, q_buy_costBean> getMap()
    {
        return map;
    }
}
