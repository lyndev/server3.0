/**
 * @date 2014/5/19
 * @author ChenLong
 */
package game.server.thread.dboperator.handler;

import game.server.db.game.bean.RankBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.logic.login.service.LoginService;
import org.apache.mina.core.session.IoSession;

/**
 * 创建角色插入角色数据返回
 * @author ChenLong
 */
public class ResCreatePlayerInsertHandler extends DBOperatorHandler
{
    private final boolean isSuccess;
    private final IoSession session;
    private final UserBean userBean;
    private final RoleBean roleBean;
    private final RankBean rankBean;

    public ResCreatePlayerInsertHandler(int lineNum, IoSession session, boolean isSuccess, UserBean userBean, RoleBean roleBean, RankBean rankBean)
    {
        super(lineNum);
        this.session = session;
        this.isSuccess = isSuccess;
        this.userBean = userBean;
        this.roleBean = roleBean;
        this.rankBean = rankBean;
    }

    @Override
    public void action()
    {
        LoginService.getInstance().createRoleInsertRes(session, isSuccess, userBean, roleBean, rankBean);
    }
}
