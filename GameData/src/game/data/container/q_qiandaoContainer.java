/**
 * Auto generated, do not edit it
 *
 * q_qiandao
 */
package game.data.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import game.data.bean.q_qiandaoBean;
import game.data.dao.q_qiandaoDao;

public class q_qiandaoContainer
{
    private List<q_qiandaoBean> list;
    private final Map<Integer, q_qiandaoBean> map = new HashMap<>();
    private final q_qiandaoDao dao = new q_qiandaoDao();

    public void load()
    {
        list = dao.select();
        Iterator<q_qiandaoBean> iter = list.iterator();
        while (iter.hasNext())
        {
            q_qiandaoBean bean = (q_qiandaoBean) iter.next();
            map.put(bean.getQ_yuefen(), bean);
        }
    }

    public List<q_qiandaoBean> getList()
    {
        return list;
    }

    public Map<Integer, q_qiandaoBean> getMap()
    {
        return map;
    }
}
