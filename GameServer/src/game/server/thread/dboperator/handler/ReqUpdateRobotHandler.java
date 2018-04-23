package game.server.thread.dboperator.handler;

import game.server.db.game.bean.RoleBean;
import game.server.db.game.dao.RoleDao;

public class ReqUpdateRobotHandler extends DBOperatorHandler
{
    private final RoleBean bean;

    public ReqUpdateRobotHandler(RoleBean bean)
    {
        super(0);
        this.bean = bean;
    }

    @Override
    public void action()
    {
        RoleDao.updateWithLog(bean);
    }
}
