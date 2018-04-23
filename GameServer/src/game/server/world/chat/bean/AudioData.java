package game.server.world.chat.bean;

import com.google.protobuf.ByteString;

/**
 *
 * @author ZouZhaopeng
 */
public class AudioData
{
    private ByteString data;
    private boolean needInsert;

    public AudioData(ByteString data)
    {
        this(data, true);
    }

    public AudioData(ByteString data, boolean needInsert)
    {
        this.data = data;
        this.needInsert = needInsert;
    }

    public ByteString getData()
    {
        return data;
    }

    public void setData(ByteString data)
    {
        this.data = data;
    }

    public boolean isNeedInsert()
    {
        return needInsert;
    }

    public void setNeedInsert(boolean needInsert)
    {
        this.needInsert = needInsert;
    }
    
}
