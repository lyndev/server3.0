package game.server.thread.dboperator.handler;

import game.server.db.game.bean.ProduceExceptionBean;
import game.server.db.game.dao.ProduceExceptionDao;

/**
 *
 * <b>对手信息.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ProduceExceptionHandler extends DBOperatorHandler
{

    private final ProduceExceptionBean bean;

    public ProduceExceptionHandler(int lineNum, ProduceExceptionBean bean)
    {
        super(lineNum);
        this.bean = bean;
    }

    @Override
    public void action()
    {
        ProduceExceptionDao.insert(bean);
    }

}
