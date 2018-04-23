package game.server.world.chat.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;

/**
 *
 * @author ZouZhaopeng
 */
public class Link implements IJsonConverter
{
    private int linkId; //链接id
    private int type; //链接类型: 1.卡牌链接 2.装备链接 3.神兵链接
    private String jsonString; //卡牌/装备的持久数据

    public Link()
    {
    }
    public Link(int linkId, int type, String jsonString)
    {
        this.linkId = linkId;
        this.type = type;
        this.jsonString = jsonString;
    }

    public int getLinkId()
    {
        return linkId;
    }

    public void setLinkId(int linkId)
    {
        this.linkId = linkId;
    }

    /**
     * 1.卡牌链接 2.装备链接 3. 神兵链接
     * @return 
     */
    public int getType()
    {
        return type;
    }

    /**
     * 1.卡牌链接 2.装备链接 3. 神兵链接
     * @param type 
     */
    public void setType(int type)
    {
        this.type = type;
    }

    public String getJsonString()
    {
        return jsonString;
    }

    public void setJsonString(String jsonString)
    {
        this.jsonString = jsonString;
    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("linkId", linkId);
        obj.put("type", type);
        if (jsonString != null)
        {
            obj.put("jsonString", jsonString);
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
        JSONObject obj = (JSONObject) json;
        linkId = obj.getIntValue("linkId");
        type = obj.getIntValue("type");
        jsonString = obj.getString("jsonString");
    }

}
