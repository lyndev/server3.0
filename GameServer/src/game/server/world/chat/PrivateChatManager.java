package game.server.world.chat;

import game.server.world.GameWorld;
import game.server.world.chat.bean.PrivateChatNote;
import game.server.world.wplayer.WPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 所有私聊的聊天记录
 *
 * @author ZouZhaopeng
 */
public class PrivateChatManager
{
    private final static Logger LOG = Logger.getLogger(PrivateChatManager.class);
    public final static int PRIVATE_CLEAN_NUM = 100;

    private final Map<String, PrivateChatNote> privateNotes; //会话id->聊天记录
    private final Map<Long, List<String>> index; //玩家A->玩家A的所有聊天会话id
    long lastCleanTimestamp = System.currentTimeMillis();

    public PrivateChatManager()
    {
        privateNotes = new HashMap<>();
        index = new HashMap<>();
    }

    /**
     * 添加一对玩家的聊天记录
     *
     * @param note 聊天记录
     * @return true 成功, false 失败
     */
    public boolean addPrivateChatNote(PrivateChatNote note)
    {
        if (note == null)
        {
            LOG.error("添加空的私聊记录");
            return false;
        }
        if (privateNotes.containsKey(note.getConversationId()))
        {
            LOG.error("重复添加聊天记录, 会话id = " + note.getConversationId());
            return false;
        }
        privateNotes.put(note.getConversationId(), note);
//        putIndex(note);
        return true;
    }

    public PrivateChatNote getPrivateChatNote(String conversationId)
    {
        PrivateChatNote note = privateNotes.get(conversationId);
//        if (note == null)
//        {
//            LOG.error("没有找到对应的会话, 会话conversationId = " + conversationId);
//        }
        return note;
    }

    public List<String> getIndexListOfPlayer(Long userId)
    {
        return index.get(userId);
    }

    public void putIndex(PrivateChatNote note)
    {
        if (note == null)
        {
            return;
        }
        if (!index.containsKey(note.getPlayerS()))
        {
            index.put(note.getPlayerS(), new ArrayList<String>());
        }
        if (!index.containsKey(note.getPlayerB()))
        {
            index.put(note.getPlayerB(), new ArrayList<String>());
        }
        int indexS = index.get(note.getPlayerS()).indexOf(note.getConversationId());
        if (indexS == -1){
            index.get(note.getPlayerS()).add(note.getConversationId());
        }
        
        int indexB = index.get(note.getPlayerB()).indexOf(note.getConversationId());
        if (indexB == -1){
            index.get(note.getPlayerB()).add(note.getConversationId());
        }
//        index.get(note.getPlayerS()).add(note.getConversationId());
//        index.get(note.getPlayerB()).add(note.getConversationId());
    }

    public boolean isChated(long playerS, long playerB)
    {
        List<String> list = index.get(playerS);
        return list != null && list.contains(PrivateChatNote.makeConversationId(playerS, playerB));
    }

    public void save()
    {
        for (PrivateChatNote note : privateNotes.values())
        {
            note.save();
        }
    }

    /**
     * 清理离线玩家的聊天数据, 如果双方都离线, 并且最后离线时间已经超过了1天, 清理之
     * @param currentTimeMillis
     */
    public void tick(long currentTimeMillis)
    {
//        if (currentTimeMillis - lastCleanTimestamp < 60 * 60 * 1000L)
//        { //每1小时执行一次该tick
//            return;
//        }
        Iterator<Map.Entry<Long, List<String>>> mapItr = index.entrySet().iterator();
        while (mapItr.hasNext())
        {
            Map.Entry<Long, List<String>> next = mapItr.next();

            long userId = next.getKey();
            List<String> conversationList = next.getValue();
            if (conversationList == null || conversationList.isEmpty())
            {
                continue;
            }

            WPlayer self = GameWorld.getInstance().getPlayer(userId);
            if (self == null)
            {
                continue;
            }

            if (self.getSession() == null) //玩家已离线
            {
                Iterator<String> listItr = conversationList.iterator();
                while (listItr.hasNext()) // 遍历这个玩家的私聊会话, 如果对方也离线, 清理
                {
                    //处理一个会话
                    String conversation = listItr.next();
                    PrivateChatNote pNote = getPrivateChatNote(conversation);
                    if (pNote == null)
                    {
                        continue;
                    }
                    
                    Long otherId = pNote.getOtherPlayer(userId);
                    if (otherId == null)
                    {
                        continue;
                    }
                    
                    WPlayer other = GameWorld.getInstance().getPlayer(otherId);
                    if (needClean(self, other))
                    {
                        pNote.save();
                        mapItr.remove();
                        listItr.remove();
                    }

                }
            }
        }
        lastCleanTimestamp = currentTimeMillis;

    }

    private boolean needClean(WPlayer self, WPlayer other)
    {
//        final long CLEAN_TIME = 24 * 60 * 60 * 1000L; //离线超过1天清理
        final long CLEAN_TIME = 1 * 60 * 1000L; //离线超过1天清理
        final long nowTime = System.currentTimeMillis();
        
        return self !=null && other != null && self.getSession() == null && other.getSession() == null &&
                (nowTime - self.getLastConnectTime() > CLEAN_TIME || nowTime - other.getLastConnectTime() > CLEAN_TIME);
    }
    
    /**
     * 删除规则：A把B删除时，只删除A在index里面的conversationId
     *           只有当A和B在Index里都不存在conversationId时,才删除privateNotes里，conversationId对应的记录
     * @param userId
     * @param lst 
     */
    public void removeChatRecord(long userId, long peerId){
        String converationId = PrivateChatNote.makeConversationId(userId, peerId);
        //删除自己的聊天记录引用
        List<String> lstConv = index.get(userId);
        if (lstConv == null){
            return;
        }
        
        int selfIndex = lstConv.indexOf(converationId);
        if (selfIndex == -1){
            return;
        }
        lstConv.remove(selfIndex);
        
        //若对方没有聊天记录引用，则删除converationId所对应的privateNotes记录
        List<String> peerConv = index.get(peerId);
        if (peerConv != null){
            int peerIndex = peerConv.indexOf(converationId);
            if (peerIndex != -1){
                return;
            }
        }
        privateNotes.remove(converationId);
    }

}
