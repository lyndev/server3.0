/**
 * @date  2014/5/19
 * @author ChenLong
 */

package game.server.thread.dboperator.handler;

import game.server.db.game.bean.RankBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.db.game.dao.RankDao;
import game.server.db.game.dao.RoleDao;
import game.server.db.game.dao.UserDao;
import game.server.logic.line.GameLineManager;
import org.apache.mina.core.session.IoSession;

/**
 * 请求插入玩家数据
 * @author ChenLong
 */
public class ReqCreatePlayerInsertHandler extends DBOperatorHandler
{
    private final IoSession session;
    private final UserBean userBean;
    private final RoleBean roleBean;
    private final RankBean rankBean;

    public ReqCreatePlayerInsertHandler(int lineNum, IoSession session, UserBean userBean, RoleBean roleBean, RankBean rankBean)
    {
        super(lineNum);
        this.session = session;
        this.userBean = userBean;
        this.roleBean = roleBean;
        this.rankBean = rankBean;
    }

    @Override
    public void action()
    {
        ResCreatePlayerInsertHandler resHandler = null;
        if (UserDao.insert(userBean) != 0 && RoleDao.insert(roleBean) != 0 && RankDao.insert(rankBean) != 0)
            resHandler = new ResCreatePlayerInsertHandler(getLineId(), session, true, userBean, roleBean, rankBean);
        else
            resHandler = new ResCreatePlayerInsertHandler(getLineId(), session, false, null, null, null);

        GameLineManager.getInstance().addCommand(getLineId(), resHandler);
    }
}
