/**
 * @date 2014/5/19
 * @author ChenLong
 */

package game.server.thread.dboperator.handler;

import game.server.db.game.bean.MailBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.logic.login.service.LoginService;
import java.util.List;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class ResSelectPlayerHandler extends DBOperatorHandler
{
    private final IoSession session;
    private final boolean hasResult;
    private final UserBean userBean;
    private final RoleBean roleBean;
    private final List<MailBean> mailList;

    public ResSelectPlayerHandler(int lineNum, IoSession session, boolean hasResult, UserBean userBean, RoleBean roleBean, List<MailBean> mailList)
    {
        super(lineNum);
        this.session = session;
        this.hasResult = hasResult;
        this.userBean = userBean;
        this.roleBean = roleBean;
        this.mailList = mailList;
    }

    @Override
    public void action()
    {
        LoginService.getInstance().loginResSelect(session, hasResult, userBean, roleBean, mailList);
    }
}
