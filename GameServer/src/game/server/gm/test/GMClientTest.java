package game.server.gm.test;

import com.alibaba.fastjson.JSONObject;
import game.core.net.communication.CommunicationC;
import game.core.net.communication.ICommunication;
import game.core.net.server.IServer;
import game.server.gm.GMCommandMessage;
import game.server.gm.codec.GMProtocolCodecFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * @author Administrator
 */
public class GMClientTest implements IServer
{
    private IoSession session = null;

    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        System.out.println("GMCommand from sessionId = " + session.getId() + "\t" + buf.toString());
        // 读取加密标识（暂不做解密处理）
        short secret = buf.getShort();
        // 读取消息长度（无聊至极的设计）
        short dataLen = buf.getShort();
        // 读取消息内容
        byte[] msgData = new byte[buf.remaining()];
        buf.get(msgData);

        // 创建消息对象
        JSONObject json;
        try
        {
            json = JSONObject.parseObject(new String(msgData));
        }
        catch (Exception e)
        {
            throw new RuntimeException("GMCommand parsing failed! cause:", e);
        }

        GMCommandMessage message = new GMCommandMessage();
        message.setSecret(secret);
        message.setSession(session);
        message.setSerial(json.getLong("serial"));
        message.setCommand(json.getIntValue("command"));
        message.setData(json.getJSONObject("data"));

        System.out.println("客户端收到服务端发送的消息>>");
        System.out.println("command:" + message.getCommand());
        System.out.println("serial:" + message.getSerial());
        System.out.println("secret:" + message.getSecret());
        System.out.println("data:" + message.getData().toJSONString());
    }

    @Override
    public void sessionCreate(IoSession session)
    {
        this.session = session;
        System.out.println("sessionId = " + session.getId());
    }

    @Override
    public void sessionClosed(IoSession session)
    {
        System.out.println("sessionId = " + session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
    }

    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
        return new GMProtocolCodecFactory(null, null, null);
    }

    public void start(int port)
    {
        ICommunication connector = new CommunicationC(this);
        ((CommunicationC) connector).initialize();
        connector.connect("127.0.0.1", port);

        JSONObject json = new JSONObject();
        json.put("command", 1001);
        json.put("serial", 123456);
        JSONObject dataJ = new JSONObject();
        dataJ.put("playerId", 1000);
        dataJ.put("gold", 500);
        json.put("data", dataJ);

        byte[] data = json.toJSONString().getBytes();
        // 4字节包长 + 2字节加密标识 + 2字节json长度 + json字符串
        IoBuffer buffer = IoBuffer.allocate(data.length + 8);
        buffer.putInt(data.length + 8);
        buffer.putShort((short) 889);
        buffer.putShort((short) data.length);
        buffer.put(data);
        buffer.flip();

        connector.send(session, buffer);
    }

    public static void main(String[] args) throws Exception
    {
        new GMClientTest().start(10000);
    }

}
