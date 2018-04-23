package game.server.db.game.bean;

import java.util.BitSet;

/**
 *
 * @author ZouZhaopeng
 */
public class MailBean
{
    private String id;

    private String senderName;
    private int type;

    private String receiverId;
    private String receiverName;

    private int accessory;
    private int isRead;
    private int sendTime;
    private int deadLine;

    private final BitSet saveFlag = new BitSet(3);

    private String mailData;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getReceiverId()
    {
        return receiverId;
    }

    public void setReceiverId(String receiverId)
    {
        this.receiverId = receiverId;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public int getAccessory()
    {
        return accessory;
    }

    public void setAccessory(int accessory)
    {
        this.accessory = accessory;
    }

    public int getIsRead()
    {
        return isRead;
    }

    public void setIsRead(int isRead)
    {
        this.isRead = isRead;
    }

    public int getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(int sendTime)
    {
        this.sendTime = sendTime;
    }

    public int getDeadLine()
    {
        return deadLine;
    }

    public void setDeadLine(int deadLine)
    {
        this.deadLine = deadLine;
    }

    public void setDeleteFlag()
    {
        setFlag(0);
    }

    public void setInsertFlag()
    {
        setFlag(1);
    }

    public void setModifyFlag()
    {
        setFlag(2);
    }

    public void setFlag(int pos)
    {
        saveFlag.set(pos);
    }

    public boolean getDeleteFlag()
    {
        return getFlag(0);
    }

    public boolean getInsertFlag()
    {
        return getFlag(1);
    }

    public boolean getModifyFlag()
    {
        return getFlag(2);
    }

    public boolean getFlag(int pos)
    {
        return saveFlag.get(pos);
    }

    public boolean isFlagMarked()
    {
        return getDeleteFlag() || getInsertFlag() || getModifyFlag();
    }

    public void resetFlag()
    {
        saveFlag.clear();
    }

    public String getMailData()
    {
        return mailData;
    }

    public void setMailData(String mailData)
    {
        this.mailData = mailData;
    }

}
