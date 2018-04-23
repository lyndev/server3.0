/**
 * Auto generated, do not edit it
 *
 * q_game_map
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_game_mapBean;
import game.data.dao.q_game_mapDao;

public class q_game_mapContainer
{
    private List<q_game_mapBean> list;
    private final Map<Integer, q_game_mapBean> map = new HashMap<>();
    private final q_game_mapDao dao = new q_game_mapDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_game_mapBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_game_mapBean bean = (q_game_mapBean) iter.next();
            map.put(bean.getQ_mapId(), bean);
        }
    }

    public List<q_game_mapBean> getList()
    {
        return list;
    }

    public Map<Integer, q_game_mapBean> getMap()
    {
        return map;
    }
}
