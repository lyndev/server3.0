/**
 * Auto generated, do not edit it
 *
 * q_card_formula
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_card_formulaBean;
import game.data.dao.q_card_formulaDao;

public class q_card_formulaContainer
{
    private List<q_card_formulaBean> list;
    private final Map<Integer, q_card_formulaBean> map = new HashMap<>();
    private final q_card_formulaDao dao = new q_card_formulaDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_card_formulaBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_card_formulaBean bean = (q_card_formulaBean) iter.next();
            map.put(bean.getQ_id(), bean);
        }
    }

    public List<q_card_formulaBean> getList()
    {
        return list;
    }

    public Map<Integer, q_card_formulaBean> getMap()
    {
        return map;
    }
}
