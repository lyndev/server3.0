package game.server.world.chat;

import game.message.ChatMessage;
import game.server.world.chat.bean.FectionChatNote;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 所有公会的聊天记录
 * 
 * @author ZouZhaopeng
 */
public class FectionChatManager
{
    private final static Logger LOG = Logger.getLogger(FectionChatManager.class);
    public final static int FECTION_CLEAN_NUM = 80;
    
    //帮会id->聊天记录
    private final Map<Long, FectionChatNote> fectionNotes;
    
    public FectionChatManager()
    {
        fectionNotes = new HashMap<>();
    }
    
    public FectionChatNote getFectionChatNote(long fectionId)
    {
        return fectionNotes.get(fectionId);
    }
    public void addFectionChatNote(FectionChatNote note)
    {
        if (note != null)
        {
            fectionNotes.put(note.getFectionId(), note);
        }
        else
        {
            LOG.error("添加的消息为空");
        }
    }
    
    public void sendTextMessageTo(long fectionId, ChatMessage.TextMessage msg, long timestamp)
    {
        FectionChatNote note = getFectionChatNote(fectionId);
        if (note == null)
        {
            note = new FectionChatNote(fectionId);
            addFectionChatNote(note);
        }
//        ChatRecord record = new TextRecord(msg, timestamp);
//        note.addChatRecord(record);
//        //通知公会中所有玩家新消息
    }
    
    public void sendAudioMessageTo(long fectionId, ChatMessage.AudioMessage msg, long timestamp)
    {
        FectionChatNote note = getFectionChatNote(fectionId);
        if (note == null)
        {
            note = new FectionChatNote(fectionId);
            addFectionChatNote(note);
        }
//        ChatRecord record = new AudioRecord(msg, timestamp);
//        note.addChatRecord(record);
//        //通知公会所有玩家新消息
    }
    
    public void save()
    {
        for (FectionChatNote note : fectionNotes.values())
        {
            note.save();
        }
    }
}
