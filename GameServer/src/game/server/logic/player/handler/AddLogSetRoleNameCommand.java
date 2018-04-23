/**
 * @date 2014/7/26
 * @author ChenLong
 */
package game.server.logic.player.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import java.util.Date;

/**
 *
 * @author ChenLong
 */
public class AddLogSetRoleNameCommand implements ICommand
{
    private String fgi;
    private String serverId;
    private String roleId;
    private String fedId;
    private String roleName;
    private Date date;

    public ICommand set(String fgi, String serverId, String roleId, String fedId, String roleName, Date date)
    {
        this.fgi = fgi;
        this.serverId = serverId;
        this.roleId = roleId;
        this.fedId = fedId;
        this.roleName = roleName;
        this.date = date;
        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogSetRoleName(
                fgi,
                serverId,
                roleId,
                fedId,
                roleName,
                Long.toString(date.getTime() / 1000));
    }
}
