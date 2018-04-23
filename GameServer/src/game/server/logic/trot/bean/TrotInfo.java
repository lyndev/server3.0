/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.trot.bean;

import game.server.logic.trot.filter.TrotFilter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @date   2014-7-15
 * @author pengmian
 */
public class TrotInfo 
{
    private TrotType type;      //公告类型
    private long time;          //公告第一次发送的延迟时间
    private String content;     //公告内容
    private int count;          //公告次数
    private int interval;       //两次公告之间的间隔时间
    private TrotFilter filter;  //公告过滤器（由类型决定）
    private String parameter;   //参数（Filter内部使用）
    
    public TrotInfo(String content)
    {
        type = TrotType.TROT_TO_ALL;
        time = 1;
        count = 1;
        interval = 3;
        parameter = "";
        this.content = content;
    }
       

    public TrotType getType()
    {
        return type;
    }

    public void setType(TrotType type)
    {
        this.type = type;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        if (time <= 0)
        {
            time = 1;
        }
        this.time = time;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getInterval()
    {
        return interval;
    }

    public void setInterval(int interval)
    {
        if (interval <= 0)
        {
            interval = 3;
        }
        this.interval = interval;
    }

    public TrotFilter getFilter()
    {
        return filter;
    }

    public void setFilter(TrotFilter filter)
    {
        this.filter = filter;
    }

    public String getParameter()
    {
        return parameter;
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    public boolean isValid()
    {
        return filter != null 
                && content != null
                && !content.trim().equalsIgnoreCase("")
                && type.value() <= TrotType.TROT_TO_PLATFORM.value() 
                && type.value() >= TrotType.TROT_TO_ALL.value();
    }
    
}
