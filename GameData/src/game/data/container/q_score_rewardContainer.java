/**
 * Auto generated, do not edit it
 *
 * q_score_reward
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_score_rewardBean;
import game.data.dao.q_score_rewardDao;

public class q_score_rewardContainer
{
    private List<q_score_rewardBean> list;
    private final Map<Integer, q_score_rewardBean> map = new HashMap<>();
    private final q_score_rewardDao dao = new q_score_rewardDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_score_rewardBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_score_rewardBean bean = (q_score_rewardBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_score_rewardBean> getList()
    {
        return list;
    }

    public Map<Integer, q_score_rewardBean> getMap()
    {
        return map;
    }
}
