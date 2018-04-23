package game.server.logic.mail.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.item.bean.Item;
import game.server.logic.mail.service.MailService;
import game.server.logic.struct.Resource;
import game.server.logic.support.IJsonConverter;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 用来辅助发送邮件的工具类
 * 
 * @author ZouZhaopeng
 */
public class MailTool implements IJsonConverter
{
    private static final Logger log = Logger.getLogger(MailTool.class);
    
    private int type;
    private String parameter;
    private String title;
    private String content;
    private String senderName;
    private int deadLine;
    private String resStr;
    private String itemStr;

    public MailTool()
    {
    }
    
    public MailTool(int type,  String parameter, String title, String content, String senderName, int deadLine, String resStr, String itemStr)
    {
        this.type = type;
        this.parameter = parameter;
        this.title = title;
        this.content = content;
        this.senderName = senderName;
        this.deadLine = deadLine;
        this.resStr = resStr;
        this.itemStr = itemStr;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getParameter()
    {
        return parameter;
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public int getDeadLine()
    {
        return deadLine;
    }

    public void setDeadLine(int deadLine)
    {
        this.deadLine = deadLine;
    }

    public String getResStr()
    {
        return resStr;
    }

    public void setResStr(String resStr)
    {
        this.resStr = resStr;
    }

    public String getItemStr()
    {
        return itemStr;
    }

    public void setItemStr(String itemStr)
    {
        this.itemStr = itemStr;
    }

    

    @Override
    public JSON toJson()
    {
        JSONObject json = new JSONObject();
        
        json.put("type", type);
        json.put("parameter", parameter);
        json.put("title", title);
        json.put("content", content);
        json.put("senderName", senderName);
        json.put("deadLine", deadLine);
        json.put("resStr", resStr);
        json.put("itemStr", itemStr);
        
        return json;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject obj = (JSONObject)json;
        
        type = obj.getIntValue("type");
        parameter = obj.getString("parameter");
        title = obj.getString("title");
        content = obj.getString("content");
        senderName = obj.getString("senderName");
        deadLine = obj.getIntValue("deadLine");
        resStr = obj.getString("resStr");
        itemStr = obj.getString("itemStr");
    }
    
    public Mail getMail()
    {
        int ddln = (int)(System.currentTimeMillis() / 1000) + 60 * 60 * deadLine;
        Mail mail = new Mail();
        int accessory = 1;
        List<Resource> resList = MailService.parseResources(resStr);
        List<Item> itemList = MailService.parseItems(itemStr);
        if (resList.isEmpty() && itemList.isEmpty())
            accessory = 0;
        mail.setAll(title, content, senderName, 2, 0L, "", accessory, ddln, resList, itemList);
        
        return mail;
    }
    
    
    public static void main(String[] args)
    {
        MailTool tool = new MailTool(1, "1", "天降横财", "感谢您对游戏的支持,祝您游戏愉快~", "系统", 24, "2_10", "");

        log.error(tool.toJson().toJSONString());
    }
}
