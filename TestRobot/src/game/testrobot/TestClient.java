/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot;

import game.core.command.Handler;
import game.core.command.ICommand;
import game.core.message.Dictionary;
import game.core.message.DictionaryManager;
import game.core.message.RMessage;
import game.core.net.codec.ClientProtocolCodecFactory;
import game.core.net.communication.CommunicationC;
import game.core.net.communication.ICommunication;
import game.core.net.server.IServer;
import game.testrobot.constant.SessionKey;
import game.testrobot.line.Line;
import game.testrobot.line.LineManager;
import game.testrobot.robot.RobotPlayer;
import game.testrobot.robot.RobotPlayerManager;
import game.testrobot.robot.RobotStatus;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * @author ChenLong
 */
public class TestClient implements IServer
{
    private final static Logger logger = Logger.getLogger(TestClient.class);
    private final static TestClient instance;
    private final static DictionaryManager dictionaries = new DictionaryManager(); // 消息字典集合
    private final Map<Long, IoSession> sessions = new ConcurrentHashMap<>();
    private CommunicationC connector = null;

    private final Queue<RobotPlayer> connectQ = new LinkedBlockingQueue<>();

    private final BlockingQueue<Byte> lockq = new ArrayBlockingQueue<>(2);

    private final AtomicInteger count = new AtomicInteger();
    private final long start = System.currentTimeMillis();

    ;

    static
    {
        instance = new TestClient();
    }

    private TestClient()
    {
        loadLog4JConfig();
        loadMessageDictionary();
        LineManager.getInstance();

        connector = new CommunicationC(this, 20000);
        connector.initialize();
    }

    public static TestClient getInstance()
    {
        return instance;
    }

    public void start(String[] args)
    {
        count.set(10000);
        batchLogin(count.get());
        //serialLogin(count.get());
    }

    public void batchLogin(int num)
    {
        for (int i = 0; i < num; ++i)
        {
            if (i != 0 && i % 1000 == 0)
            {
                try
                {
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " + num);
                    Thread.sleep(10 * 1000);
                }
                catch (InterruptedException ex)
                {

                }
            }
            RobotPlayer player = new RobotPlayer();
            player.setUserName("Robot" + 8 + "" + i);
            //player.setUserName(Long.toString(startTime/10000000) + "_" + i);

            // 向RobotPlayerManager中注册
            RobotPlayerManager.getInstance().putByUserName(player.getUserName(), player);

            // 向Line中注册
            int lineId = LineManager.getInstance().randLine(player.getUserName()); // 根据帐号名随机选线
            Line line = LineManager.getInstance().getLine(lineId);
            LineManager.getInstance().addCommand(lineId, new ICommand()
            {
                private Line line;
                private RobotPlayer player;

                public ICommand set(Line line, RobotPlayer player)
                {
                    this.line = line;
                    this.player = player;
                    return this;
                }

                @Override
                public void action()
                {
                    line.putPlayerByUserName(player.getUserName(), player);
                }
            }.set(line, player));

            connect(player);
        }
    }

    public void decCount()
    {
        if (count.decrementAndGet() <= 0)
        {
            long end = System.currentTimeMillis();
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%% end - start = " + (end - start));
        }
    }

    public void unlock()
    {
        lockq.add((byte) 2);
    }

    public void serialLogin(int num)
    {
        lockq.add((byte) 1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i)
        {
            while (true)
            {
                try
                {
                    lockq.take();
                    break;
                }
                catch (InterruptedException ex)
                {
                    logger.warn("InterruptedException", ex);
                }
            }

            RobotPlayer player = new RobotPlayer();
            player.setUserName("Robot_" + 2 + "_" + i);
            //player.setUserName(Long.toString(startTime/10000000) + "_" + i);

            // 向RobotPlayerManager中注册
            RobotPlayerManager.getInstance().putByUserName(player.getUserName(), player);

            // 向Line中注册
            int lineId = LineManager.getInstance().randLine(player.getUserName()); // 根据帐号名随机选线
            Line line = LineManager.getInstance().getLine(lineId);
            LineManager.getInstance().addCommand(lineId, new ICommand()
            {
                private Line line;
                private RobotPlayer player;

                public ICommand set(Line line, RobotPlayer player)
                {
                    this.line = line;
                    this.player = player;
                    return this;
                }

                @Override
                public void action()
                {
                    line.putPlayerByUserName(player.getUserName(), player);
                }
            }.set(line, player));

            connect(player);
        }
        //long end = System.currentTimeMillis();
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> end - start = " + (end - start));
    }

