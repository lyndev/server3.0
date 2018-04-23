package game.server.world.chat;

import com.google.protobuf.ByteString;
import game.core.message.SMessage;
import game.data.bean.q_globalBean;
import game.message.ChatMessage;
import game.server.db.game.bean.AudioDataBean;
import game.server.logic.constant.ErrorCode;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanTemplet;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ReqSelectAudioDataHandler;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.SensitiveWordFilter;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.chat.bean.AudioRecord;
import game.server.world.chat.bean.ChatNote;
import game.server.world.chat.bean.ChatRecord;
import game.server.world.chat.bean.PrivateChatNote;
import game.server.world.chat.bean.TextRecord;
import game.server.world.wplayer.WPlayer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class ChatManager
{
    private final static Logger LOG = Logger.getLogger(ChatManager.class);
    private final static int NO_CONFIG_DEFAULT_VALUE = 0;
    
    private final WorldChatManager worldChatManager; //世界聊天 
    private final FectionChatManager fectionChatManager; //公会聊天
    private final PrivateChatManager privateChatManager; //私聊
    private final AudioDataManager audioDataManager; //语音数据

    public ChatManager()
    {
        worldChatManager = new WorldChatManager();
        fectionChatManager = new FectionChatManager();
        privateChatManager = new PrivateChatManager();
        audioDataManager = new AudioDataManager();
    }

    /**
     * 语音消息的响应方法
     *
     * @param player
     * @param msg 语音消息
     */
    public void onSendAudioMessage(Player player, ChatMessage.ReqSendAudioMessage msg)
    {
        ErrorCode errorCode = null;
        do
        {
            if (player.getDenyChatTimestamp() > MiscUtils.getTimestamp())
            {
                errorCode = ErrorCode.CHAT_FORBIDDEN;
                break;
            }

            if (msg == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] msg == null");
                errorCode = ErrorCode.NULL_MESSAGE;
                break;
            }
            if (!isChatModuleOpen(player.getUserId()))
            {
                LOG.error("玩家[" + player.getUserName() + "]聊天模块未开启");
                errorCode = ErrorCode.CHAT_NOT_OPEN;
                break;
            }

            String other = null;
            if (msg.hasUserId())
            {
                other = msg.getUserId();
            }
            ChatMessage.AudioMessage audioMessage = msg.getAudio();
            if (audioMessage == null)
            {
                LOG.error("玩家[" + player.getUserName() + "]语音消息为空");
                errorCode = ErrorCode.NULL_AUDIO_MESSAGE;
                break;
            }
            if (!audioMessage.hasAudioMsg())
            {
                LOG.error("玩家[" + player.getUserName() + "]语音消息数据为空");
                errorCode = ErrorCode.AUDIO_MESSAGE_NO_DATA;
                break;
            }
            UUID audioUuid = UUID.fromString(audioMessage.getUuid());
            if (audioUuid == null || audioDataManager.getAudioRecord(audioUuid) != null)
            {
                LOG.error("玩家[" + player.getUserName() + "]重复发送语音信息");
                //可能断线重连, 然后重发, 不做处理, 客户端不应该感受到这种细节
                break;
            }
            errorCode = switchAudioMessage(player, msg.getChannel(), msg.getAudio(), other);

        } while (false);

        if (errorCode != null)
        {
            sendErrorMessage(player, errorCode);
        }
    }

    private ErrorCode switchAudioMessage(Player player, int channel, ChatMessage.AudioMessage msg, String other)
    {
        ErrorCode errorCode;
        switch (channel)
        {
            case 1:
                errorCode = sendAudioMessageToWorld(player, msg);
                break;
//            case 2:
//                errorCode = sendAudioMessageToFection(player, msg);
//                break;
            case 3:
                errorCode = sendAudioMessageToPlayer(player, msg, other);
                break;
            default:
                LOG.error("玩家[" + player.getUserName() + "] 发送消息到未知的频道: " + channel);
                errorCode = ErrorCode.UNKOWN_CHANNEL;
                break;
        }
        return errorCode;
    }

    private ErrorCode sendAudioMessageToWorld(Player player, ChatMessage.AudioMessage msg)
    {
        ErrorCode errorCode = null;
        do
        {
            if (!isWorldChatOpen(player.getUserId()))
            {
                LOG.error("玩家[" + player.getUserName() + "]世界聊天未开启");
                errorCode = ErrorCode.WORLD_CHAT_NOT_OPEN;
                break;
            }

            long nowTime = System.currentTimeMillis();
            if (nowTime - player.getLastWorldChatTime() < getWorldChatCDInMillis())
            {
                errorCode = ErrorCode.WORLD_CHAT_CD;
                break;
            }

            AudioRecord audioRecord = new AudioRecord(player, msg, nowTime);
            worldChatManager.addChatRecord(audioRecord);
            audioDataManager.putAudioRecord(audioRecord.getUuid(), msg.getAudioMsg(), true);
            player.setLastWorldChatTime(nowTime);

            ChatMessage.ResSendAudioMessage.Builder builder = ChatMessage.ResSendAudioMessage.newBuilder();
            builder.setSendTime(msg.getSendTime());
            builder.setTimestamp(nowTime / 1000D);
            MessageUtils.send(player, new SMessage(ChatMessage.ResSendAudioMessage.MsgID.eMsgID_VALUE,
                    builder.build().toByteArray()));

            notifyWorld(audioRecord);

        } while (false);

        return errorCode;
    }

    private void notifyWorld(ChatRecord record)
    {
        do
        {
            WPlayer wPlayer = getWPlayer(record.getSenderUserId());
            if (wPlayer == null)
            {
                LOG.error("没有找到玩家, userId = " + UniqueId.toBase36(record.getSenderUserId()));
                break;
            }

            int msgId;
            byte[] msg;
            if (record instanceof TextRecord)
            {
                ChatMessage.NotifyTextMessage.Builder builder = ChatMessage.NotifyTextMessage.newBuilder();
                builder.setChannel(1);
                builder.setTextMessage(((TextRecord) record).getBuilder());
                msgId = ChatMessage.NotifyTextMessage.MsgID.eMsgID_VALUE;
                msg = builder.build().toByteArray();
            }
            else if (record instanceof AudioRecord)
            {
                ChatMessage.NotifyAudioMessage.Builder builder = ChatMessage.NotifyAudioMessage.newBuilder();
                builder.setChannel(1);
                builder.setAudioMessage(((AudioRecord) record).getBuilder());
                msgId = ChatMessage.NotifyAudioMessage.MsgID.eMsgID_VALUE;
                msg = builder.build().toByteArray();
            }
            else
            {
                LOG.error("未知类型的聊天消息: " + record.getClass().getSimpleName());
                break;
            }

            for (WPlayer other : GameWorld.getInstance().getUserPlayers().values())
            {
                if (!other.isRobot() && other != wPlayer)
                {
                    MessageUtils.send(other.getSession(), new SMessage(msgId, msg));
                }
            }

        } while (false);

    }
    
    private ErrorCode sendAudioMessageToPlayer(Player player, ChatMessage.AudioMessage msg, String other)
    {
        ErrorCode errorCode = null;
        WPlayer otherPlayer;
        do
        {
            if (!isPrivateChatOpen(player.getUserId()))
            {
                LOG.error("玩家[" + player.getUserName() + "]私聊未开启");
                errorCode = ErrorCode.PRIVATE_CHAT_NOT_OPEN;
                break;
            }
            if (other == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 目标玩家没有找到, 玩家userId为空");
                errorCode = ErrorCode.NO_TARGET_WPLAYER;
                break;
            }
            otherPlayer = getWPlayer(UniqueId.toBase10(other));
            if (otherPlayer == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 目标玩家没有找到, 玩家userId = " + other);
                errorCode = ErrorCode.NO_TARGET_WPLAYER;
                break;
            }
            if (player.getUserId() == otherPlayer.getUserId())
            {
                LOG.error("玩家[" + player.getUserName() + "]不能和自己聊天");
                errorCode = ErrorCode.CHAT_ONESELF;
                break;
            }

            PrivateChatNote note = privateChatManager.getPrivateChatNote(
                    PrivateChatNote.makeConversationId(player.getUserId(), otherPlayer.getUserId()));
            if (note == null)
            {
                note = new PrivateChatNote(player.getUserId(), otherPlayer.getUserId());
                if (!privateChatManager.addPrivateChatNote(note))
                {
                    LOG.error("玩家[" + player.getUserName() + "] 添加聊天会话失败, 会话id为: " + note == null ? "空的会话" : note.getConversationId());
                    errorCode = ErrorCode.NO_CHAT_CONVERSATION;
                    break;
                }
            }

            long nowTime = System.currentTimeMillis();
            AudioRecord record = new AudioRecord(player, msg, nowTime);
            note.addChatRecord(record);
            audioDataManager.putAudioRecord(record.getUuid(), msg.getAudioMsg(), true);

            ChatMessage.ResSendAudioMessage.Builder builder = ChatMessage.ResSendAudioMessage.newBuilder();
            builder.setSendTime(msg.getSendTime());
            builder.setTimestamp(nowTime / 1000D);
            MessageUtils.send(player, new SMessage(ChatMessage.ResSendAudioMessage.MsgID.eMsgID_VALUE, builder.build().toByteArray()));

            //通知另一个玩家
            ChatMessage.NotifyAudioMessage.Builder notifyBuilder = ChatMessage.NotifyAudioMessage.newBuilder();
            notifyBuilder.setAudioMessage(record.getBuilder());
            notifyBuilder.setChannel(3);
            MessageUtils.send(otherPlayer.getSession(), new SMessage(ChatMessage.NotifyAudioMessage.MsgID.eMsgID_VALUE, notifyBuilder.build().toByteArray()));

        } while (false);

        return errorCode;
    }

    public static ChatMessage.SenderInfo.Builder getSenderInfoBuilder(WPlayer wPlayer)
    {
        ChatMessage.SenderInfo.Builder builder = ChatMessage.SenderInfo.newBuilder();
        builder.setIcon(wPlayer.getRoleHead());
        builder.setLevel(wPlayer.getRoleLevel());
        builder.setRoleName(wPlayer.getRoleName());
        builder.setUserId(UniqueId.toBase36(wPlayer.getUserId()));
//        builder.setVip(wPlayer.getVipLevel());
//        do
//        {
//            FactionInfo factionInfo = FactionService.getInstance().getFactionOfPlayer(wPlayer);
//            if (factionInfo == null)
//            {
////                LOG.info("玩家[" + wPlayer.getUserName() + "] 没有加入公会");
//                break;
//            }
//            ChatMessage.FectionInfo.Builder fectionBuilder = ChatMessage.FectionInfo.newBuilder();
//            fectionBuilder.setIcon(factionInfo.getIcon());
//            fectionBuilder.setName(factionInfo.getName());
//            fectionBuilder.setId(UniqueId.toBase36(factionInfo.getId()));
//            builder.setFection(fectionBuilder);
//        } while (false);

//        do
//        { //竞技场数据
//            ArenaPlayer arenaPlayer = ArenaMatching.getInstance().getPlayer(wPlayer.getUserId());
//            if (arenaPlayer == null)
//            {
////                LOG.info("玩家[" + wPlayer.getUserName() + "] 玩家没有竞技场数据");
//                break;
//            }
//            ChatMessage.ArenaInfo.Builder arenaBuilder = ChatMessage.ArenaInfo.newBuilder();
//            List<ArenaCard> arenaCards = arenaPlayer.getCards();
//            if (arenaCards == null)
//            {
//                LOG.info("玩家[" + wPlayer.getUserName() + "]玩家没有竞技场防守阵容");
//                break;
//            }
//            for (ArenaCard card : arenaCards)
//            {
//                arenaBuilder.addCards(getArenaSummary(card));
//            }
//            builder.setArenaInfo(arenaBuilder);
//        } while (false);

        return builder;
    }

    public void onSendTextMessage(Player player, ChatMessage.ReqSendTextMessage msg)
    {
        ErrorCode errorCode;
        do
        {
            if (player.getDenyChatTimestamp() > MiscUtils.getTimestamp())
            {
                errorCode = ErrorCode.CHAT_FORBIDDEN;
                break;
            }
            long userId = player.getUserId();
            if (msg == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] msg == null");
                errorCode = ErrorCode.NULL_MESSAGE;
                break;
            }
            if (!isChatModuleOpen(userId))
            {
                LOG.error("玩家[" + player.getUserName() + "] 聊天模块未开启");
                errorCode = ErrorCode.CHAT_NOT_OPEN;
                break;
            }
            if (msg.getText() == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 消息内容为空");
                errorCode = ErrorCode.NULL_MESSAGE;
                break;
            }

            String other = null;
            if (msg.hasUserId())
            {
                other = msg.getUserId();
            }
            errorCode = switchTextMessage(player, msg.getChannel(), msg.getText(), other);

        } while (false);

        if (errorCode != null)
        {
            sendErrorMessage(player, errorCode);
        }
    }

    private ErrorCode switchTextMessage(Player player, int channel, ChatMessage.TextMessage msg, String userId)
    {
        ErrorCode errorCode;
        switch (channel)
        {
            case 1:
                errorCode = sendTextMessageToWorld(player, msg);
                break;
//            case 2:
//                errorCode = sendTextMessageToFection(player, msg);
//                break;
            case 3:
                errorCode = sendTextMessageToPlayer(player, msg, userId);
                break;
            default:
                LOG.error("玩家[" + player.getUserName() + "] 发送消息到未知的频道: " + channel);
                errorCode = ErrorCode.UNKOWN_CHANNEL;
                break;
        }
        return errorCode;
    }

    private ErrorCode sendTextMessageToWorld(Player player, ChatMessage.TextMessage msg)
    {
        ErrorCode errorCode = null;
        do
        {
            if (!isWorldChatOpen(player.getUserId()))
            {
                LOG.error("玩家[" + player.getUserName() + "] 世界聊天未开启");
                errorCode = ErrorCode.WORLD_CHAT_NOT_OPEN;
                break;
            }

            long nowTime = System.currentTimeMillis();
            if (nowTime - player.getLastWorldChatTime() < getWorldChatCDInMillis())
            {
                errorCode = ErrorCode.WORLD_CHAT_CD;
                break;
            }

            String filtedMsg = filtSensitiveWord(msg.getTextMsg().toStringUtf8());
            TextRecord textRecord = new TextRecord(player, filtedMsg, nowTime);
            worldChatManager.addChatRecord(textRecord);
            player.setLastWorldChatTime(nowTime);

            ChatMessage.ResSendTextMessage.Builder builder = ChatMessage.ResSendTextMessage.newBuilder();
            builder.setSendTime(msg.getSendTime());
            builder.setTimestamp(nowTime / 1000D);
            builder.setTextMsg(ByteString.copyFromUtf8(filtedMsg));
            MessageUtils.send(player, new SMessage(ChatMessage.ResSendTextMessage.MsgID.eMsgID_VALUE,
                    builder.build().toByteArray()));

            notifyWorld(textRecord);

        } while (false);

        return errorCode;
    }

    private ErrorCode sendTextMessageToPlayer(Player player, ChatMessage.TextMessage msg, String userId)
    {
        ErrorCode errorCode = null;
        WPlayer otherPlayer;
        do
        {
            if (!isPrivateChatOpen(player.getUserId()))
            {
                LOG.error("玩家[" + player.getUserName() + "] 私聊未开启");
                errorCode = ErrorCode.PRIVATE_CHAT_NOT_OPEN;
                break;
            }
            WPlayer wPlayer = getWPlayer(player.getUserId());
            if (wPlayer == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 找不到WPlayer");
                errorCode = ErrorCode.WPLAYER_NOT_FOUND; //这个错误码没法发送给客户端, 因为找不到发送的对象
                break;
            }

            otherPlayer = getWPlayer(UniqueId.toBase10(userId));
            if (otherPlayer == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 没有找到目标玩家, 玩家userId = " + userId);
                errorCode = ErrorCode.NO_TARGET_WPLAYER;
                break;
            }
            if (wPlayer == otherPlayer)
            {
                LOG.error("玩家[" + player.getUserName() + "] 不能和自己聊天");
                errorCode = ErrorCode.CHAT_ONESELF;
                break;
            }

            PrivateChatNote note = privateChatManager.getPrivateChatNote(
                    PrivateChatNote.makeConversationId(wPlayer.getUserId(), otherPlayer.getUserId()));
            if (note == null)
            {
                note = new PrivateChatNote(wPlayer.getUserId(), otherPlayer.getUserId());
                if (!privateChatManager.addPrivateChatNote(note))
                { //note == null 会进入这里break掉
                    LOG.error("玩家[" + player.getUserName() + "] 添加聊天会话失败, 会话id为: " + note == null ? "空的会话" : note.getConversationId());
                    errorCode = ErrorCode.NO_CHAT_CONVERSATION;
                    break;
                }
            }//到这里note必不为空
            
            //检查更新Index中的conversationId
            privateChatManager.putIndex(note);
            
            long nowTime = System.currentTimeMillis();
            String filtedMsg = filtSensitiveWord(msg.getTextMsg().toStringUtf8());
            TextRecord record = new TextRecord(player, filtedMsg, nowTime);
            note.addChatRecord(record);
            ChatMessage.ResSendTextMessage.Builder builder = ChatMessage.ResSendTextMessage.newBuilder();
            builder.setSendTime(msg.getSendTime());
            builder.setTimestamp(nowTime / 1000D);
            builder.setTextMsg(ByteString.copyFromUtf8(filtedMsg));
            MessageUtils.send(player, new SMessage(ChatMessage.ResSendTextMessage.MsgID.eMsgID_VALUE, builder.build().toByteArray()));

            //通知另一个玩家
            ChatMessage.NotifyTextMessage.Builder notifyBuilder = ChatMessage.NotifyTextMessage.newBuilder();
            notifyBuilder.setTextMessage(record.getBuilder());
            notifyBuilder.setChannel(3);
            MessageUtils.send(otherPlayer.getSession(), new SMessage(ChatMessage.NotifyTextMessage.MsgID.eMsgID_VALUE, notifyBuilder.build().toByteArray()));

            //如果对方不是好友，则把它加为临时好友
            GameWorld.getInstance().getFriendManager().addFriend(player.getSession(), player.getUserId(), otherPlayer.getUserId(), 0, true);
        } while (false);

        return errorCode;
    }

    private String filtSensitiveWord(String str)
    {
        if (SensitiveWordFilter.getInstance().hasSensitiveWord(str))
            return SensitiveWordFilter.getInstance().filterSensitiveWord(str);
        else
            return str;
    }

    public void onFetchAudioMessage(Player player, ChatMessage.ReqFetchAudioMessage msg)
    {
        ErrorCode errorCode = null;

        do
        {
            if (player.getDenyChatTimestamp() > MiscUtils.getTimestamp())
            {
                errorCode = ErrorCode.CHAT_FORBIDDEN;
                break;
            }
            UUID uuid = UUID.fromString(msg.getUuid());
            if (uuid == null)
            {
                errorCode = ErrorCode.AUDIO_MSG_NOT_FOUND;
                break;
            }
            ByteString data = audioDataManager.getAudioRecord(uuid);
            if (data == null)
            { //异步到数据库去取
                GameDBOperator.getInstance().submitRequest(new ReqSelectAudioDataHandler(player, uuid));
                break;
            }
            else
            {
                sendResFetchAudioMessage(player, msg.getUuid(), data);
            }

        } while (false);

        if (errorCode != null)
        {
            sendErrorMessage(player, errorCode);
        }
    }

    public void afterFetchFromDB(Player player, AudioDataBean bean)
    {
        ErrorCode errorCode = null;
        do
        {
            if (bean == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 数据库中没有找到对应的语音消息记录");
                errorCode = ErrorCode.AUDIO_MSG_NOT_FOUND;
                break;
            }
            String data = bean.getData();
            if (data == null)
            {
                LOG.error("玩家[" + player.getUserName() + "] 语音消息没有数据");
                errorCode = ErrorCode.AUDIO_MESSAGE_NO_DATA;
                break;
            }

            ByteString byteStringData;
            try
            { //各种解码
                byteStringData = AudioDataBean.StringToByteString(data);
            }
            catch (Exception e)
            {
                LOG.error("玩家[" + player.getUserName() + "] 数据库数据无法转换为语音数据");
                sendErrorMessage(player, errorCode);
                break;
            }
            sendResFetchAudioMessage(player, bean.getUuid(), byteStringData);
            audioDataManager.putAudioRecord(UUID.fromString(bean.getUuid()), byteStringData, false); //false不用插入数据库

        } while (false);

        if (errorCode != null)
        {
            sendErrorMessage(player, errorCode);
        }
    }

    private void sendResFetchAudioMessage(Player player, String uuid, ByteString data)
    {
        ChatMessage.ResFetchAudioMessage.Builder builder = ChatMessage.ResFetchAudioMessage.newBuilder();
        builder.setUuid(uuid);
        builder.setData(data);
        MessageUtils.send(player, new SMessage(ChatMessage.ResFetchAudioMessage.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    public void clientInitializeOver(Player player)
    {
        WPlayer wPlayer = getWPlayer(player.getUserId());
        if (wPlayer == null)
        {
            LOG.error("玩家[" + player.getUserName() + "] 没有在world找到玩家");
            return;
        }

        //世界聊天内容
        ChatMessage.ResInitChatRecordMessag.Builder builder = ChatMessage.ResInitChatRecordMessag.newBuilder();
        ChatMessage.ChannelInfoData.Builder worldBuilder = getChannelInfoBuilder(1, worldChatManager.getWorldNote());
        if (worldBuilder != null)
        {
            builder.addInfoData(worldBuilder);
        }

//        ChatMessage.ChannelInfoData.Builder fectionBuilder = null;
//        Long fectionId = wPlayer.getFactionId();
//        if (fectionId != null)
//        {
//            FectionChatNote fNote = fectionChatManager.getFectionChatNote(fectionId);
//            if (fNote != null)
//            {
//                fectionBuilder = getChannelInfoBuilder(2, fNote);
//            }
//        }
//        if (fectionBuilder != null)
//        {
//            builder.addInfoData(fectionBuilder);
//        }
        //私聊内容
        List<String> conversationList = privateChatManager.getIndexListOfPlayer(player.getUserId());
        if (conversationList != null)
        {
            for (String conversation : conversationList)
            {
                PrivateChatNote note = privateChatManager.getPrivateChatNote(conversation);
                ChatMessage.ChannelInfoData.Builder privateBuilder = getChannelInfoBuilder(3, note);
                if (privateBuilder != null)
                {
                    builder.addInfoData(privateBuilder);
                }
            }
        }
        MessageUtils.send(wPlayer.getSession(), new SMessage(ChatMessage.ResInitChatRecordMessag.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    /**
     * 获取某个频道的最近消息
     *
     * @param channel 1世界, 2公会, 3私聊
     * @param note 聊天记录
     * @return 
     */
    private ChatMessage.ChannelInfoData.Builder getChannelInfoBuilder(int channel, ChatNote note)
    {
        if (note == null)
        {
            return null;
        }

        LinkedList<ChatRecord> records = note.getRecords();
        if (records == null || records.isEmpty())
        {
            return null;
        }

        int count = 0;
        ChatMessage.ChannelInfoData.Builder builder = ChatMessage.ChannelInfoData.newBuilder();
        builder.setChannel(channel);
        Iterator<ChatRecord> itr = records.descendingIterator();
        while (itr.hasNext())
        {
            ChatRecord next = itr.next();
            if (channel == 1 && next.getTimestamp() + getTimeLimitInMillis() < System.currentTimeMillis())
            { //世界聊天如果超过了配置时间, 比如3分钟, 不再发送
                break;
            }

            ChatMessage.ChatInfoData.Builder recordBuilder = ChatMessage.ChatInfoData.newBuilder();
            if (note instanceof PrivateChatNote)
            { //私聊, 填充接收者roleName
                PrivateChatNote pNote = (PrivateChatNote)note;
                long receiverId = pNote.getOtherPlayer(next.getSenderUserId());
                recordBuilder.setReceiverName(getRoleNameByUserId(receiverId));
                recordBuilder.setReceiverUserId(UniqueId.toBase36(receiverId));
            }
            
            if (next instanceof TextRecord)
            {
                TextRecord text = (TextRecord) next;
                recordBuilder.setTextMsg(text.getBuilder());
                builder.addInfoList(recordBuilder);
                ++count;
            }
            else if (next instanceof AudioRecord)
            {
                AudioRecord audio = (AudioRecord) next;
                recordBuilder.setAudioMessage(audio.getBuilder());
                builder.addInfoList(recordBuilder);
                ++count;
            }
            else
            {
                LOG.error("未知的消息类型: " + next.getClass().getSimpleName());
            }

            if (count >= getDisplayLimit())
            { // 如果超过了指定条数, 比如50条, 不再发送
                break;
            }
        }

        if (count <= 0)
        {// 即使records列表不为空, 也可能因为超过时间限制, 没有消息添加到builder中
            return null;
        }
        else
        {
            return builder;
        }
    }

    private String getRoleNameByUserId(long userId)
    {
        WPlayer wPlayer = getWPlayer(userId);
        if (wPlayer == null)
            return "";
        else
            return wPlayer.getRoleName();
    }

    public void doNothing()
    {
    }

    public WorldChatManager getWorldChatManager()
    {
        return worldChatManager;
    }

    public FectionChatManager getFectionChatManager()
    {
        return fectionChatManager;
    }

    public PrivateChatManager getPrivateChatManager()
    {
        return privateChatManager;
    }

    public AudioDataManager getAudioDataManager()
    {
        return audioDataManager;
    }

    public boolean isChatModuleOpen(long userId)
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2064);
        if (globalBean == null)
        {
            LOG.warn("聊天模块功能开放表没有配置, 默认开启, 功能开放id = 2064");
            return true;
        }
        return checkOpen(userId, globalBean.getQ_int_value());
    }

    public boolean isWorldChatOpen(long userId)
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2061);
        if (globalBean == null){
            LOG.error("全局配置表，不存在2061");
            return true;
        }
        
        int openLevle = globalBean.getQ_int_value();
        return checkOpen(userId, openLevle);
    }

    public boolean isFectionChatOpen(long userId)
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2062);
        if (globalBean == null){
            LOG.error("全局配置表，不存在2062");
            return  true;
        }
        
        int openLevle = globalBean.getQ_int_value();
        return checkOpen(userId, openLevle);
    }

    public boolean isPrivateChatOpen(long userId)
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2060);
        if (globalBean == null){
            LOG.error("全局配置表，不存在2060");
            return  true;
        }
        
        int openLevle = globalBean.getQ_int_value();
        return checkOpen(userId, openLevle);
    }

    public long getWorldChatCDInMillis()
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2063);
        if (globalBean == null){
            LOG.error("全局配置表，不存在2063");
            return  30 * 1000;
        }
        
        int nMillSecond = globalBean.getQ_int_value();
        return nMillSecond;
    }

    /**
     * 需要显示的消息条数
     *
     * @return
     */
    public static int getDisplayLimit()
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2065);
        if (globalBean == null){
            LOG.error("全局配置表，不存在2065");
            return 1;
        }
        
        int nMsgCnt = globalBean.getQ_int_value();
        return nMsgCnt;
    }

    /**
     * 获取多少分钟前的消息(用毫秒表示)
     *
     * @return
     */
    public long getTimeLimitInMillis()
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(2066);
        if (globalBean == null){
            LOG.error("全局配置表，不存在2066");
            return 60 * 1000;
        }
        
        int nSecond = globalBean.getQ_int_value();
        return nSecond * 1000;
        
//        return  MiscUtils.getGlobalIntValue(1216, 3) * 60000L; //毫秒数 = 分钟数 * 60,000 millis/min
    }

    private static WPlayer getWPlayer(long userId)
    {
        WPlayer wPlayer = GameWorld.getInstance().getPlayer(userId);
        if (wPlayer == null)
        {
            LOG.error("GameWorld 中找不到指定userId的玩家: [" + userId + "]");
        }
        return wPlayer;
    }

    private boolean checkOpen(long userId, int openLevel)
    {
        WPlayer wPlayer = getWPlayer(userId);
        return wPlayer == null ? false : wPlayer.getRoleLevel() >= openLevel;
    }

    private void sendErrorMessage(Player player, ErrorCode errorCode)
    {
        ChatMessage.ResError.Builder builder = ChatMessage.ResError.newBuilder();
        builder.setErrorCode(errorCode.getCode());
        MessageUtils.send(player, new SMessage(ChatMessage.ResError.MsgID.eMsgID_VALUE,
                builder.build().toByteArray()));
    }

    public void save()
    {
        worldChatManager.save();
        fectionChatManager.save();
        privateChatManager.save();
        audioDataManager.save();
    }
}
