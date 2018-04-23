/**
 * Auto generated, do not edit it
 *
 * q_score_mission
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_score_missionBean;
import game.data.dao.q_score_missionDao;

public class q_score_missionContainer
{
    private List<q_score_missionBean> list;
    private final Map<Integer, q_score_missionBean> map = new HashMap<>();
    private final q_score_missionDao dao = new q_score_missionDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_score_missionBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_score_missionBean bean = (q_score_missionBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_score_missionBean> getList()
    {
        return list;
    }

    public Map<Integer, q_score_missionBean> getMap()
    {
        return map;
    }
}
