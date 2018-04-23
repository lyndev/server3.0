/**
 * Auto generated, do not edit it
 *
 * q_mission_talk
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_mission_talkBean;
import game.data.dao.q_mission_talkDao;

public class q_mission_talkContainer
{
    private List<q_mission_talkBean> list;
    private final Map<Integer, q_mission_talkBean> map = new HashMap<>();
    private final q_mission_talkDao dao = new q_mission_talkDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_mission_talkBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_mission_talkBean bean = (q_mission_talkBean) iter.next();
            map.put(bean.getQ_talk_id(), bean);
        }
    }

    public List<q_mission_talkBean> getList()
    {
        return list;
    }

    public Map<Integer, q_mission_talkBean> getMap()
    {
        return map;
    }
}
