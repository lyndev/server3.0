package game.server.db.game.bean;

/**
 * 私聊消息 数据库bean
 * 
 * @author ZouZhaopeng
 */
public class PrivateChatRecordBean extends ChatRecordBean
{
    private String conversationId; //会话id
    private long receiverId; //接收者userId

    public PrivateChatRecordBean()
    {
    }
    
    public String getConversationId()
    {
        return conversationId;
    }

    public void setConversationId(String conversationId)
    {
        this.conversationId = conversationId;
    }

    public long getReceiverId()
    {
        return receiverId;
    }

    public void setReceiverId(long receiverId)
    {
        this.receiverId = receiverId;
    }
    
}
