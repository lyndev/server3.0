/**
 * Auto generated, do not edit it
 *
 * q_exchange_coin
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_exchange_coinBean;
import game.data.dao.q_exchange_coinDao;

public class q_exchange_coinContainer
{
    private List<q_exchange_coinBean> list;
    private final Map<Integer, q_exchange_coinBean> map = new HashMap<>();
    private final q_exchange_coinDao dao = new q_exchange_coinDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_exchange_coinBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_exchange_coinBean bean = (q_exchange_coinBean) iter.next();
            map.put(bean.getQ_times(), bean);
        }
    }

    public List<q_exchange_coinBean> getList()
    {
        return list;
    }

    public Map<Integer, q_exchange_coinBean> getMap()
    {
        return map;
    }
}
