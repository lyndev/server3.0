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
public class AddLogChangeRoleLevelCommand implements ICommand
{
    private String fgi;
    private String serverId;
    private String roleId;
    private String fedId;
    private String level;
    private Date date;

    public ICommand set(String fgi, String serverId, String roleId, String fedId, String level, Date date)
    {
        this.fgi = fgi;
        this.serverId = serverId;
        this.roleId = roleId;
        this.fedId = fedId;
        this.level = level;
        this.date = date;
        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogChangeRoleLevel(
                fgi,
                serverId,
                roleId,
                fedId,
                level,
                Long.toString(date.getTime() / 1000));
    }
}
