/**
 * Auto generated, do not edit it
 *
 * q_position
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_positionBean;
import game.data.dao.q_positionDao;

public class q_positionContainer
{
    private List<q_positionBean> list;
    private final Map<Integer, q_positionBean> map = new HashMap<>();
    private final q_positionDao dao = new q_positionDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_positionBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_positionBean bean = (q_positionBean) iter.next();
            map.put(bean.getQ_posId(), bean);
        }
    }

    public List<q_positionBean> getList()
    {
        return list;
    }

    public Map<Integer, q_positionBean> getMap()
    {
        return map;
    }
}
