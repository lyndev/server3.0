/**
 * @date 2014/3/25
 * @author ChenLong
 */
package gametestclient;

import game.core.command.CommandProcessor;
import game.core.command.Handler;
import game.core.command.ICommand;
import game.core.message.Dictionary;
import game.core.message.DictionaryManager;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.net.codec.ServerProtocolCodecFactory;
import game.core.net.communication.CommunicationC;
import game.core.net.server.IServer;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * @author ChenLong
 */
public class GameTestClient extends CommandProcessor
{
    private Logger log = Logger.getLogger(GameTestClient.class);
    private final static Object obj = new Object();
    private static GameTestClient client;
    private CommunicationC connector;
    public final static DictionaryManager dictionaries = new DictionaryManager();
    private IoSession clientSession;

    private GameTestClient()
    {
        super(GameTestClient.class.getSimpleName());
        try
        {
            dictionaries.load(System.getProperty("user.dir") + File.separator + "src" + File.separator + "messages.xml");
            connector = newCommunicationC();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static GameTestClient getInstance()
    {
        synchronized (obj)
        {
            if (client == null)
                client = new GameTestClient();
        }
        return client;
    }

    private CommunicationC newCommunicationC()
    {
        return new CommunicationC(new IServer()
        {
            @Override
            public void doCommand(IoSession session, IoBuffer buf)
            {
                GameTestClient.this.doCommand(session, buf);
            }

            @Override
            public void sessionCreate(IoSession session)
            {
                clientSession = session;
                System.out.println("game session " + session.getId() + " create");
            }

            @Override
            public void sessionClosed(IoSession session)
            {
                System.out.println("game session " + session.getId() + " close");
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status)
            {
                System.out.println("game session " + session.getId() + " heart timeout");
                session.close(true);
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause)
            {
                session.close(true);
            }

            @Override
            public ProtocolCodecFactory getCodecFactory()
            {
                return new ServerProtocolCodecFactory();
            }

        });
    }

    public void start(String[] args)
    {
        //connector.send(clientSession, sendMsg);
                /*
         game.message.Client$ReqLogin
         Player.Person.Builder builder = Player.Person.newBuilder();
         builder.setId(2);
         builder.setName("沉默的猪");
         builder.setEmail("wjv.1983@gmail.com");

         Player.Person person = builder.build();
         System.out.println("发送消息:\n" + person);

         SMessage sendMsg = new SMessage(
         Player.Person.MsgID.eMsgID_VALUE, person.toByteArray());
         session.write(sendMsg);
         */
    }


    @Override
    public void writeError(String message)
    {
        log.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        log.error(message, t);
    }

    public void connect(String ip, int port)
    {
        connector.connect(ip, port);
    }

    public boolean isConnected()
    {
        return clientSession != null;
    }

    public void sendToServer(SMessage msg)
    {
        if (clientSession != null)
        {
            clientSession.write(msg);
        }
        else
        {
            log.error("clientSession is null, click 连接按钮");
        }
    }

    @Override
    public void doCommand(ICommand command)
    {
        Handler handler = (Handler) command;
        handler.action();
    }

    public void doCommand(org.apache.mina.core.session.IoSession session, org.apache.mina.core.buffer.IoBuffer buf)
    {
        try
        {
            System.out.println("msg from sessionId = " + session.getId() + "\t" + buf.toString());
            // 读取消息ID
            int msgId = buf.getInt();
            System.out.println("msg id: " + msgId);
            // 获取消息字典
            Dictionary dic = dictionaries.get(msgId);
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
                    addCommand(handler);
                }
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
        {
            log.error("error", e);
        }
    }
}
