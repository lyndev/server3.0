package game.server.world.chat.bean;

import game.server.db.game.bean.ChatRecordBean;
import org.apache.log4j.Logger;

/**
 * 聊天消息
 *
 * @author ZouZhaopeng
 */
public abstract class ChatRecord
{
    private final static Logger LOG = Logger.getLogger(ChatRecord.class);
    
    private long senderUserId; //发送者的userId
    private long timestamp; //服务器指定的时间戳

    private String senderName; //发送者userName
    private int vip; //发送者vip等级
    private boolean needInsert; //是否需要插入数据库

    public ChatRecord()
    {
        needInsert = true;
    }

    public ChatRecord(long senderUserId, long timestamp, String senderName, int vip)
    {
        this.needInsert = true;
        this.senderUserId = senderUserId;
        this.timestamp = timestamp;
        this.senderName = senderName;
        this.vip = vip;
    }

    public long getSenderUserId()
    {
        return senderUserId;
    }

    public void setSenderUserId(long senderUserId)
    {
        this.senderUserId = senderUserId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    //不同类型记录的差异字段, 用JSON存放
    public abstract String getContent();

    public int getVip()
    {
        return vip;
    }

    public void setVip(int vip)
    {
        this.vip = vip;
    }

    public boolean isNeedInsert()
    {
        return needInsert;
    }

    /**
     * 新消息都需要插入数据库, 只有从数据库取出来的消息不再插入, 需要设置为false,
     * @param needInsert 
     */
    public void setNeedInsert(boolean needInsert)
    {
        this.needInsert = needInsert;
    }

    public void initFrom(ChatRecordBean bean)
    {
        if (bean == null)
        {
            LOG.error("数据库记录bean为空");
            return;
        }
        senderUserId = bean.getSenderId();
        senderName = bean.getSenderName();
        timestamp = bean.getTimestamp();
        vip = bean.getVip();
    }
}
