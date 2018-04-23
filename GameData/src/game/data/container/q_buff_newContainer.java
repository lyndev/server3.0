/**
 * Auto generated, do not edit it
 *
 * q_buff_new
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_buff_newBean;
import game.data.dao.q_buff_newDao;

public class q_buff_newContainer
{
    private List<q_buff_newBean> list;
    private final Map<Integer, q_buff_newBean> map = new HashMap<>();
    private final q_buff_newDao dao = new q_buff_newDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_buff_newBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_buff_newBean bean = (q_buff_newBean) iter.next();
            map.put(bean.getQ_buff_id(), bean);
        }
    }

    public List<q_buff_newBean> getList()
    {
        return list;
    }

    public Map<Integer, q_buff_newBean> getMap()
    {
        return map;
    }
}
