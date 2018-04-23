package game.server.world.chat.bean;

import game.server.db.game.bean.WorldChatRecordBean;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ReqInsertWorldChatRecordBatchHandler;
import game.server.world.chat.ChatManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 世界聊天记录
 *
 * @author ZouZhaopeng
 */
public class WorldChatNote extends ChatNote
{
    private final static Logger LOG = Logger.getLogger(WorldChatNote.class);

    public WorldChatNote()
    {
        super();
    }

    public void addChatRecord(ChatRecord chatMsg)
    {
        records.addLast(chatMsg);
        ++insertNum;
        checkInsert();
        int count = records.size();
        while (count > ChatManager.getDisplayLimit())
        {
            records.removeFirst();
            --count;
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
        List<WorldChatRecordBean> list = new ArrayList<>();
        for (ChatRecord cr : records)
        {
            if (cr.isNeedInsert())
            {
                list.add(toWorldChatRecordBean(cr));
                cr.setNeedInsert(false);
            }
        }
        if (!list.isEmpty())
        {
            insertNum -= list.size();
            insertBatch(list);
        }
    }

    public WorldChatRecordBean toWorldChatRecordBean(ChatRecord record)
    {
        if (record == null)
        {
            LOG.error("toWorldChatRecordBean 参数为空");
            return null;
        }

        WorldChatRecordBean bean = new WorldChatRecordBean();
        bean.setSenderId(record.getSenderUserId());
        bean.setSenderName(record.getSenderName());
        bean.setTimestamp(record.getTimestamp());
        bean.setContent(record.getContent());
        bean.setType(1); //世界1, 公会2, 私聊3
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
//            GameDBOperator.getInstance().submitRequest(new ReqInsertWorldChatRecordHandler(toWorldChatRecordBean(record)));
//        }
//        else
//        {
//            LOG.error("空的记录, 未插入数据库");
//        }
//    }
    public void insertBatch(List<WorldChatRecordBean> list)
    {
        if (list != null && !list.isEmpty())
        {
            GameDBOperator.getInstance().submitRequest(new ReqInsertWorldChatRecordBatchHandler(list));
        }
        else
        {
            LOG.error("空的记录, 未插入数据库");
        }
    }

}
