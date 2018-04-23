package game.server.fight;

import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.net.codec.InnerProtocolCodecFactory;
import game.core.net.communication.CommunicationC;
import game.core.net.server.IServer;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.thread.DispatchProcessor;
import game.server.util.MessageDictionary;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * @date 2014-7-2
 * @author pengmian
 */
public class FightConnector implements IServer
{
    private final Logger logger = Logger.getLogger(FightConnector.class);
    private AtomicReference<IoSession> fightServerSession = new AtomicReference<>();
    private final CommunicationC connector;
    private String ipAddress;
    private int port;
    private final BlockingDeque<SMessage> cachedMsg;
    private volatile Thread sendThread;
    private volatile Boolean running;

    /**
     * 单件
     */
    private static final FightConnector singleton;

    static
    {
        singleton = new FightConnector();
    }

    public static FightConnector getSingleton()
    {
        return singleton;
    }

    /**
     * 构造
     */
    private FightConnector()
    {
        sendThread = null;
        cachedMsg = new LinkedBlockingDeque<>();
        connector = new CommunicationC(this, 1);
        connector.initialize();
    }

    /**
     * 连接是否成功
     *
     * @return
     */
    public boolean isConnected()
    {
        return fightServerSession.get() != null && fightServerSession.get().isConnected();
    }

    /**
     * 连接
     *
     * @param address
     * @param port
     */
    public void connect(String address, int port)
    {
        this.ipAddress = address;
        this.port = port;

        if (sendThread == null || !sendThread.isAlive())
        {
            sendThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    running = true;
                    while (running)
                    {
                        try
                        {
                            if (!isConnected())
                            {
                                if (!connector.connect(ipAddress, FightConnector.this.port))
                                {
                                    logger.error("与战斗服务器的连接已经断开，5秒后重新连接！");

                                    try
                                    {
                                        Thread.sleep(5 * 1000);
                                    }
                                    catch (InterruptedException ex)
                                    {
                                        logger.error("InterruptedException", ex);
                                    }
                                }
                            }
                            else
                            {

                                SMessage msg = null;
                                try
                                {
                                    msg = cachedMsg.take();
                                    if (msg != null)
                                    {
                                        /// 连接断开可能会丢消息
                                        WriteFuture future = fightServerSession.get().write(msg, null);
//                                    future.await();
//                                    if (!future.isWritten())
//                                    {
//                                        cachedMsg.addFirst(msg);
//                                    }
                                    }

                                }
                                catch (InterruptedException | UnsupportedOperationException ex)
                                {
                                    if (msg != null)
                                    {
                                        cachedMsg.addFirst(msg);
                                    }
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            logger.error("catch all exception to avoid thread exit", ex);
                        }
                    }
                    if (fightServerSession.get() != null)
                        fightServerSession.get().close(true);
                }
            });
            sendThread.start();
        }
    }

    /**
     * 向战斗服务器发送消息
     *
     * @param message
     */
    public void send(SMessage message)
    {
        cachedMsg.add(message);
    }

    public void stop()
    {
        running = false;
        if (sendThread != null && sendThread.isAlive())
        {
            sendThread.interrupt();
        }
    }

    /**
     * 消息处理
     *
     * @param session
     * @param buf
     */
    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        int msgId = buf.getInt();
        long userId = buf.getLong();

        Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
        if (dic == null)
        {
            logger.error("Not found message dictionary! msgId = " + msgId);
            return;
        }

        try
        {
            Handler handler = dic.getHandlerInstance();
            if (handler == null)
            {
                logger.error("Not found message handler! msgId = " + msgId);
                return;
            }

            byte[] msgData = new byte[buf.remaining()];
            buf.get(msgData);

            Player player = PlayerManager.getPlayerByUserId(userId);
            if (player == null)
            {
                logger.info("战斗服务器结果返回时，玩家已经不在了！ userId = " + userId + "msgId = " + msgId);
                return;
            }

            RMessage message = new RMessage();
            message.setId(msgId);
            message.setData(dic.getMessage(), msgData);
            message.setSession(player.getSession());

            handler.setMessage(message);
            DispatchProcessor.getInstance().addCommand(handler);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex)
        {
            logger.error("error: ", ex);
        }
    }

    @Override
    public void sessionCreate(IoSession session)
    {
        logger.info("成功连接战斗服务器：" + session.getRemoteAddress().toString());
        fightServerSession.set(session);

    }

    @Override
    public void sessionClosed(IoSession session)
    {
        logger.info("与战斗服务器的连接: [" + session.getRemoteAddress().toString() + "]已经断开，尝试重新连接");
        //session.close(true);
        fightServerSession.get().close(true);

        if (sendThread != null && sendThread.isAlive())
        {
            sendThread.interrupt();
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
        logger.info("session idle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
        logger.info("session [" + session.getRemoteAddress().toString() + "exception: " + cause.toString());
        fightServerSession.get().close(true);
    }

    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
        return new InnerProtocolCodecFactory();
    }

}
