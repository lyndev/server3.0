package script.login;

import game.core.script.IScript;
import game.server.logic.item.bean.Item;
import game.server.logic.mail.service.MailService;
import game.server.logic.mail.bean.Mail;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.util.ScriptArgs;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class NewPlayerGiftScript implements IScript
{
    private final Logger log = Logger.getLogger(NewPlayerGiftScript.class);

    @Override
    public int getId()
    {
        return 1024;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.PLAYER);

        log.info("call java script " + scriptId);

        if (player != null)
        {
            String title = "新手豪华大礼包";
            String content = "亲爱的导演：\n    热烈祝贺您的年度大戏《万万没想到之尖叫三国》正式开拍！在此我们特意为您送上 {ffd800}新手豪华大礼包{3f2311} 以表达我们恭贺之意！";
            String senderName = "";
            int type = 2;
            int accessory = 1;
            int deadLine = (int)(System.currentTimeMillis() / 1000) + 60 * 60 * 24 * 7;
            //发放的物品列表
            int itemID = 10001; //氪金大礼包ID
            int itemNum = 1;
            List<Item> itemList = new ArrayList<>();
            itemList.add(BeanFactory.createItem(itemID, itemNum));
            Mail mail = new Mail();
            mail.setAll(title, content, senderName, type, 0L, "", accessory, deadLine, null, itemList);
            MailService.getInstance().sendByType(2, UniqueId.toBase36(player.getUserId()), mail);
        }
        else
        {
            log.error("player == null");
        }
        return null;
    }
}