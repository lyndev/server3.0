
package game.server.logic.loginGift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.logic.constant.GlobalTableKey;
import game.server.logic.loginGift.bean.LoginGiftBean;
import game.server.logic.support.IJsonConverter;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class LoginGiftService implements IJsonConverter
{
    private final Logger LOG = Logger.getLogger(LoginGiftService.class);
    private static final LoginGiftService instance;
    private final Map<Integer, LoginGiftBean> map;
    
    static 
    {
        instance = new LoginGiftService();
    }
    
    private LoginGiftService()
    {
        map = new HashMap<>();
    }

    public static LoginGiftService getInstance()
    {
        return instance;
    }
    
    public LoginGiftBean getLoginGift(int award_id)
    {
        return map.get(award_id);
    }
    
    public boolean putLoginGift(LoginGiftBean bean)
    {
        if (map.containsKey(bean.getAward_id()))
        {
            LOG.error("添加登陆有礼活动失败, id [" + bean.getAward_id() + "] 的活动已存在");
            return false;
        }
        else
        {
            map.put(bean.getAward_id(), bean);
            LOG.info("添加登陆有礼活动成功, id [" + bean.getAward_id() + "], name [" + bean.getAward_name() + "]");
            return true;
        }
    }
    
    public Map<Integer, LoginGiftBean> getMap()
    {
        return map;
    }
    
    public void load()
    {
        GlobalBean bean = GlobalDao.select(GlobalTableKey.LOGIN_GIFT.getKey());
        if (bean != null)
        {
            JSONArray arr = JSONArray.parseArray(bean.getValue());
            this.fromJson(arr);
        }
    }

    @Override
    public JSON toJson()
    {
        JSONArray arr = new JSONArray();
        if (map != null && !map.isEmpty())
        {
            for (LoginGiftBean bean : map.values())
            {
                arr.add(bean.toJson());
            }
        }
        return arr;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONArray arr = (JSONArray)json;
        if (arr != null && !arr.isEmpty())
        {
            for (Object each : arr)
            {
                LoginGiftBean bean = new LoginGiftBean();
                bean.fromJson((JSON)each);
                map.put(bean.getAward_id(), bean);
            }
        }
    }
    
}
