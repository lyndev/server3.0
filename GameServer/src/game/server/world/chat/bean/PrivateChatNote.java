package game.server.world.chat.bean;

import game.server.db.game.bean.PrivateChatRecordBean;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ReqInsertPrivateChatRecordBatchHandler;
import game.server.util.UniqueId;
import game.server.world.chat.ChatManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 一次私聊(会话)的聊天记录
 *
 * @author ZouZhaopeng
 */
public class PrivateChatNote extends ChatNote
{
    private final static Logger LOG = Logger.getLogger(PrivateChatNote.class);

    private long playerS; //私聊userId小的一方
    private long playerB; //私聊userId大的一方

    public PrivateChatNote()
    {
        super();
    }

    public PrivateChatNote(long playerS, long playerB)
    {
        super();
        if (playerS < playerB)
        {
            this.playerS = playerS;
            this.playerB = playerB;
        }
        else
        {
            this.playerS = playerB;
            this.playerB = playerS;
        }
    }

    public void addChatRecord(ChatRecord record)
    {
        if (record != null)
        {
            records.addLast(record);
            ++insertNum;
            checkInsert();
            int count = records.size();
            while (count > ChatManager.getDisplayLimit())
            {
                records.removeFirst();
                --count;
            }
        }
        else
        {
            LOG.error("添加的聊天记录为空");
        }
    }

    public void checkInsert()
    {
        if (insertNum > ChatManager.getDisplayLimit() / 2)
        {
            save();
        }
    }

    public void save()
    {
        List<PrivateChatRecordBean> list = new ArrayList<>();
        for (ChatRecord cr : records)
        {
            if (cr.isNeedInsert())
            {
                list.add(toPrivateChatRecordBean(cr));
                cr.setNeedInsert(false);
            }
        }
        if (!list.isEmpty())
        {
            insertNum -= list.size();
            insertBatch(list);
        }
    }

    public long getPlayerS()
    {
        return playerS;
    }

    public void setPlayerS(long playerS)
    {
        this.playerS = playerS;
    }

    public long getPlayerB()
    {
        return playerB;
    }

    public void setPlayerB(long playerB)
    {
        this.playerB = playerB;
    }

    public String getConversationId()
    {
        return makeConversationId(playerS, playerB);
    }

    /**
     * 根据两个玩家userId, 获得二者聊天的唯一会话id <br/>
     * 规则是 smallerId_biggerId
     *
     * @param playerS 聊天的一方userId
     * @param playerB 聊天的另一方userId
     * @return 聊天会话id
     */
    public static String makeConversationId(long playerS, long playerB)
    {
        if (playerS < playerB)
            return new StringBuilder().append(UniqueId.toBase36(playerS)).append("_").append(UniqueId.toBase36(playerB)).toString();
        else
            return new StringBuilder().append(UniqueId.toBase36(playerB)).append("_").append(UniqueId.toBase36(playerS)).toString();
    }

    public PrivateChatRecordBean toPrivateChatRecordBean(ChatRecord record)
    {
        if (record == null)
        {
            LOG.error("toPrivateChatRecordBean 参数为空");
            return null;
        }
        PrivateChatRecordBean bean = new PrivateChatRecordBean();
        bean.setSenderId(record.getSenderUserId());
        bean.setSenderName(record.getSenderName());
        bean.setTimestamp(record.getTimestamp());
        bean.setContent(record.getContent());
        bean.setType(3); //世界1, 公会2, 私聊3
        bean.setConversationId(getConversationId());
        bean.setReceiverId(getOtherPlayer(bean.getSenderId()));
        bean.setVip(record.getVip());
        if (record instanceof AudioRecord)
        {
            bean.setIsAudio(1);
        }
        else
        {
            bean.setIsAudio(0);
        }

        return bean;
    }

//    public void insert(ChatRecord record)
//    {
//        if (record != null)
//        {
//            GameDBOperator.getInstance().submitRequest(new ReqInsertPrivateChatRecordHandler(toPrivateChatRecordBean(record)));
//        }
//        else
//        {
//            LOG.error("空的记录, 未插入数据库");
//        }
//    }

    public void insertBatch(List<PrivateChatRecordBean> list)
    {
        if (list != null && !list.isEmpty())
        {
            GameDBOperator.getInstance().submitRequest(new ReqInsertPrivateChatRecordBatchHandler(list));
        }
        else
        {
            LOG.error("空的记录, 未插入数据库");
        }
    }
    
    public Long getOtherPlayer(long self)
    {
        if (playerS == self)
        {
            return playerB;
        }
        else
        {
            return playerS;
        }
    }

}
