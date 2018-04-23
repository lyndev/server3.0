/**
 * @date 2014/9/13
 * @author ChenLong
 */
package script.player;

import game.core.script.IScript;
import game.server.logic.constant.ResourceType;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.service.MailService;
import game.server.logic.struct.Resource;
import game.server.logic.support.RoleView;
import game.server.logic.util.ScriptArgs;
import java.util.LinkedList;
import java.util.List;

/**
 * 发送月卡福利脚本
 *
 * @author ChenLong
 */
public class IssueMonthCardAwardScript implements IScript
{
    @Override
    public int getId()
    {
        return 1028;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        List<RoleView> awardPlayers = (List<RoleView>) args.get(ScriptArgs.Key.ARG1);

        Resource resource = new Resource(ResourceType.GOLD_BULLION.value(), 130); // 130元宝
        List<Resource> resources = new LinkedList<>();
        resources.add(resource);

        Mail mail = new Mail();
        mail.setAll("月卡福利", "亲，月卡福利哟~~~", "游戏服务器", 2, 0, "", 1, Integer.MAX_VALUE, resources, null);
        MailService.getInstance().sendAll(awardPlayers, mail);

        return null;
    }
}
