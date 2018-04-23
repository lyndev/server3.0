/**
 * @date 2014/9/17
 * @author ChenLong
 */
package game.server.logic.vip.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;
import game.server.logic.vip.VipGiftStatus;
import org.apache.log4j.Logger;

/**
 * VIP礼包数据
 *
 * @author ChenLong
 */
public class VipGiftBean implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(VipGiftBean.class);

    private int giftId;
    private VipGiftStatus status;
    private long buyTime;

    public VipGiftBean()
    {
        
    }

    public VipGiftBean(int giftId, VipGiftStatus status, long buyTime)
    {
        this.giftId = giftId;
        this.status = status;
        this.buyTime = buyTime;
    }
    
    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("giftId", giftId);
        jsonObj.put("status", status.getValue());
        jsonObj.put("buyTime", buyTime);
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
            {
                logger.error("VipGiftBean fromJson json == null");
                break;
            }
            if (!(json instanceof JSONObject))
            {
                logger.error("VipGiftBean fromJson json not instanceof JSONObject");
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            giftId = jsonObj.getIntValue("giftId");
            status = VipGiftStatus.getStatusFromValue(jsonObj.getIntValue("status"));
            buyTime = jsonObj.getLongValue("buyTime");
        } while (false);
    }

    public int getGiftId()
    {
        return giftId;
    }

    public VipGiftStatus getStatus()
    {
        return status;
    }

    public long getBuyTime()
    {
        return buyTime;
    }
}
