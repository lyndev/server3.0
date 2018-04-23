/**
 * Auto generated, do not edit it
 *
 * q_color_attr
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_color_attrBean;
import game.data.dao.q_color_attrDao;

public class q_color_attrContainer
{
    private List<q_color_attrBean> list;
    private final Map<Integer, q_color_attrBean> map = new HashMap<>();
    private final q_color_attrDao dao = new q_color_attrDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_color_attrBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_color_attrBean bean = (q_color_attrBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_color_attrBean> getList()
    {
        return list;
    }

    public Map<Integer, q_color_attrBean> getMap()
    {
        return map;
    }
}
