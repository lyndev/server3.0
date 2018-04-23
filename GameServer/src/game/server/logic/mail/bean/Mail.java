package game.server.logic.mail.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.util.UUIDUtils;
import game.message.MailMessage;
import game.server.db.game.bean.MailBean;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.IJsonConverter;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class Mail implements IJsonConverter
{
    private static final Logger log = Logger.getLogger(Mail.class);
    private static final int DELETE_FLAG = 0;
    private static final int INSERT_FLAG = 1;
    private static final int MODIFY_FLAG = 2;

    private UUID id;                      //邮件ID
    private String title;                   //邮件标题
    private String content;             //邮件内容/正文

    private String senderName;      //发送者名字
    private int type;                         //邮件类型 (1为竞技系统、2普通邮件、3为战斗视频)

    private long receiverId;          //接收者ID(roleID)
    private String receiverName;    //接收者名字(roleName)

    private int accessory;                  //是否包含附件
    private int isRead;                       //是否已读
    private int sendTime;                   //发送时间, 在发送的时刻设置
    private int deadLine;                   //过期时间点(单位: 秒)

    private List<Resource> resourceList = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();
    private final BitSet saveFlag = new BitSet(3);

    public Mail()
    {
        id = UUID.randomUUID();
        sendTime = (int) (System.currentTimeMillis() / 1000);
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public long getReceiverId()
    {
        return receiverId;
    }

    public void setReceiverId(long receiverId)
    {
        this.receiverId = receiverId;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public int getAccessory()
    {
        return accessory;
    }

    public void setAccessory(int accessory)
    {
        this.accessory = accessory;
    }

    public int getIsRead()
    {
        return isRead;
    }

    public void setIsRead(int isRead)
    {
        this.isRead = isRead;
    }

    public int getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(int sendTime)
    {
        this.sendTime = sendTime;
    }

    public int getDeadLine()
    {
        return deadLine;
    }

    public void setDeadLine(int deadLine)
    {
        this.deadLine = deadLine;
    }

    public List<Resource> getResourceList()
    {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList)
    {
        this.resourceList = resourceList;
    }

    public List<Item> getItemList()
    {
        return itemList;
    }

    public void setItemList(List<Item> itemList)
    {
        this.itemList = itemList;
    }

    public void setDeleteFlag()
    {
        setFlag(DELETE_FLAG);
    }

    public void setInsertFlag()
    {
        setFlag(INSERT_FLAG);
    }

    public void setModifyFlag()
    {
        setFlag(MODIFY_FLAG);
    }

    public void setFlag(int pos)
    {
        saveFlag.set(pos);
    }

    public boolean getDeleteFlag()
    {
        return getFlag(DELETE_FLAG);
    }

    public boolean getInsertFlag()
    {
        return getFlag(INSERT_FLAG);
    }

    public boolean getModifyFlag()
    {
        return getFlag(MODIFY_FLAG);
    }

    public boolean getFlag(int pos)
    {
        return saveFlag.get(pos);
    }

    public boolean isFlagMarked()
    {
        return getDeleteFlag() || getInsertFlag() || getModifyFlag();
    }

    public void resetFlag()
    {
        saveFlag.clear();
    }

    /**
     * 用模板初始化一封邮件, 除了收件人id和收件人名字, 其他都已设置好
     *
     * @param other 模板邮件
     */
    public void initFrom(Mail other)
    {
        setAll(other.title, other.content, other.senderName, other.type, 0L, null, other.accessory, other.deadLine, other.resourceList, other.itemList);
    }

    /**
     * 设置邮件内容
     *
     * @param title 邮件标题
     * @param content 邮件内容
     * @param senderName 发送者名字
     * @param type 发送类型 （1系统邮件、2手动邮件（运营发送的））
     * @param receiverId 接收者角色ID (roleId)
     * @param receiverName 接收者角色名 (roleName)
     * @param accessory 是否有附件 (1是, 0否)
     * @param deadLine 邮件过期时间戳(unixtimestamp), 过期邮件会被删除
     * @param resourceList 资源列表
     * @param itemList 物品列表
     */
    public void setAll(String title, String content, String senderName, int type, long receiverId, String receiverName,
            int accessory, int deadLine, List<Resource> resourceList, List<Item> itemList)
    {
        setTitle(title);
        setContent(content);
        setSenderName(senderName);
        setReceiverId(receiverId);
        setReceiverName(receiverName);
        setAccessory(accessory);
        setIsRead(0);
        setType(type);
        setDeadLine(deadLine);
        resetFlag();
        if (resourceList != null)
        {
            List<Resource> lst = new ArrayList<>(resourceList.size());
            for (Resource res : resourceList)
            {
                lst.add(new Resource(res.getType(), res.getAmount()));
            }
            setResourceList(lst);
        }
        if (itemList != null)
        {
            List<Item> lst = new ArrayList<>(itemList.size());
            for (Item item : itemList)
            {
                try
                {
                    lst.add((Item)item.clone());
                }
                catch (CloneNotSupportedException e)
                {
                    log.error(e);
                    log.error("物品不支持Clone, 物品id = " + item.getId() + ", 类型 = " + item.getType());
                }
            }
            setItemList(lst);
        }
    }

    /**
     * 判断邮件是否已过期, 过期邮件应该被删除
     *
     * @return
     */
    public boolean isOverdue()
    {
        int nowTimeInt = (int) (System.currentTimeMillis() / 1000);
        return nowTimeInt > getDeadLine();
    }

    public MailBean toMailBean()
    {
        MailBean mailBean = new MailBean();
        mailBean.setId(UUIDUtils.toCompactString(id));
        mailBean.setSenderName(senderName);
        mailBean.setType(type);
        mailBean.setReceiverId(UniqueId.toBase36(receiverId));
        mailBean.setReceiverName(receiverName);
        mailBean.setAccessory(accessory);
        mailBean.setIsRead(isRead);
        mailBean.setSendTime(sendTime);
        mailBean.setDeadLine(deadLine);
        if (getDeleteFlag())
            mailBean.setDeleteFlag();
        if (getInsertFlag())
            mailBean.setInsertFlag();
        if (getModifyFlag())
            mailBean.setModifyFlag();
        mailBean.setMailData(toJson().toJSONString());

        return mailBean;
    }

    public static Mail fromMailBean(MailBean mailBean)
    {
        Mail mail = null;
        if (mailBean != null)
        {
            JSONObject json = (JSONObject) JSONObject.parse(mailBean.getMailData());
            mail = new Mail();
            mail.fromJson(json);
            mail.resetFlag();
        }
        return mail;
    }

    public MailMessage.Mail.Builder getBuilder()
    {
        MailMessage.Mail.Builder builder = MailMessage.Mail.newBuilder();
        builder.setId(UUIDUtils.toCompactString(id));
        builder.setTitle(title);
        builder.setContent(content);
        builder.setType(type);
        builder.setSenderName(senderName);
        builder.setReceiverId(UniqueId.toBase36(receiverId));
        builder.setReceiverName(receiverName);
        builder.setAccessory(accessory);
        builder.setRead(isRead);
        builder.setSendTime(sendTime);
        builder.setDeadLine(deadLine);
        if (getResourceList() != null)
        {
            for (Resource resource : getResourceList())
            {
                builder.addResources(resource.getMessageBuilder());
            }
        }
        if (getItemList() != null)
        {
            for (Item item : getItemList())
            {
                builder.addItems(item.getMessageBuilder());
            }
        }

        return builder;
    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("id", UUIDUtils.toCompactString(id));
        obj.put("title", title);
        obj.put("content", content);

        obj.put("senderName", senderName);
        obj.put("type", type);
        obj.put("receiverId", receiverId);
        obj.put("receiverName", receiverName);
        obj.put("accessory", accessory);
        obj.put("read", isRead);
        obj.put("sendTime", sendTime);
        obj.put("deadLine", deadLine);

        JSONArray resArray = new JSONArray();
        for (Resource res : resourceList)
        {
            resArray.add(res.toJson());
        }
        obj.put("resourceList", resArray);

        JSONArray itemArray = new JSONArray();
        for (Item item : itemList)
        {
            itemArray.add(item.toJson());
        }
        obj.put("itemList", itemArray);

        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        Validate.notNull(json);

        if (!(json instanceof JSONObject))
            throw new IllegalArgumentException("Mail.fromJson invoke with unexpected parameter type: " + json.getClass().getSimpleName());

        JSONObject obj = (JSONObject) json;

        setId(UUIDUtils.fromCompactString(obj.getString("id")));
        setTitle(obj.getString("title"));
        setContent(obj.getString("content"));

        setSenderName(obj.getString("senderName"));
        setType(obj.getIntValue("type"));
        setReceiverId(obj.getLongValue("receiverId"));
        setReceiverName(obj.getString("receiverName"));
        setAccessory(obj.getIntValue("accessory"));
        setIsRead(obj.getIntValue("read"));
        setSendTime(obj.getIntValue("sendTime"));
        setDeadLine(obj.getIntValue("deadLine"));

        JSONArray resArr = obj.getJSONArray("resourceList");
        if (resArr != null)
        {
            for (Object res : resArr)
            {
                Resource resource = new Resource();
                resource.fromJson((JSON) res);
                resourceList.add(resource);
            }
        }

        JSONArray itemArr = obj.getJSONArray("itemList");
        if (itemArr != null)
        {
            for (Object item : itemArr)
            {
                itemList.add(BeanFactory.createItem((JSONObject) item));
            }
        }
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
