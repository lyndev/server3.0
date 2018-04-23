/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.gm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.script.IScript;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.gm.GMCommandMessage;
import game.server.gm.GMCommandUtils;
import game.server.logic.constant.BackstageCmdResult;
import game.server.logic.constant.GlobalTableKey;
import game.server.logic.item.bean.Item;
import game.server.logic.loginGift.LoginGiftService;
import game.server.logic.loginGift.bean.LoginGiftBean;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.service.MailService;
import game.server.logic.notice.NoticeBean;
import game.server.logic.notice.NoticeManager;
import game.server.logic.support.BeanFactory;
import game.server.logic.util.ScriptArgs;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class GmCmdScript implements IScript
{
    private final Logger log = Logger.getLogger(GmCmdScript.class);

    @Override
    public int getId()
    {
        return 1027;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        if (getId() != scriptId)
        {
            log.error("错误的调用, 调用Id = " + scriptId);
        }

        GMCommandMessage msg = (GMCommandMessage) ((ScriptArgs) arg).get(ScriptArgs.Key.OBJECT_VALUE);
        log.info("GMCommand! command = " + msg.getCommand() + "，data = " + msg.getData().toJSONString());

        if (msg.getData() == null)
        {
            msg.getSession().write(GMCommandUtils.createMessage(msg, new JSONObject(), -1));
            return null;
        }
        switch (msg.getCommand())
        {
            case 6010: //邮件GM
                sendMail(msg);
                break;

            case 7010: //发公告
                addNotice(msg);
                break;

            case 7011: //删除公告
                removeNotice(msg);
                break;

            case 3080: //登陆有礼活动
                addLoginGift(msg);
                break;
                
            case 3081: //取消登陆有礼活动
                removeLoginGift(msg);
                break;
                
            default:
                break;
        }
        return null;
    }

    public void sendMail(GMCommandMessage msg)
    {
        JSONArray playerList = msg.getData().getJSONArray("player_list");
        String title = msg.getData().getString("mail_title");
        String content = msg.getData().getString("mail_content");
        JSONArray items = msg.getData().getJSONArray("items");
        StringBuilder players = new StringBuilder();
        if (playerList != null)
        {
            Object ob;
            for (int index = 0; index < playerList.size(); ++index)
            {
                ob = playerList.get(index);
                players.append((String) ob);
                if (index != playerList.size() - 1)
                {
                    players.append(",");
                }
            }
        }
        
        if (title == null || content == null || playerList == null) // items可以为空
        {
            msg.getSession().write(GMCommandUtils.createMessage(msg, new JSONObject(), -1));
            return;
        }

        List<Item> itemList = new ArrayList<>(items.size());
        JSONObject ob;
        for (Object each : items)
        {
            ob = (JSONObject) each;
            int id = ob.getIntValue("item_id");
            int num = ob.getIntValue("item_num");
            Item item = BeanFactory.createItem(id, num);
            if (item != null)
            {
                itemList.add(item);
            }
        }

        int accessory = 1;
        if (itemList.isEmpty())
        {
            accessory = 0;
        }

        int deadLine = (int) (System.currentTimeMillis() / 1000);
        deadLine += 60 * 60 * 24 * 7; //默认有效期7天

        //通过玩家roleId发放
        Mail mail = new Mail();
        mail.setAll(title, content, "系统", 2, 0L, "", accessory, deadLine, null, itemList);
        MailService.getInstance().sendByType(14, players.toString(), mail);
        msg.getSession().write(GMCommandUtils.createMessage(msg, new JSONObject(), 0));
    }

    public void addNotice(GMCommandMessage msg)
    {
        JSONObject jsonObj = msg.getData();
        int notice_type = jsonObj.getIntValue("notice_type");
        int notice_time = jsonObj.getIntValue("notice_time");
        int duration = jsonObj.getIntValue("duration");
        int interval = jsonObj.getIntValue("interval");
        String content = jsonObj.getString("content");

        NoticeBean noticeBean = new NoticeBean((int) (System.currentTimeMillis() / 1000L),
                notice_type, notice_time, duration, interval, content);
        BackstageCmdResult result = NoticeManager.getInstance().addNotice(noticeBean);
        msg.getSession().write(GMCommandUtils.createMessage(msg, new JSONObject(), result.getValue()));
    }

    public void removeNotice(GMCommandMessage msg)
    {
        JSONObject jsonObj = msg.getData();
        int id = jsonObj.getIntValue("id");
        BackstageCmdResult result = NoticeManager.getInstance().removeNotice(id);
        msg.getSession().write(GMCommandUtils.createMessage(msg, new JSONObject(), result.getValue()));
    }

    public void addLoginGift(GMCommandMessage msg)
    {
        JSONObject jsonObj = msg.getData();
        int award_id = jsonObj.getIntValue("award_id");
        String award_name = jsonObj.getString("award_name");
        int time_begin = jsonObj.getIntValue("time_begin");
        int time_end = jsonObj.getIntValue("time_end");
        int level_min = jsonObj.getIntValue("level_min");
        int level_max = jsonObj.getIntValue("level_max");
        String mail_title = jsonObj.getString("mail_title");
        String mail_content = jsonObj.getString("mail_content");
        JSONArray items = jsonObj.getJSONArray("items");
        List<Item> itemList = new ArrayList<>();
        if (items != null)
        {
            for (Object each : items)
            {
                JSONObject itemObj = (JSONObject)each;
                int item_id = itemObj.getIntValue("item_id");
                int item_num = itemObj.getIntValue("item_num");
                Item item = BeanFactory.createItem(item_id, item_num);
                if (item != null)
                {
                    itemList.add(item);
                }
            }
        }
        
        LoginGiftBean bean = new LoginGiftBean(award_id, award_name, time_begin, time_end, level_min, level_max, mail_title, mail_content, itemList, false);
        BackstageCmdResult result;
        if (LoginGiftService.getInstance().putLoginGift(bean))
        {
            result = BackstageCmdResult.Success;
        }
        else
        {
            result = BackstageCmdResult.LoginGiftExist;
        }
        msg.getSession().write(GMCommandUtils.createMessage(msg, result.toJSONObject(), result.getValue()));
        GlobalBean globalBean = new GlobalBean();
        globalBean.setId(GlobalTableKey.LOGIN_GIFT.getKey());
        globalBean.setValue(LoginGiftService.getInstance().toJson().toJSONString());
        if (GlobalDao.update(globalBean) <= 0)
        {
            GlobalDao.insert(globalBean);
        }
    }
    
    public void removeLoginGift(GMCommandMessage msg)
    {
        JSONObject jsonObj = msg.getData();
        int id = jsonObj.getIntValue("id");
        LoginGiftBean bean = LoginGiftService.getInstance().getLoginGift(id);
        BackstageCmdResult result;
        if (bean == null)
        {
            log.error("取消登陆有奖活动失败, 没有找到指定的活动id [" + id + "]");
            result = BackstageCmdResult.LoginGiftNotFound;
        }
        else
        {
            bean.setDisable(true);
            log.info("取消登陆又将活动成功, 取消的活动id [" + id + "]");
            result = BackstageCmdResult.Success;
            GlobalBean globalBean = new GlobalBean();
            globalBean.setId(GlobalTableKey.LOGIN_GIFT.getKey());
            globalBean.setValue(LoginGiftService.getInstance().toJson().toJSONString());
            if (GlobalDao.update(globalBean) <= 0)
            {
                GlobalDao.insert(globalBean);
            }
        }
        msg.getSession().write(GMCommandUtils.createMessage(msg, result.toJSONObject(), result.getValue()));
    }
    
}
