/**
 * Auto generated, do not edit it
 *
 * q_monster_pos
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_monster_posBean;
import game.data.dao.q_monster_posDao;

public class q_monster_posContainer
{
    private List<q_monster_posBean> list;
    private final Map<Integer, q_monster_posBean> map = new HashMap<>();
    private final q_monster_posDao dao = new q_monster_posDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_monster_posBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_monster_posBean bean = (q_monster_posBean) iter.next();
            map.put(bean.getQ_monsterPosId(), bean);
        }
    }

    public List<q_monster_posBean> getList()
    {
        return list;
    }

    public Map<Integer, q_monster_posBean> getMap()
    {
        return map;
    }
}
