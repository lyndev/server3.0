package game.server.thread.dboperator.handler;

import game.server.db.game.bean.WorldChatRecordBean;
import game.server.db.game.dao.WorldChatRecordDao;
import java.util.List;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqInsertWorldChatRecordBatchHandler extends DBOperatorHandler
{
    private final List<WorldChatRecordBean> list;
    
    public ReqInsertWorldChatRecordBatchHandler(List<WorldChatRecordBean> list)
    {
        super(0);
        this.list = list;
    }

    public List<WorldChatRecordBean> getBean()
    {
        return list;
    }

    @Override
    public void action()
    {
        WorldChatRecordDao.insertBatch(list);
        WorldChatRecordDao.insertAuditBatch(list);
    }
}
