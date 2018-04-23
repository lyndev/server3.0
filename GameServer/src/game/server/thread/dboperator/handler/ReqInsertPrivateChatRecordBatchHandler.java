package game.server.thread.dboperator.handler;

import game.server.db.game.bean.PrivateChatRecordBean;
import game.server.db.game.dao.PrivateChatRecordDao;
import java.util.List;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqInsertPrivateChatRecordBatchHandler extends DBOperatorHandler
{
    private final List<PrivateChatRecordBean> list;
    
    public ReqInsertPrivateChatRecordBatchHandler(List<PrivateChatRecordBean> list)
    {
        super(0);
        this.list = list;
    }

    public List<PrivateChatRecordBean> getBeanList()
    {
        return list;
    }

    @Override
    public void action()
    {
        PrivateChatRecordDao.insertBatch(list);
        PrivateChatRecordDao.insertAuditBatch(list);
    }
    
}