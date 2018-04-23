package script.login;

import game.core.script.IScript;
import game.server.logic.item.bean.Item;
import game.server.logic.loginGift.LoginGiftService;
import game.server.logic.loginGift.bean.LoginGiftBean;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.service.MailService;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.util.ScriptArgs;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 登陆有礼活动
 * 
 * @author ZouZhaopeng
 */
public class LoginGiftScript implements IScript
{
    private final Logger log = Logger.getLogger(LoginGiftScript.class);

    @Override
    public int getId()
    {
        return 1029;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        /*
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.PLAYER);

        log.info("call java script " + scriptId);

        if (player != null)
        {
            Map<Integer, LoginGiftBean> map = LoginGiftService.getInstance().getMap();
            for (LoginGiftBean bean : map.values())
            {
                if (check(player, bean))
                {
                    int type = 2;
                    int accessory = 0;
                    String senderName = "";
                    int deadLine = (int) (System.currentTimeMillis() / 1000) + 60 * 60 * 24 * 7;
                    if (bean.getItemList() != null && !bean.getItemList().isEmpty())
                    {
                        accessory = 1;
                    }
                    Mail mail = new Mail();
                    mail.setAll(bean.getMail_title(), bean.getMail_content(), senderName, type, 0L, "", accessory, deadLine, null, bean.getItemList());
                    MailService.getInstance().sendByType(2, UniqueId.toBase36(player.getUserId()), mail);
                    player.getLoginGiftManager().add(bean.getAward_id());
                }
            }
        }
        else
        {
            log.error("player == null");
        }
        */
        return null;
    }

    private boolean check(Player player, LoginGiftBean bean)
    {
        return !bean.isDisable() && bean.isOpening() && bean.isLevelFit(player.getRoleLevel()) && !player.getLoginGiftManager().contains(bean.getAward_id());
    }
}
