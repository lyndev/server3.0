/**
 * Auto generated, do not edit it
 *
 * q_language
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_languageBean;
import game.data.dao.q_languageDao;

public class q_languageContainer
{
    private List<q_languageBean> list;
    private final Map<Integer, q_languageBean> map = new HashMap<>();
    private final q_languageDao dao = new q_languageDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_languageBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_languageBean bean = (q_languageBean) iter.next();
            map.put(bean.getQ_langulageid(), bean);
        }
    }

    public List<q_languageBean> getList()
    {
        return list;
    }

    public Map<Integer, q_languageBean> getMap()
    {
        return map;
    }
}