    public void connect(RobotPlayer player)
    {
        connectQ.add(player);
        connector.connect("192.168.1.36", 10087);
        //connector.connect("192.168.10.32", 10086);
        //connector.connect("127.0.0.1", 10086);
    }

    private void connectSuccess(IoSession session)
    {
        RobotPlayer player = connectQ.poll();

        session.setAttribute(SessionKey.LINE_ID.name(), player.getLineId());
        session.setAttribute(SessionKey.USER_NAME.name(), player.getUserName());

        LineManager.getInstance().addCommand(player.getLineId(),
                new ICommand()
                {
                    private RobotPlayer player;
                    private IoSession session;

                    public ICommand set(RobotPlayer player, IoSession session)
                    {
                        this.player = player;
                        this.session = session;
                        return this;
                    }

                    @Override
                    public void action()
                    {
                        player.setSession(session);
                        player.setStatus(RobotStatus.CONNECTED);
                        player.login();
                    }
                }.set(player, session));
    }

    public long getStart()
    {
        return start;
    }

    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        try
        {
            logger.debug("msg from sessionId = " + session.getId() + "\t" + buf.toString());

            // 读取消息ID
            int msgId = buf.getInt();
            logger.debug("msg id: " + msgId);

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

                    Object userNameObj = session.getAttribute(SessionKey.USER_NAME.name());
                    Object lineIdObj = session.getAttribute(SessionKey.LINE_ID.name());
                    if (userNameObj != null && lineIdObj != null)
                    {
                        String userName = (String) userNameObj;
                        int lineId = (Integer) lineIdObj;
                        RobotPlayer player = RobotPlayerManager.getInstance().getByUserName(userName);
                        if (player != null)
                        {
                            handler.setAttribute("PLAYER", player);
                            LineManager.getInstance().addCommand(lineId, handler);
                        }
                        else
                        {
                            logger.error("cannot get RobotPlayer, userName: [" + userName + "]");
                        }
                    }
                    else
                    {
                        logger.error("cannot get userName, msgId = " + msgId);
                    }
                }
                else
                {
                    logger.error("Not found message handler! msgId = " + msgId);
                }
            }
            else
            {
                // do someting...
                if (msgId != 100199)
                    logger.error("Not found message dictionary! msgId = " + msgId);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
        {
            logger.error("error", e);
        }
    }

    @Override
    public void sessionCreate(IoSession session)
    {
        sessions.put(session.getId(), session);
        System.out.println("sessionId = " + session.getId());
        connectSuccess(session);
    }

    @Override
    public void sessionClosed(IoSession session)
    {
        sessions.remove(session.getId());
        System.out.println("sessionId = " + session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
        System.out.println("sessionId = " + session.getId() + status.toString() + " , close");
        session.close(false);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
        cause.printStackTrace();
    }

    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
        return new ClientProtocolCodecFactory();
    }

    class ConnectWork implements Runnable
    {
        private String ip;
        private int port;
        private int connectNum;

        public ConnectWork(String ip, int port, int connectNum)
        {
            this.ip = ip;
            this.port = port;
            this.connectNum = connectNum;
        }

        @Override
        public void run()
        {
            connector.connect(ip, 8989);
        }
    }

    /**
     * 加载log4j配置信息.
     */
    private void loadLog4JConfig()
    {
        String configPath = System.getProperty("user.dir") + File.separator + "config" + File.separator;
        String val = System.getProperty("ideDebug");
        if (val != null && val.equals("true"))
        {
            DOMConfigurator.configureAndWatch(configPath + "log4j_devel.xml");
        }
        else
        {
            DOMConfigurator.configureAndWatch(configPath + "log4j_server.xml");
        }
    }

    /**
     * 加载消息配置
     */
    private void loadMessageDictionary()
    {
        StringBuilder configDir = new StringBuilder();
        configDir.append(System.getProperty("user.dir"));
        configDir.append(File.separator);
        configDir.append("config");
        configDir.append(File.separator);

        try
        {
            // 加载消息和对应的处理器
            dictionaries.load(configDir.toString() + "messages.xml");
        }
        catch (Exception e)
        {
            logger.error("加载系统配置文件出错！", e);
        }
    }
}
