/**
 * Auto generated, do not edit it
 *
 * q_main_role
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_main_roleBean;
import game.data.dao.q_main_roleDao;

public class q_main_roleContainer
{
    private List<q_main_roleBean> list;
    private final Map<Integer, q_main_roleBean> map = new HashMap<>();
    private final q_main_roleDao dao = new q_main_roleDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_main_roleBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_main_roleBean bean = (q_main_roleBean) iter.next();
            map.put(bean.getQ_idx(), bean);
        }
    }

    public List<q_main_roleBean> getList()
    {
        return list;
    }

    public Map<Integer, q_main_roleBean> getMap()
    {
        return map;
    }
}
