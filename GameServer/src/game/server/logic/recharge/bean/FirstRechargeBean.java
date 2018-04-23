/**
 * @date 2014/9/25
 * @author ChenLong
 */
package game.server.logic.recharge.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.recharge.FirstRechargeState;
import game.server.logic.support.IJsonConverter;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class FirstRechargeBean implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(FirstRechargeBean.class);

    private int id;
    private FirstRechargeState state = FirstRechargeState.CAN_NOT_GET;

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", id);
        jsonObj.put("state", state.getValue());
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
            {
                logger.error("FirstRechargeBean fromJson json == null");
                break;
            }
            if (!(json instanceof JSONObject))
            {
                logger.error("FitstRechargeBean not instanceof JSONObject, json class: " + json.getClass().getName());
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            id = jsonObj.getIntValue("id");
            int stateValue = jsonObj.getIntValue("state");
            FirstRechargeState tstate = FirstRechargeState.getType(stateValue);
            if (tstate != null)
                this.state = tstate;
            else
                logger.error("FitstRechargeBean fromJson state == null, stateValue = " + stateValue);
        } while (false);
    }

    public int getId()
    {
        return id;
    }

    public FirstRechargeState getState()
    {
        return state;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setState(FirstRechargeState state)
    {
        this.state = state;
    }
}
