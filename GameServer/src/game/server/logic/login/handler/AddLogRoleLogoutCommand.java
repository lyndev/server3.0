/**
 * @date 2014/7/26
 * @author ChenLong
 */
package game.server.logic.login.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import java.util.Date;

/**
 *
 * @author ChenLong
 */
public class AddLogRoleLogoutCommand implements ICommand
{
    private String fgi;
    private String serverId;
    private String roleId;
    private String fedId;
    private long timeSpent;
    private Date date;

    public ICommand set(String fgi, String serverId, String roleId, String fedId, long timeSpent, Date date)
    {
        this.fgi = fgi;
        this.serverId = serverId;
        this.roleId = roleId;
        this.fedId = fedId;
        this.timeSpent = timeSpent;
        this.date = date;
        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogRoleLogout(
                fgi,
                serverId,
                roleId,
                fedId,
                Long.toString(timeSpent),
                Long.toString(date.getTime() / 1000));
        // 断线时调用"用户登出"日志接口
        ServicesFactory.getSingleLogService().addLogLogout(fgi, fedId, Long.toString(timeSpent), Long.toString(date.getTime() / 1000));
    }
}
