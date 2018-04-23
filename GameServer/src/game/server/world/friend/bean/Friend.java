/**
 * @date 2014/10/8
 * @author ChenLong
 */
package game.server.world.friend.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;
import game.server.util.UniqueId;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class Friend implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(Friend.class);
    private long userId = 0;    // 好友userId
    private int  type = 1;      // 好友类型(1:永久好友， 0:临时好友)
    private long timestamp = System.currentTimeMillis(); // 添加好友时间戳

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userId", UniqueId.toBase36(userId));
        jsonObj.put("timestamp", timestamp);
        jsonObj.put("type", type);
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
            {
                logger.error("Friend fromJson json == null");
                break;
            }
            if (!(json instanceof JSONObject))
            {
                logger.error("Friend fromJson json not instanceof JSONObject");
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            String userIdStr = jsonObj.getString("userId");
            userId = UniqueId.toBase10(userIdStr);
            timestamp = jsonObj.getLongValue("timestamp");
            type = jsonObj.getIntValue("type");
        } while (false);
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
    
    /**
     * 是否是临时好友
     * @return 
     */
    public boolean isTmpFriend(){
        if (this.type == 0){
            return true;
        }else{
            return false;
        }
    }
}
