
package game.server.logic.loginGift.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.item.bean.Item;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.IJsonConverter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ZouZhaopeng
 */
public class LoginGiftBean implements IJsonConverter
{
    private int award_id;
    private String award_name;
    private int time_begin;
    private int time_end;
    private int level_min;
    private int level_max;
    private String mail_title;
    private String mail_content;
    private List<Item> itemList;
    private boolean disable;

    public LoginGiftBean()
    {
        itemList = new ArrayList<>();
    }
    
    public LoginGiftBean(int award_id, String award_name, int time_begin, int time_end, int level_min, int level_max, String mail_title, String mail_content, List<Item> itemList, boolean disable)
    {
//        this.award_id = award_id;
        //英俊说的, 平台他们不负责维护这个id的唯一性, 让我存当前时间戳作为id
        this.award_id = (int)(System.currentTimeMillis() / 1000);
        this.award_name = award_name;
        this.time_begin = time_begin;
        this.time_end = time_end;
        this.level_min = level_min;
        this.level_max = level_max;
        this.mail_title = mail_title;
        this.mail_content = mail_content;
        this.itemList = new ArrayList<>();
        if (itemList != null)
        {
            this.itemList.addAll(itemList);
        }
        this.disable = disable;
    }
    
    public int getAward_id()
    {
        return award_id;
    }

    public String getAward_name()
    {
        return award_name;
    }

    public int getTime_begin()
    {
        return time_begin;
    }

    public int getTime_end()
    {
        return time_end;
    }

    public int getLevel_min()
    {
        return level_min;
    }

    public int getLevel_max()
    {
        return level_max;
    }

    public String getMail_title()
    {
        return mail_title;
    }

    public String getMail_content()
    {
        return mail_content;
    }

    public List<Item> getItemList()
    {
        return itemList;
    }

    public void setAward_id(int award_id)
    {
        this.award_id = award_id;
    }

    public void setAward_name(String award_name)
    {
        this.award_name = award_name;
    }

    public void setTime_begin(int time_begin)
    {
        this.time_begin = time_begin;
    }

    public void setTime_end(int time_end)
    {
        this.time_end = time_end;
    }

    public void setLevel_min(int level_min)
    {
        this.level_min = level_min;
    }

    public void setLevel_max(int level_max)
    {
        this.level_max = level_max;
    }

    public void setMail_title(String mail_title)
    {
        this.mail_title = mail_title;
    }

    public void setMail_content(String mail_content)
    {
        this.mail_content = mail_content;
    }

    public void setItemList(List<Item> itemList)
    {
        this.itemList = itemList;
    }

    public void setDisable(boolean disable)
    {
        this.disable = disable;
    }

    public boolean isDisable()
    {
        return disable;
    }
    
    public boolean isOpening()
    {
        int currTime = (int)(System.currentTimeMillis() / 1000);
        return (time_begin <= currTime) && (time_end >= currTime);
    }
    
    public boolean isLevelFit(int level)
    {
        return (level_min <= level) && (level_max >= level);
    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("award_id", award_id);
        obj.put("award_name", award_name);
        obj.put("time_begin", time_begin);
        obj.put("time_end", time_end);
        obj.put("level_min", level_min);
        obj.put("level_max", level_max);
        obj.put("mail_title", mail_title);
        obj.put("mail_content", mail_content);
        obj.put("disable", disable);

        if (itemList != null && !itemList.isEmpty())
        {
            JSONArray itemArr = new JSONArray();
            for (Item item :itemList)
            {
                itemArr.add(item.toJson());
            }
            obj.put("itemList", itemArr);
        }
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject obj = (JSONObject)json;
        award_id = obj.getIntValue("award_id");
        award_name = obj.getString("award_name");
        time_begin = obj.getIntValue("time_begin");
        time_end = obj.getIntValue("time_end");
        level_min = obj.getIntValue("level_min");
        level_max = obj.getIntValue("level_max");
        mail_title = obj.getString("mail_title");
        mail_content = obj.getString("mail_content");
        disable = obj.getBooleanValue("disable");
        JSONArray itemArr = obj.getJSONArray("itemList");
        if (itemArr != null && !itemArr.isEmpty())
        {
            for (Object each : itemArr)
            {
                JSONObject itemObj = (JSONObject)each;
                Item item = BeanFactory.createItem(itemObj);
                if (item != null)
                {
                    itemList.add(item);
                }
            }
        }
    }
    
    public static void main(String[] args)
    {
        LoginGiftBean bean = new LoginGiftBean();
        bean.setAward_id(1025);
        bean.setAward_name("登陆有礼活动名称");
        bean.setTime_begin((int)(System.currentTimeMillis() / 1000));
        bean.setTime_end((int)(System.currentTimeMillis() / 1000) + 360000);
        bean.setLevel_min(0);
        bean.setLevel_max(100);
        bean.setMail_title("登陆有奖活动标题");
        bean.setMail_content("登陆有奖活动内容");
        bean.setDisable(false);
        System.out.println(bean.toJson().toJSONString());
    }
}
