package game.server.world.chat;

import com.google.protobuf.ByteString;
import game.server.db.game.bean.AudioDataBean;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ReqInsertAudioDataBatchHandler;
//import game.server.thread.dboperator.handler.ReqInsertAudioDataHandler;
import game.server.world.chat.bean.AudioData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 * 语音数据管理器
 *
 * @author ZouZhaopeng
 */
public class AudioDataManager
{
    private final static Logger LOG = Logger.getLogger(AudioDataManager.class);
    public final static int AUDIO_DATA_CLEAN_NUM = 200;

    private final Map<UUID, AudioData> audioData; //UUID->语音消息数据
    private final LinkedList<UUID> index; //
    private int insertNum; //需要插入数据库的条数

    public AudioDataManager()
    {
        audioData = new HashMap<>();
        index = new LinkedList<>();
        insertNum = 0;
    }

    /**
     * 用UUID获取指定的语音信息数据
     *
     * @param uuid 语音信息UUID
     * @return 语音信息数据
     */
    public ByteString getAudioRecord(UUID uuid)
    {
        AudioData data = audioData.get(uuid);
        return data == null ? null : data.getData();
    }

    /**
     * 添加指定语音信息
     *
     * @param uuid 语音信息UUID
     * @param data 语音信息数据
     * @param needInsertDB 是否同时插入数据库
     */
    public void putAudioRecord(UUID uuid, ByteString data, boolean needInsertDB)
    {
        audioData.put(uuid, new AudioData(data, needInsertDB));
        index.addLast(uuid);
        if (needInsertDB)
        {
            ++insertNum;
            checkInsert();
        }
        int count = index.size();
        while (count > AUDIO_DATA_CLEAN_NUM)
        {
            audioData.remove(index.getFirst());
            index.removeFirst();
            --count;
        }
    }
    
    /**
     * 检查是否该插入数据库了
     */
    public void checkInsert()
    {
        if (insertNum > AUDIO_DATA_CLEAN_NUM / 2)
        {
            save();
        }
    }
    
    /**
     * 把内存中还没有存的数据回存到数据库
     */
    public void save()
    {
        List<AudioDataBean> list = new ArrayList<>();
        for (Map.Entry<UUID, AudioData> entry : audioData.entrySet())
        {
            UUID key = entry.getKey();
            AudioData value = entry.getValue();
            if (value.isNeedInsert())
            {
                list.add(new AudioDataBean(key, value.getData()));
                value.setNeedInsert(false);
            }
        }
        if (!list.isEmpty())
        {
            insertNum -= list.size();
            insertBatch(list);
        }
    }

//    /**
//     * 插入语音信息到数据库(两个数据库)
//     *
//     * @param uuid 语音消息UUID
//     * @param data 语音数据
//     */
//    public void insert(UUID uuid, ByteString data)
//    {
//        if (uuid != null && data != null)
//        {
//            AudioDataBean bean = new AudioDataBean(uuid, data);
//            GameDBOperator.getInstance().submitRequest(new ReqInsertAudioDataHandler(bean));
//        }
//    }

    public void insertBatch(List<AudioDataBean> list)
    {
        if (list != null && !list.isEmpty())
        {
            GameDBOperator.getInstance().submitRequest(new ReqInsertAudioDataBatchHandler(list));
        }
    }
}
