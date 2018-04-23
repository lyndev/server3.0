/**
 * @date 2014/9/16
 * @author ChenLong
 */
package game.server.logic.recharge.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class RechargeBean implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(RechargeBean.class);

    private String order = StringUtils.EMPTY;
    private long finishedTime = 0;

    public RechargeBean()
    {
    }

    public RechargeBean(String order, long finishedTime)
    {
        this.order = order;
        this.finishedTime = finishedTime;
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("order", order);
        jsonObj.put("finishedTime", finishedTime);

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json != null)
        {
            if (json instanceof JSONObject)
            {
                JSONObject jsonObj = (JSONObject) json;
                order = jsonObj.getString("order");
                finishedTime = jsonObj.getLongValue("finishedTime");
            }
            else
            {
                logger.error("RechargeBean json not instanceof JSONObject");
            }
        }
        else
        {
            logger.error("RechargeBean json == null");
        }
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public String getOrder()
    {
        return order;
    }

    public long getFinishedTime()
    {
        return finishedTime;
    }
}
