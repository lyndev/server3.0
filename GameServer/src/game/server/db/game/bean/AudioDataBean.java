package game.server.db.game.bean;

import com.google.protobuf.ByteString;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ZouZhaopeng
 */
public class AudioDataBean
{
    private String uuid;
    private String data;

    public AudioDataBean()
    {
    }

    public AudioDataBean(UUID uuid, ByteString data)
    {
        this.uuid = uuid.toString();
        try
        {
            this.data = ByteStringToString(data);
        }
        catch (UnsupportedEncodingException ex)
        {
            this.data = "";
        }
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public static String ByteStringToString(ByteString data) throws UnsupportedEncodingException
    {
        return new String(Base64.encodeBase64(data.toByteArray()), "UTF-8");
    }
    
    public static ByteString StringToByteString(String data) throws UnsupportedEncodingException
    {
        return ByteString.copyFrom(Base64.decodeBase64(data.getBytes("UTF-8")));
    }
}
