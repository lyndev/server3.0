/**
 * @date 2014/7/17
 * @author ChenLong
 */
package script.login;

import game.server.logic.login.handler.AddLogCreateRoleCommand;
import game.core.script.IScript;
import game.server.db.game.bean.RankBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class CreateRoleSuccessScript implements IScript
{
    private final Logger log = Logger.getLogger(CreateRoleSuccessScript.class);

    @Override
    public int getId()
    {
        return 1008;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;

        IoSession session = (IoSession) args.get(ScriptArgs.Key.ARG1);
        boolean isSuccess = (Boolean) args.get(ScriptArgs.Key.ARG2);
        UserBean userBean = (UserBean) args.get(ScriptArgs.Key.ARG3);
        RoleBean roleBean = (RoleBean) args.get(ScriptArgs.Key.ARG4);
        RankBean rankBean = (RankBean) args.get(ScriptArgs.Key.ARG5);

        log.info("call CreateRoleSuccessScript script " + scriptId);

        addLogCreateRole(session, userBean, roleBean);

        return null;
    }

    private void addLogCreateRole(IoSession session, UserBean userBean, RoleBean roleBean)
    {
        AddLogCreateRoleCommand command = new AddLogCreateRoleCommand();
        command.set(roleBean.getFgi(),
                Integer.toString(roleBean.getServerId()),
                roleBean.getRoleId(),
                roleBean.getFedId(),
                roleBean.getRoleName(),
                Integer.toString(roleBean.getRoleLevel()),
                new Date());
        BackLogProcessor.getInstance().addCommand(command);
    }
}
