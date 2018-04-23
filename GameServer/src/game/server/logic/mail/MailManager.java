package game.server.logic.mail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.message.SMessage;
import game.core.util.UUIDUtils;
import game.message.MailMessage;
import game.server.db.game.bean.MailBean;
import game.server.logic.constant.Reasons;
import game.server.logic.item.bean.Item;
import game.server.logic.mail.bean.Mail;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.util.MessageUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * 用于处理在线玩家的邮件
 *
 * @author ZouZhaopeng
 */
public class MailManager
{
    private final static Logger log = Logger.getLogger(MailManager.class);
    private final transient Player owner;
    private final Map<UUID, Mail> map;

    public MailManager(Player owner)
    {
        this.owner = owner;
        this.map = new ConcurrentHashMap<>();
    }

    public List<MailBean> toMailBeanList()
    {
        List<MailBean> mailList = new ArrayList<>();
        Iterator<Map.Entry<UUID, Mail>> itr = map.entrySet().iterator();
        while (itr.hasNext()) //涉及remove操作, 用Iterator来迭代循环
        {
            Mail mail = itr.next().getValue();
            //数据库的操作标记
            if (mail.isFlagMarked())
            {
                mailList.add(mail.toMailBean());
                //如果没有删除标记，则看是否过期
                if (mail.getDeleteFlag() == false && mail.isOverdue() && mail.getAccessory() != 1)
                {
                    sendMailDeleteMsg(UUIDUtils.toCompactString(mail.getId()));
                    mail.setDeleteFlag();
                }
            }
            else
            {
                //没有数数据库操作标记:如果是已读，且过期
                if (mail.getIsRead() == 1 && mail.isOverdue() && mail.getAccessory() != 1 )
                {
                    sendMailDeleteMsg(UUIDUtils.toCompactString(mail.getId()));
                    mail.setDeleteFlag();
                    mailList.add(mail.toMailBean());
                }
            }
            if (mail.getDeleteFlag())
                itr.remove();
            else
                mail.resetFlag();
        }
        return mailList;

    }

    private Mail getMail(UUID mailId)
    {
        return map.get(mailId);
    }

    public void putMail(Mail mail)
    {
        map.put(mail.getId(), mail);
    }

    /**
     * 添加邮件
     *
     * @param mail
     */
    public void addMailToPlayer(Mail mail)
    {
        Validate.notNull(mail);

        mail.setInsertFlag(); //设置插入标志
        putMail(mail);
        newMailNotify(mail);
    }

    /**
     * 加载数据库中的指定玩家的所有邮件到玩家邮件map中
     *
     * @param mailList
     */
    public void loginLoadMail(List<MailBean> mailList)
    {
        if (mailList != null)
        {
            for (MailBean mailBean : mailList)
            {
                JSONObject json = (JSONObject) JSON.parse(mailBean.getMailData());
                Mail mail = new Mail();
                mail.fromJson(json);
                if (mail.isOverdue()) //过期删除
                    mail.setDeleteFlag();
                putMail(mail);
            }
        }
    }

    /**
     * 通知玩家有新邮件
     *
     * @param mail 单封邮件
     */
    public void newMailNotify(Mail mail)
    {
        Validate.notNull(mail);

        List<Mail> mailList = new ArrayList<>(1);
        mailList.add(mail);
        newMailNotify(mailList);
    }

    /**
     * 通知玩家有新邮件
     *
     * @param mailList 邮件列表
     */
    public void newMailNotify(List<Mail> mailList)
    {
        Validate.notNull(mailList);

        MailMessage.ResMailList.Builder builder = MailMessage.ResMailList.newBuilder();
        for (Mail mail : mailList)
        {
            builder.addMails(mail.getBuilder());
        }
        MessageUtils.send(owner, new SMessage(MailMessage.ResMailList.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    /**
     * 获取附件资源和物品, 获取后立即删除
     *
     * @param msg
     */
    public void onReqGetAccessory(MailMessage.ReqGetAccessory msg)
    {
        Validate.notNull(msg);

        Mail mail = getMail(UUIDUtils.fromCompactString(msg.getId()));
        if (mail == null || mail.getDeleteFlag() || mail.isOverdue())
        {//如果客户端请求删除的邮件找不到, 说明客户端存在,而服务端不存在, 那么也应该让客户端删除这封邮件(mail == null)
            sendMailDeleteMsg(msg.getId());
            if (mail != null && mail.isOverdue())
                mail.setDeleteFlag();
            return;
        }

        //邮件有效, 领取物品
        sendMailDeleteMsg(msg.getId()); //先发送删除消息给客户端, 否则会不能删除邮件
        mail.setDeleteFlag();
        Date nowTime = Calendar.getInstance().getTime();
        for (Resource resource : mail.getResourceList())
        {
            owner.getBackpackManager().addResource(resource.getType(), resource.getAmount(), true, Reasons.MAIL, nowTime);
        }
        mail.getResourceList().clear();

        for (Item item : mail.getItemList())
        {
            owner.getBackpackManager().addItem(item, true, Reasons.MAIL);
        }
        mail.getItemList().clear();
    }

    /**
     * 指定邮件已读
     *
     * @param msg
     */
    public void onReqMailRead(MailMessage.ReqMailRead msg)
    {
        Validate.notNull(msg);

        Mail mail = getMail(UUIDUtils.fromCompactString(msg.getId()));
        if (mail != null)
        {
            if (mail.getIsRead() == 1)
            {
                log.error("一封邮件只能读一次，客户端你怎么了");
            }
            mail.setIsRead(1); //设置为已读
            mail.setModifyFlag();
            //判断类型是系统的直接删除，如果是手动邮件：判断是否过期，过期则删除，不过期
        }
    }

    /**
     * 客户端请求删除指定邮件
     *
     * @param msg
     */
    public void onReqMailDelete(MailMessage.ReqMailDelete msg)
    {
        Validate.notNull(msg);

        Mail mail = getMail(UUIDUtils.fromCompactString(msg.getId()));
        if (mail != null)
        {
            sendMailDeleteMsg(msg.getId());
            mail.setDeleteFlag(); //先设置了删除标记, 向客户端发送删除消息会找不到邮件
        }
    }

    private void sendMailDeleteMsg(String id)
    {
        MailMessage.ResMailDelete.Builder builder = MailMessage.ResMailDelete.newBuilder();
        builder.setId(id);
        MessageUtils.send(owner, new SMessage(MailMessage.ResMailDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    /**
     * 发送指定玩家的邮件列表
     *
     */
    public void sendMailList()
    {
        MailMessage.ResMailList.Builder builder = MailMessage.ResMailList.newBuilder();
        for (Mail mail : map.values())
        {
            if (!mail.getDeleteFlag())
            {
                //如果邮件是已读状态，且邮件已经过期（这里还要处理带附件的）
                if (mail.getIsRead() == 1 && mail.isOverdue() && mail.getAccessory() != 1)
                    mail.setDeleteFlag();
                else
                    builder.addMails(mail.getBuilder());
            }
        }
        MessageUtils.send(owner, new SMessage(MailMessage.ResMailList.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    /**
     * 返回邮件列表
     *
     * @param msg
     */
    public void onReqMailList(MailMessage.ReqMailList msg)
    {
        Validate.notNull(msg);
        sendMailList();
    }

    /**
     * 客户端初始化完成后, 发送邮件列表
     *
     */
    public void clientInitializeOver()
    {
        sendMailList();
    }
}
