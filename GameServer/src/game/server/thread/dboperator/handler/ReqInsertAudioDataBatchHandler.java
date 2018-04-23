package game.server.thread.dboperator.handler;

import game.server.db.game.bean.AudioDataBean;
import game.server.db.game.dao.AudioDataDao;
import java.util.List;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqInsertAudioDataBatchHandler extends DBOperatorHandler
{
private final List<AudioDataBean> list;
    
    public ReqInsertAudioDataBatchHandler(List<AudioDataBean> list)
    {
        super(0);
        this.list = list;
    }

    public List<AudioDataBean> getBeanList()
    {
        return list;
    }

    @Override
    public void action()
    {
        AudioDataDao.insertBatch(list);
        AudioDataDao.insertAuditBatch(list);
    }
}
