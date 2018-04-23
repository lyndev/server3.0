package game.core.net;

import game.core.message.Dictionary;
import game.core.message.RMessage;
import game.core.message.DictionaryManager;
import game.core.message.Player;
import game.core.net.codec.ServerProtocolCodecFactory;
import game.core.net.communication.CommunicationC;
import game.core.net.communication.ICommunication;
import game.core.net.server.IServer;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 * Protobuf的客户端测试程序
 *
 * @author wangJingWei
 */
class TClient2 implements IServer
{
    private IoSession session = null;

    private final Logger log = Logger.getLogger(TClient2.class);

    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        try
        {
            System.out.println("msg from sessionId = " + session.getId() + "\t" + buf.toString());
            // 读取消息ID
            int msgId = buf.getInt();
            System.out.println("msg id: " + msgId);
            // 获取消息字典
            Dictionary dic = CTest2.dictionaries.get(msgId);
            if (dic != null)
            {
                // 读取消息内容
                byte[] msgData = new byte[buf.remaining()];
                buf.get(msgData);
                // 创建消息对象
                RMessage message = new RMessage();
                message.setId(msgId);
                message.setSession(session);
                message.setData(dic.getMessage(), msgData);
                System.out.println("客户端收到服务端发送的消息：\n" + message.getData());
            }
        }
        catch (Exception e)
        {
            log.error("error", e);
        }
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
//        return new InnerProtocolCodecFactory();
        return new ServerProtocolCodecFactory(null, null, null);
    }

    public void start(int port)
    {
        ICommunication connector = new CommunicationC(this);
        connector.connect("127.0.0.1", 8989);

        Player.Person.Builder builder = Player.Person.newBuilder();
        builder.setId(1);
        builder.setName("奔跑的猪");
        builder.setEmail("wjvonline@163.com");

        Player.Person person = builder.build();
        byte[] bytes = person.toByteArray();

        IoBuffer buffer = IoBuffer.allocate(bytes.length + Integer.SIZE / Byte.SIZE * 2);
        buffer.putInt(bytes.length + Integer.SIZE / Byte.SIZE);
        buffer.putInt(Player.Person.MsgID.eMsgID_VALUE);
        buffer.put(bytes);
        buffer.flip();

        connector.send(session, buffer);
    }

}

public class CTest2
{
    public static DictionaryManager dictionaries = new DictionaryManager();

    public static void main(String[] args) throws Exception
    {
        // 测试注册消息类型和对应的处理函数
//        dictionaries.register(
//                Player.Person.MsgID.eMsgID_VALUE,
//                 Player.Person.class, PlayerHandler.class);
        // 测试从配置文件中加载并注册消息类型和对应的处理函数
        dictionaries.load(System.getProperty("user.dir")
                + File.separator + "test" + File.separator + "config" + File.separator + "messages.xml");
        new TClient2().start(8989);
    }
}
