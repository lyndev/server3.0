/**
 * Auto generated, do not edit it
 *
 * q_main_build
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_main_buildBean;
import game.data.dao.q_main_buildDao;

public class q_main_buildContainer
{
    private List<q_main_buildBean> list;
    private final Map<Integer, q_main_buildBean> map = new HashMap<>();
    private final q_main_buildDao dao = new q_main_buildDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_main_buildBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_main_buildBean bean = (q_main_buildBean) iter.next();
            map.put(bean.getQ_idx(), bean);
        }
    }

    public List<q_main_buildBean> getList()
    {
        return list;
    }

    public Map<Integer, q_main_buildBean> getMap()
    {
        return map;
    }
}
