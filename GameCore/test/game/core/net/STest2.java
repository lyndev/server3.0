package game.core.net;

import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.RMessage;
import game.core.message.DictionaryManager;
import game.core.message.SMessage;
import game.core.message.Player;
import game.core.net.codec.ServerProtocolCodecFactory;
import game.core.net.communication.ICommunication;
import game.core.net.communication.CommunicationS;
import game.core.net.server.IServer;
import game.core.util.SessionUtils;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 * Protobuf的服务端测试程序
 *
 * @author wangJingWei
 */
class TServer2 implements IServer
{
    private final Logger log = Logger.getLogger(TServer2.class);

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
            Dictionary dic = STest2.dictionaries.get(msgId);
            if (dic != null)
            {
                Handler handler = dic.getHandlerInstance();
                if (handler != null)
                {
                    // 读取消息内容
                    byte[] msgData = new byte[buf.remaining()];
                    buf.get(msgData);
                    // 创建消息对象
                    RMessage message = new RMessage();
                    message.setId(msgId);
                    message.setSession(session);
                    message.setData(dic.getMessage(), msgData);
                    // 交给handler处理消息
                    handler.setMessage(message);
                    handler.action();
                }
            }

            // 测试回发消息，服务端发送消息的代码应该是在Handler对应的Service中处理，但现在没有设计Service，故这里先做测试
            Player.Person.Builder builder = Player.Person.newBuilder();
            builder.setId(2);
            builder.setName("沉默的猪");
            builder.setEmail("wjv.1983@gmail.com");

            Player.Person person = builder.build();
            System.out.println("发送消息:\n" + person);

            SMessage sendMsg = new SMessage(
                    Player.Person.MsgID.eMsgID_VALUE, person.toByteArray());
            session.write(sendMsg);
        }
        catch (Exception e)
        {
            log.error("error", e);
        }
    }

    @Override
    public void sessionCreate(IoSession session)
    {
        System.out.println("sessionCreate sessionId = " + session.getId());
    }

    @Override
    public void sessionClosed(IoSession session)
    {
        System.out.println("sessionClosed sessionId = " + session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
        SessionUtils.closeSession(session, "回收空闲连接！");
    }

    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
//        return new InnerProtocolCodecFactory();
        return new ServerProtocolCodecFactory(null, null, null);
    }

    public void start(int port)
    {
        ICommunication server = new CommunicationS(this, 65535);
        server.accept(port);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
        session.close(true);
        log.error(session + " close exceptionCaught. cause: " + cause.getMessage());
    }
}

public class STest2
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
        new TServer2().start(8989);
        System.out.println("Server started.");
    }
}
