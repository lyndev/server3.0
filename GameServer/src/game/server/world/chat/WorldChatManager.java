package game.server.world.chat;

import game.server.world.chat.bean.AudioRecord;
import game.server.world.chat.bean.TextRecord;
import game.server.world.chat.bean.WorldChatNote;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class WorldChatManager
{
    private final static Logger LOG = Logger.getLogger(WorldChatManager.class);
    public final static int WORLD_CLEAN_NUM = 100;
    
    private final WorldChatNote worldNotes;
    public WorldChatManager()
    {
        worldNotes = new WorldChatNote();
    }
    
    public void addChatRecord(AudioRecord chatRecord)
    {
        if (chatRecord != null)
        {
            worldNotes.addChatRecord(chatRecord);
        }
        else
        {
            LOG.error("chatRecord == null");
        }
    }
    
    public WorldChatNote getWorldNote()
    {
        return worldNotes;
    }
    
    public void addChatRecord(TextRecord chatRecord)
    {
        if (chatRecord != null)
        {
            worldNotes.addChatRecord(chatRecord);
        }
        else
        {
            LOG.error("chatRecord == null");
        }
        //根据一定条件更新数据库, 删除过时的消息
    }
    
    public void save()
    {
        worldNotes.save();
    }
    
}
