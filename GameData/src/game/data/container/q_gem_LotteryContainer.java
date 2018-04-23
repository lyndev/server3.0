/**
 * Auto generated, do not edit it
 *
 * q_gem_Lottery
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_gem_LotteryBean;
import game.data.dao.q_gem_LotteryDao;

public class q_gem_LotteryContainer
{
    private List<q_gem_LotteryBean> list;
    private final Map<Integer, q_gem_LotteryBean> map = new HashMap<>();
    private final q_gem_LotteryDao dao = new q_gem_LotteryDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_gem_LotteryBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_gem_LotteryBean bean = (q_gem_LotteryBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_gem_LotteryBean> getList()
    {
        return list;
    }

    public Map<Integer, q_gem_LotteryBean> getMap()
    {
        return map;
    }
}
