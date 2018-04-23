package game.server.world.chat.bean;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import game.message.ChatMessage;
import game.server.db.game.bean.ChatRecordBean;
//import game.server.logic.item.bean.HolyArms;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import game.server.world.chat.ChatManager;
import game.server.world.wplayer.WPlayer;

/**
 * 文本消息
 *
 * @author ZouZhaopeng
 */
public class TextRecord extends ChatRecord
{
    private String textRecord;
//    private final List<Link> linkList;

    public TextRecord()
    {
//        linkList = new ArrayList<>();
    }

    public TextRecord(Player player, String msg, long timestamp)
    {
        super(player.getUserId(), timestamp, player.getUserName(), player.getVipManager().getVipLevel());
        this.textRecord = msg;
//        this.linkList = linkList;
    }

    public String getTextRecord()
    {
        return textRecord;
    }

    public void setTextRecord(String textRecord)
    {
        this.textRecord = textRecord;
    }

    /**
     * 获取对应的消息, 如果没有设置发送者id, 返回null
     *
     * @return
     */
    public ChatMessage.TextMessage.Builder getBuilder()
    {
        ChatMessage.TextMessage.Builder builder = null;
        do
        {
            WPlayer sender = GameWorld.getInstance().getPlayer(getSenderUserId());
            if (sender == null)
            {
                break;
            }
            builder = ChatMessage.TextMessage.newBuilder();
            builder.setSendTime((double) getTimestamp() / 1000D);
            builder.setTextMsg(ByteString.copyFromUtf8(textRecord));
            builder.setSenderInfo(ChatManager.getSenderInfoBuilder(sender));
//            if (linkList != null)
//            {
//                for (Link link : linkList)
//                {
//                    ChatMessage.Link.Builder linkBuilder = ChatMessage.Link.newBuilder();
//                    if (link.getType() == 1) //卡牌链接
//                    {
//                        Card card = new Card();
//                        card.fromJson(JSONObject.parseObject(link.getJsonString()));
//                        ChatMessage.CardLink.Builder cardBuilder = ChatMessage.CardLink.newBuilder();
//                        cardBuilder.setId(link.getLinkId());
//                        cardBuilder.setCardInfo(card.getCardInfo());
//                        linkBuilder.setCard(cardBuilder);
//                    }
//                    else if (link.getType() == 2) //装备链接
//                    {
//                        Equipment equipment = (Equipment) BeanFactory.createItem(JSONObject.parseObject(link.getJsonString()));
//                        ChatMessage.EquipLink.Builder equipBuilder = ChatMessage.EquipLink.newBuilder();
//                        equipBuilder.setId(link.getLinkId());
//                        equipBuilder.setEquipInfo(equipment.getEquipmentBuilder());
//                        linkBuilder.setEquip(equipBuilder);
//                    }
//                    else if (link.getType() == 3) //神兵链接
//                    {
//                        HolyArms holyArms = (HolyArms)BeanFactory.createItem(JSONObject.parseObject(link.getJsonString()));
//                        ChatMessage.HolyArmsLink.Builder holyArmsBuilder = ChatMessage.HolyArmsLink.newBuilder();
//                        holyArmsBuilder.setId(link.getLinkId());
//                        holyArmsBuilder.setHolyArmsInfo(holyArms.getBuilder());
//                        linkBuilder.setHolyArms(holyArmsBuilder);
//                    }
//                    builder.addLink(linkBuilder);
//                }
//            }
        } while (false);

        return builder;
    }

    @Override
    public String getContent()
    {
        JSONObject obj = new JSONObject();
        obj.put("text", textRecord);
        do
        {
        } while (false);

        return obj.toJSONString();
    }

    @Override
    public void initFrom(ChatRecordBean bean)
    {
        super.initFrom(bean);
        do
        {
            if (bean == null)
            {
                break;
            }

            String str = bean.getContent();
            JSONObject obj = JSONObject.parseObject(str);
            if (obj == null)
            {
                textRecord = str; //没有用JSON存的老数据
                break;
            }

            textRecord = obj.getString("text");

        } while (false);
    }

}
