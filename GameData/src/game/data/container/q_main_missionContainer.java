/**
 * Auto generated, do not edit it
 *
 * q_main_mission
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_main_missionBean;
import game.data.dao.q_main_missionDao;

public class q_main_missionContainer
{
    private List<q_main_missionBean> list;
    private final Map<Integer, q_main_missionBean> map = new HashMap<>();
    private final q_main_missionDao dao = new q_main_missionDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_main_missionBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_main_missionBean bean = (q_main_missionBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_main_missionBean> getList()
    {
        return list;
    }

    public Map<Integer, q_main_missionBean> getMap()
    {
        return map;
    }
}
