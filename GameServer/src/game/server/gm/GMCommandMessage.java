package game.server.gm;

import com.alibaba.fastjson.JSONObject;
import org.apache.mina.core.session.IoSession;

/**
 *
 * <b>GM指令的消息对象.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class GMCommandMessage
{

    private int command; // 指令标识

    private long serial; // 平台发送的值，回传给他们，不知道有求啥子用

    private short secret; // 加密标识（AES），平台 -> GS：1767=加密，889=不加密；GS -> 平台：1690=加密，1056=不加密

    private JSONObject data; // 指令内容

    private IoSession session; // 消息发送者的会话对象

    public int getCommand()
    {
        return command;
    }

    public void setCommand(int command)
    {
        this.command = command;
    }

    public long getSerial()
    {
        return serial;
    }

    public void setSerial(long serial)
    {
        this.serial = serial;
    }

    public short getSecret()
    {
        return secret;
    }

    public void setSecret(short secret)
    {
        this.secret = secret;
    }

    public JSONObject getData()
    {
        return data;
    }

    public void setData(JSONObject data)
    {
        this.data = data;
    }

    public IoSession getSession()
    {
        return session;
    }

    public void setSession(IoSession session)
    {
        this.session = session;
    }

}
