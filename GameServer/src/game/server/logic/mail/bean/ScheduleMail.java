/**
 * @date 2014/8/6
 * @author ChenLong
 */
package game.server.logic.mail.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.support.IJsonConverter;
import java.util.UUID;

/**
 * 定时邮件
 *
 * @author ChenLong
 */
public class ScheduleMail implements IJsonConverter
{
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int days;
    private Mail mail;
    
    private volatile int remainDays;
    private volatile long lastSendTime = 0;

    public ScheduleMail()
    {

    }

    public ScheduleMail(int year, int month, int date, int hour, int minute, int days, Mail mail)
    {
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.mail = mail;
        this.remainDays = days;
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("year", year);
        jsonObj.put("month", month);
        jsonObj.put("date", date);
        jsonObj.put("hour", hour);
        jsonObj.put("minute", minute);
        jsonObj.put("days", days);
        jsonObj.put("remainDays", remainDays);
        jsonObj.put("lastSendTime", lastSendTime);
        jsonObj.put("mail", mail.toJson());

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject jsonObj = (JSONObject) json;
        this.year = jsonObj.getIntValue("year");
        this.month = jsonObj.getIntValue("month");
        this.date = jsonObj.getIntValue("date");
        this.hour = jsonObj.getIntValue("hour");
        this.minute = jsonObj.getIntValue("minute");
        this.days = jsonObj.getIntValue("days");
        this.remainDays = jsonObj.getIntValue("remainDays");
        this.lastSendTime = jsonObj.getLong("lastSendTime");

        JSONObject mailJsonObj = jsonObj.getJSONObject("mail");
        Mail tmail = new Mail();
        tmail.fromJson(mailJsonObj);
        this.mail = tmail;
    }

    public UUID getId()
    {
        return mail.getId();
    }

    /**
     * 将remainDays减一, 并返回结果
     *
     * @return
     */
    public synchronized int decRemainDays()
    {
        lastSendTime = System.currentTimeMillis();
        return --remainDays;
    }

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

    public int getDate()
    {
        return date;
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public int getDays()
    {
        return days;
    }

    public synchronized int getRemainDays()
    {
        return remainDays;
    }

    public long getLastSendTime()
    {
        return lastSendTime;
    }

    public Mail getMail()
    {
        return mail;
    }
}
