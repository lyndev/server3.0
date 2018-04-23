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
public class AddLogCreateRoleCommand implements ICommand
{
    private String fgi;
    private String serverId;
    private String roleId;
    private String fedId;
    private String roleName;
    private String roleLevel;
    private Date date;

    public ICommand set(String fgi, String serverId, String roleId, String fedId,
            String roleName, String roleLevel, Date date)
    {
        this.fgi = fgi;
        this.serverId = serverId;
        this.roleId = roleId;
        this.fedId = fedId;
        this.roleName = roleName;
        this.roleLevel = roleLevel;
        this.date = date;
        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogCreateRole(
                fgi,
                serverId,
                roleId,
                fedId,
                roleName,
                roleLevel,
                "",
                "",
                "",
                Long.toString(date.getTime() / 1000));
    }
}
