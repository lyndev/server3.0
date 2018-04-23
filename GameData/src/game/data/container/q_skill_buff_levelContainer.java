/**
 * Auto generated, do not edit it
 *
 * q_skill_buff_level
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_skill_buff_levelBean;
import game.data.dao.q_skill_buff_levelDao;

public class q_skill_buff_levelContainer
{
    private List<q_skill_buff_levelBean> list;
    private final Map<String, q_skill_buff_levelBean> map = new HashMap<>();
    private final q_skill_buff_levelDao dao = new q_skill_buff_levelDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_skill_buff_levelBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_skill_buff_levelBean bean = (q_skill_buff_levelBean) iter.next();
            map.put(bean.getQ_skill_level_id(), bean);
        }
    }

    public List<q_skill_buff_levelBean> getList()
    {
        return list;
    }

    public Map<String, q_skill_buff_levelBean> getMap()
    {
        return map;
    }
}
