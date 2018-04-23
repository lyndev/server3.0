/**
 * @date 2014/5/19
 * @author ChenLong
 */

package game.server.thread.dboperator.handler;

import game.server.db.game.bean.MailBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.dao.MailDao;
import game.server.db.game.dao.UserDao;
import game.server.logic.line.GameLineManager;
import java.util.List;
import org.apache.mina.core.session.IoSession;

/**
 * 请求查询玩家
 * @author ChenLong
 */
public class ReqSelectPlayerHandler extends DBOperatorHandler
{
    private final String userName;
    private final IoSession session;

    public ReqSelectPlayerHandler(int lineNum, IoSession session, String userName)
    {
        super(lineNum);
        this.session = session;
        this.userName = userName;
    }

    @Override
    public void action()
    {
        UserRoleBean userRoleBean = UserDao.selectJoinByName(userName);
        ResSelectPlayerHandler resHandler = null;
        if (userRoleBean != null)
        {
            UserBean userBean = new UserBean();
            RoleBean roleBean = new RoleBean();
            userRoleBean.splitUserRole(userBean, roleBean);
            List<MailBean> mailList = MailDao.selectByRoleId(userRoleBean.getRoleId());
            resHandler = new ResSelectPlayerHandler(getLineId(), session, true, userBean, roleBean, mailList);
        }
        else
        {
            resHandler = new ResSelectPlayerHandler(getLineId(), session, false, null, null, null);
        }
        GameLineManager.getInstance().addCommand(getLineId(), resHandler);
    }
}
