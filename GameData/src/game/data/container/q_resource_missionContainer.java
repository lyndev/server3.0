/**
 * Auto generated, do not edit it
 *
 * q_resource_mission
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_resource_missionBean;
import game.data.dao.q_resource_missionDao;

public class q_resource_missionContainer
{
    private List<q_resource_missionBean> list;
    private final Map<Integer, q_resource_missionBean> map = new HashMap<>();
    private final q_resource_missionDao dao = new q_resource_missionDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_resource_missionBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_resource_missionBean bean = (q_resource_missionBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_resource_missionBean> getList()
    {
        return list;
    }

    public Map<Integer, q_resource_missionBean> getMap()
    {
        return map;
    }
}
