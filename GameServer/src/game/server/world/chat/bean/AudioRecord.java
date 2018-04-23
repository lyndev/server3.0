package game.server.world.chat.bean;

import com.alibaba.fastjson.JSONObject;
import game.message.ChatMessage;
import game.server.db.game.bean.ChatRecordBean;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import game.server.world.chat.ChatManager;
import game.server.world.wplayer.WPlayer;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 * 语音消息
 * 
 * @author ZouZhaopeng
 */
public final class AudioRecord extends ChatRecord
{
    private final static Logger LOG = Logger.getLogger(AudioRecord.class);
    private int length; //语音时长
    private UUID uuid;
//    private long lastUse;

    public AudioRecord()
    {
    }
    public AudioRecord(Player player, ChatMessage.AudioMessage msg, long timestamp)
    {
        super(player.getUserId(), timestamp, player.getUserName(), player.getVipManager().getVipLevel());
//        audioRecord = msg.getAudioMsg().toByteArray();
        length = msg.getLength();
        uuid = UUID.fromString(msg.getUuid());
//        lastUse = timestamp;
    }
    
    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

//    public long getLastUse()
//    {
//        return lastUse;
//    }
//
//    public void setLastUse(long lastUse)
//    {
//        this.lastUse = lastUse;
//    }
    
    /**
     * 获取对应的消息, 如果没有设置发送者userId, 返回null
     * @return 
     */
    public ChatMessage.AudioMessage.Builder getBuilder()
    {
        ChatMessage.AudioMessage.Builder builder = null;
        do
        { 
            WPlayer sender = GameWorld.getInstance().getPlayer(getSenderUserId());
            if (sender == null)
            {
                break;
            }
            builder = ChatMessage.AudioMessage.newBuilder();
            builder.setSendTime((double) getTimestamp() / 1000);
            builder.setLength(length);
            builder.setUuid(uuid.toString());
            builder.setSenderInfo(ChatManager.getSenderInfoBuilder(sender));
        } while (false);
        
        return builder;
    }

    @Override
    public String getContent()
    {
        JSONObject obj = new JSONObject();
        obj.put("uuid", uuid.toString());
        obj.put("length", length);
        return obj.toJSONString();
    }
    
    @Override
    public void initFrom(ChatRecordBean bean)
    {
        super.initFrom(bean);
        if (bean != null)
        {
            String str = bean.getContent();
            JSONObject obj = JSONObject.parseObject(str);
            if (obj != null)
            {
                length = obj.getIntValue("length");
                uuid = UUID.fromString(obj.getString("uuid"));
            }
            else
            {
                LOG.error("无法解析语音信息的length和uuid字段, 源数据为: " + bean.getContent());
            }
        }
    }
}
