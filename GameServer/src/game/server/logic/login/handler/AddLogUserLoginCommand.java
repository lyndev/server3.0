/**
 * @date 2014/9/15
 * @author ChenLong
 */
package game.server.logic.login.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import java.util.Date;

/**
 * 用户登录日志
 *
 * @author ChenLong
 */
public class AddLogUserLoginCommand implements ICommand
{
    private String client;
    private String fgi;
    private String platform;
    private String fedId;

    private String game_uid;
    private String platform_uid;
    private String gender;
    private String age;

    private String version_res;
    private String version_exe;
    private String device;
    private String ip;

    private Date date;

    public ICommand set(String client, String fgi, String platform, String fedId,
            String game_uid, String platform_uid, String gender, String age,
            String version_res, String version_exe, String device, String ip,
            Date date)
    {
        this.client = client;
        this.fgi = fgi;
        this.platform = platform;
        this.fedId = fedId;

        this.game_uid = game_uid;
        this.platform_uid = platform_uid;
        this.gender = gender;
        this.age = age;

        this.version_res = version_res;
        this.version_exe = version_exe;
        this.device = device;
        this.ip = ip;

        this.date = date;

        return this;
    }

    @Override
    public void action()
    {
        ServicesFactory.getSingleLogService().addLogLogin(client, fgi, platform, fedId,
                game_uid, platform_uid, gender, age,
                version_res, version_exe, device, ip,
                Long.toString(date.getTime() / 1000));
    }
}
