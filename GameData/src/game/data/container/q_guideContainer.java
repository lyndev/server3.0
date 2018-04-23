/**
 * Auto generated, do not edit it
 *
 * q_guide
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_guideBean;
import game.data.dao.q_guideDao;

public class q_guideContainer
{
    private List<q_guideBean> list;
    private final Map<Integer, q_guideBean> map = new HashMap<>();
    private final q_guideDao dao = new q_guideDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_guideBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_guideBean bean = (q_guideBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_guideBean> getList()
    {
        return list;
    }

    public Map<Integer, q_guideBean> getMap()
    {
        return map;
    }
}
