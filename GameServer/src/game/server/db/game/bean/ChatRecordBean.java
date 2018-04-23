package game.server.db.game.bean;

/**
 * 聊天消息 基本数据bean
 * 
 * @author ZouZhaopeng
 */
public abstract class ChatRecordBean
{
    private long senderId; //发送者, 创建者
    private String senderName; //发送者名字(查询时可读)
    private long timestamp; //发送时间戳
    private String content; //文本消息为内容 + 链接信息, 语音消息为UUID和时长等信息的json
    private int type; // 1. 文本; 2. 语音
    private int vip; //vip等级
    private int isAudio; //是否为语音消息

    public ChatRecordBean()
    {
    }
    
    public long getSenderId()
    {
        return senderId;
    }

    public void setSenderId(long senderId)
    {
        this.senderId = senderId;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getVip()
    {
        return vip;
    }

    public void setVip(int vip)
    {
        this.vip = vip;
    }

    public int getIsAudio()
    {
        return isAudio;
    }

    public void setIsAudio(int isAudio)
    {
        this.isAudio = isAudio;
    }
    
}
