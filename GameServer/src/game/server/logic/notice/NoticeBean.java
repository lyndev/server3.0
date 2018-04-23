/**
 * @date 2014/9/4
 * @author ChenLong
 */
package game.server.logic.notice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class NoticeBean implements IJsonConverter
{
    private static final Logger logger = Logger.getLogger(NoticeBean.class);

    private int id; // 公告ID, GM工具查询游戏数据库取得公告列表ID信息
    private int type; // 公告类型: 0跑马灯/1登陆界面的公告板/2游戏内的公告板, 暂时只支持 0跑马灯 类型
    private int startTime; // 发布时间: 从什么时间开始显示（如果是即时公告，GM工具自动设置为命令发出时刻+30秒）
    private int duration; // 持续有效时间（秒）, 从发布时刻开始持续多长时间有效（可以为0）
    private int interval; // 间隔时间（秒）, 间隔多少时间显示一次公告（间隔时间大于等于持续时间时判定为一次性公告）
    private String content; // 公告内容

    private int lastNoticeTime = 0; // 最后一次播公告的时间

    public NoticeBean()
    {

    }

    public NoticeBean(int id, int type, int startTime, int duration, int interval, String content)
    {
        this.id = id;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.interval = interval;
        this.content = content;
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("id", id);
        jsonObj.put("type", type);
        jsonObj.put("startTime", startTime);
        jsonObj.put("duration", duration);
        jsonObj.put("interval", interval);
        jsonObj.put("content", content);
        jsonObj.put("lastNoticeTime", lastNoticeTime);

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json != null && json instanceof JSONObject)
        {
            JSONObject jsonObj = (JSONObject) json;

            id = jsonObj.getIntValue("id");
            type = jsonObj.getIntValue("type");
            startTime = jsonObj.getIntValue("startTime");
            duration = jsonObj.getIntValue("duration");
            interval = jsonObj.getIntValue("interval");
            content = jsonObj.getString("content");
            lastNoticeTime = jsonObj.getIntValue("lastNoticeTime");
        }
        else
        {
            logger.error("json == null or !(json instanceof JSONObject)");
        }
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 是否是一次性公告
     *
     * @return
     */
    public boolean isOnceNotice()
    {
        return interval >= duration;
    }

    /**
     * 是否执行过
     *
     * @return
     */
    public boolean hasExecuted()
    {
        return lastNoticeTime > 0;
    }

    public int getId()
    {
        return id;
    }

    public int getType()
    {
        return type;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getInterval()
    {
        return interval;
    }

    public String getContent()
    {
        return content;
    }

    public int getLastNoticeTime()
    {
        return lastNoticeTime;
    }

    public void setLastNoticeTime(int lastNoticeTime)
    {
        this.lastNoticeTime = lastNoticeTime;
    }
}
