/**
 * Auto generated, do not edit it
 *
 * q_mission_chapter
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_mission_chapterBean;
import game.data.dao.q_mission_chapterDao;

public class q_mission_chapterContainer
{
    private List<q_mission_chapterBean> list;
    private final Map<Integer, q_mission_chapterBean> map = new HashMap<>();
    private final q_mission_chapterDao dao = new q_mission_chapterDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_mission_chapterBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_mission_chapterBean bean = (q_mission_chapterBean) iter.next();
            map.put(bean.getQ_chapter_id(), bean);
        }
    }

    public List<q_mission_chapterBean> getList()
    {
        return list;
    }

    public Map<Integer, q_mission_chapterBean> getMap()
    {
        return map;
    }
}
