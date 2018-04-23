
package game.server.logic.loginGift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.struct.Player;
import game.server.logic.support.IJsonConverter;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ZouZhaopeng
 */
public class LoginGiftManager implements IJsonConverter
{
    //已经领取过的登陆有礼活动id集合
    private final Set<Integer> set = new HashSet<>();
    private final transient Player owner;

    public LoginGiftManager(Player player)
    {
        this.owner = player;
    }
    
    
    public void add(int id)
    {
        set.add(id);
    }
    
    public boolean contains(int id)
    {
        return set.contains(id);
    }
    
    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        if (set != null && !set.isEmpty())
        {
            JSONArray arr = new JSONArray();
            for (Integer id : set)
            {
                arr.add(id);
            }
            obj.put("set", arr);
        }
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null)
        {
            return;
        }
        JSONObject obj = (JSONObject)json;
        JSONArray arr = obj.getJSONArray("set");
        if (arr != null && !arr.isEmpty())
        {
            for (Object each : arr)
            {
                set.add((Integer)each);
            }
        }
    }
    
}
