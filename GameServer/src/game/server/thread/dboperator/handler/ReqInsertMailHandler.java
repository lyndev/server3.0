package game.server.thread.dboperator.handler;

import game.server.db.game.bean.MailBean;
import game.server.db.game.dao.MailDao;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqInsertMailHandler extends DBOperatorHandler
{
    private final MailBean mailBean;

    public ReqInsertMailHandler(MailBean mailBean)
    {
        super(0);
        this.mailBean = mailBean;
    }
    
    public MailBean getMailBean()
    {
        return mailBean;
    }

    @Override
    public void action()
    {
        MailDao.insert(mailBean);
    }

}
