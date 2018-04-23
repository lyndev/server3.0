package game.server.logic.item.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * <b>宝石.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class Gems extends Item
{
    private int pos;

    @Override
    public JSON toJson()
    {
        JSONObject obj = (JSONObject) super.toJson();
        obj.put("pos", pos);
        obj.put("gemId", getId());
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject jsonObj = (JSONObject) json;
        this.setPos(jsonObj.getIntValue("pos"));
        this.setId(jsonObj.getIntValue("gemId"));
    }

    @Override
    public Gems clone() throws CloneNotSupportedException
    {
        return (Gems) super.clone();
    }

    public int getPos()
    {
        return pos;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

}
