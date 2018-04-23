package game.server.world.chat.bean;

//import game.server.db.game.bean.FectionChatRecordBean;
import game.server.thread.dboperator.GameDBOperator;
//import game.server.thread.dboperator.handler.ReqInsertFectionChatRecordBatchHandler;
import game.server.world.chat.ChatManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 一个公会的聊天记录
 *
 * @author ZouZhaopeng
 */
public class FectionChatNote extends ChatNote
{
    private final static Logger LOG = Logger.getLogger(FectionChatNote.class);

    private long fectionId; //公会id

    public FectionChatNote()
    {
        super();
    }

    public FectionChatNote(long fectionId)
    {
        super();
        this.fectionId = fectionId;
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
//        List<FectionChatRecordBean> list = new ArrayList<>();
//        for (ChatRecord cr : records)
//        {
//            if (cr.isNeedInsert())
//            {
//                list.add(toFectionChatRecordBean(cr));
//                cr.setNeedInsert(false);
//            }
//        }
//        if (!list.isEmpty())
//        {
//            insertNum -= list.size();
//            insertBatch(list);
//        }
    }

//    public FectionChatRecordBean toFectionChatRecordBean(ChatRecord record)
//    {
//        if (record == null)
//        {
//            LOG.error("toFectionChatRecordBean 参数为空");
//            return null;
//        }
//        FectionChatRecordBean bean = new FectionChatRecordBean();
//        bean.setSenderId(record.getSenderUserId());
//        bean.setSenderName(record.getSenderName());
//        bean.setTimestamp(record.getTimestamp());
//        bean.setContent(record.getContent());
//        bean.setType(2); //世界1, 公会2, 私聊3
//        bean.setFectionId(fectionId);
//        bean.setVip(record.getVip());
//        if (record instanceof AudioRecord)
//        {
//            bean.setIsAudio(1);
//        }
//        else
//        {
//            bean.setIsAudio(0);
//        }
//
//        return bean;
//    }

//    public void insert(ChatRecord record)
//    {
//        if (record != null)
//        {
//            GameDBOperator.getInstance().submitRequest(new ReqInsertFectionChatRecordHandler(toFectionChatRecordBean(record)));
//        }
//        else
//        {
//            LOG.error("空的记录, 未插入数据库");
//        }
//    }

//    public void insertBatch(List<FectionChatRecordBean> list)
//    {
//        if (list != null && !list.isEmpty())
//        {
////            GameDBOperator.getInstance().submitRequest(new ReqInsertFectionChatRecordBatchHandler(list));
//        }
//        else
//        {
//            LOG.error("空的记录, 未插入数据库");
//        }
//    }

    public long getFectionId()
    {
        return fectionId;
    }

    public void setFectionId(long fectionId)
    {
        this.fectionId = fectionId;
    }

}
