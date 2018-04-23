/**
 * Auto generated, do not edit it
 *
 * q_monster_level_attribute
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_monster_level_attributeBean;
import game.data.dao.q_monster_level_attributeDao;

public class q_monster_level_attributeContainer
{
    private List<q_monster_level_attributeBean> list;
    private final Map<Integer, q_monster_level_attributeBean> map = new HashMap<>();
    private final q_monster_level_attributeDao dao = new q_monster_level_attributeDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_monster_level_attributeBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_monster_level_attributeBean bean = (q_monster_level_attributeBean) iter.next();
            map.put(bean.getQ_Lv(), bean);
        }
    }

    public List<q_monster_level_attributeBean> getList()
    {
        return list;
    }

    public Map<Integer, q_monster_level_attributeBean> getMap()
    {
        return map;
    }
}
