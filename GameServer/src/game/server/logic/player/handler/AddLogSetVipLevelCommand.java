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
public class AddLogSetVipLevelCommand implements ICommand
{
    private String fgi;
    private String serverId;
    private String roleId;
    private String fedId;
    private String vipLevel;
    private Date date;

    public ICommand set(String fgi, String serverId, String roleId, String fedId, String vipLevel, Date date)
    {
        this.fgi = fgi;
        this.serverId = serverId;
        this.roleId = roleId;
        this.fedId = fedId;
        this.vipLevel = vipLevel;
        this.date = date;
        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogSetVipLevel(
                fgi,
                serverId,
                roleId,
                fedId,
                vipLevel,
                Long.toString(date.getTime() / 1000));
    }
}
