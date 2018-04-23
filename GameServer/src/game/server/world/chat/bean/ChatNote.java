package game.server.world.chat.bean;

import java.util.LinkedList;

/**
 *
 * @author ZouZhaopeng
 */
public abstract class ChatNote
{
    protected final LinkedList<ChatRecord> records;
    protected int insertNum; //需要插入数据库的消息条数

    public ChatNote()
    {
        this(new LinkedList<ChatRecord>() ,0);
    }
    
    public ChatNote(LinkedList<ChatRecord> records)
    {
        this(records, 0);
    }

    public ChatNote(LinkedList<ChatRecord> records, int insertNum)
    {
        this.records = records;
        this.insertNum = insertNum;
    }

    public int getInsertNum()
    {
        return insertNum;
    }

    public void setInsertNum(int insertNum)
    {
        this.insertNum = insertNum;
    }
    
    public LinkedList<ChatRecord> getRecords()
    {
        return records;
    }
    
    /**
     * 清除聊天记录
     */
    public void clearChatNode(){
        records.clear();
        insertNum = 0;
    }
}
