/**
 * Auto generated, do not edit it
 *
 * q_gold_Lottery
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_gold_LotteryBean;
import game.data.dao.q_gold_LotteryDao;

public class q_gold_LotteryContainer
{
    private List<q_gold_LotteryBean> list;
    private final Map<Integer, q_gold_LotteryBean> map = new HashMap<>();
    private final q_gold_LotteryDao dao = new q_gold_LotteryDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_gold_LotteryBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_gold_LotteryBean bean = (q_gold_LotteryBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_gold_LotteryBean> getList()
    {
        return list;
    }

    public Map<Integer, q_gold_LotteryBean> getMap()
    {
        return map;
    }
}
