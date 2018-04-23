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
public class AddLogRoleLoginCommand implements ICommand
{
    private String fgi;
    private String serverId;
    private String roleId;
    private String fedId;
    private String roleName;
    private String roleLevel;
    private String gender;
    private String career;
    private String camp;
    private Date date;

    public ICommand set(String fgi,
            String serverId,
            String roleId,
            String fedId,
            String roleName,
            String roleLevel,
            String gender,
            String career,
            String camp,
            Date date)
    {
        this.fgi = fgi;
        this.serverId = serverId;
        this.roleId = roleId;
        this.fedId = fedId;
        this.roleName = roleName;
        this.roleLevel = roleLevel;
        this.gender = gender;
        this.career = career;
        this.camp = camp;
        this.date = date;
        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogRoleLogin(
                fgi,
                serverId,
                roleId,
                fedId,
                roleName,
                roleLevel,
                gender,
                career,
                camp,
                Long.toString(date.getTime() / 1000));
    }
}
