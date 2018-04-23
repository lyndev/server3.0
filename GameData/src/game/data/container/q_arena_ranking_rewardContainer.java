/**
 * Auto generated, do not edit it
 *
 * q_arena_ranking_reward
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_arena_ranking_rewardBean;
import game.data.dao.q_arena_ranking_rewardDao;

public class q_arena_ranking_rewardContainer
{
    private List<q_arena_ranking_rewardBean> list;
    private final Map<Integer, q_arena_ranking_rewardBean> map = new HashMap<>();
    private final q_arena_ranking_rewardDao dao = new q_arena_ranking_rewardDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_arena_ranking_rewardBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_arena_ranking_rewardBean bean = (q_arena_ranking_rewardBean) iter.next();
            map.put(bean.getQ_rank(), bean);
        }
    }

    public List<q_arena_ranking_rewardBean> getList()
    {
        return list;
    }

    public Map<Integer, q_arena_ranking_rewardBean> getMap()
    {
        return map;
    }
}
