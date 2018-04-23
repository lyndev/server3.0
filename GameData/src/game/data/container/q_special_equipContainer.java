/**
 * Auto generated, do not edit it
 *
 * q_special_equip
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_special_equipBean;
import game.data.dao.q_special_equipDao;

public class q_special_equipContainer
{
    private List<q_special_equipBean> list;
    private final Map<Integer, q_special_equipBean> map = new HashMap<>();
    private final q_special_equipDao dao = new q_special_equipDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_special_equipBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_special_equipBean bean = (q_special_equipBean) iter.next();
            map.put(bean.getQ_Id(), bean);
        }
    }

    public List<q_special_equipBean> getList()
    {
        return list;
    }

    public Map<Integer, q_special_equipBean> getMap()
    {
        return map;
    }
}
