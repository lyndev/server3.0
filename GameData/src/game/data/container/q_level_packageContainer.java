/**
 * Auto generated, do not edit it
 *
 * q_level_package
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_level_packageBean;
import game.data.dao.q_level_packageDao;

public class q_level_packageContainer
{
    private List<q_level_packageBean> list;
    private final Map<Integer, q_level_packageBean> map = new HashMap<>();
    private final q_level_packageDao dao = new q_level_packageDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_level_packageBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_level_packageBean bean = (q_level_packageBean) iter.next();
            map.put(bean.getQ_packageId(), bean);
        }
    }

    public List<q_level_packageBean> getList()
    {
        return list;
    }

    public Map<Integer, q_level_packageBean> getMap()
    {
        return map;
    }
}
