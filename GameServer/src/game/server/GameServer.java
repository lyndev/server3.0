package game.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.haowan.logger.service.ServicesFactory;
import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.net.codec.ServerProtocolCodecFactory;
import game.core.net.codec.ServerProtocolUDPCodecFactory;
import game.core.net.codec.websocket.MinaWebSocketClientProtocolCodecFactory;
import game.core.net.communication.CommunicationS;
import game.core.net.communication.CommunicationUDP;
import game.core.net.communication.WebSocketServer;
import game.core.net.server.IServer;
import game.core.script.ScriptManager;
import game.core.timer.SimpleTimerProcessor;
import game.core.timer.TimerEvent;
import game.data.GameDataManager;
import game.data.bean.q_filterwordBean;
import game.data.bean.q_scriptBean;
import game.message.BaseMessage;
import game.message.LoginMessage;
import game.server.config.MongoDBConfig;
import game.server.config.ServerConfig;
import game.server.config.ServerType;
import game.server.db.DBFactory;
import game.server.db.game.dao.RankDao;
import game.server.gm.GMCommandServer;
import game.server.http.GameHttpServer;
import game.server.logic.constant.GameServerState;
import game.server.logic.constant.HandlerKey;
import game.server.logic.constant.SessionKey;
import game.server.logic.line.GameLine;
import game.server.logic.line.GameLineManager;
import game.server.logic.login.handler.SessionCloseHandler;
import game.server.logic.loginGift.LoginGiftService;
import game.server.logic.mail.service.MailService;
import game.server.logic.notice.NoticeManager;
import game.server.logic.player.PlayerManager;
import game.server.logic.recharge.RechargeResultGetter;
import game.server.logic.robot.RobotNames;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.ClientVersion;
import game.server.logic.support.ServicePool;
import game.server.thread.DispatchProcessor;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.DBOperatorHandler;
import game.server.util.MessageDictionary;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.SensitiveWordFilter;
import game.server.util.SessionUtils;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.xml.sax.SAXException;

public class GameServer
{
    private final static Logger logger = Logger.getLogger(GameServer.class);
    private CommunicationS clientAcceptor; // 接收客户端请求的应答器
    private CommunicationUDP clintUDPAcceptor; // upd接收器；
    private volatile long lastAcrossDayTime = System.currentTimeMillis(); // 上一次跨天tick时间点, 用于处理跨天/时限等操作
    private long lastGCTime = System.currentTimeMillis(); // 上一次GameServer自身执行System.gc()时间点
    private GameServerState state = GameServerState.NULL;
    private GMCommandServer gmCommand; // 接收平台发送的GM指令的服务端程序

    private GameServer()
    {
    }

    /**
     * 获取GameServer的实例对象.
     *
     * @return
     */
    public static GameServer getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 启动GameServer
     *
     * @param args
     */
    public void start(String[] args)
    {
        state = GameServerState.STARTING;
        try
        {
            init();
        }
        catch (Exception ex)
        {
            logger.error("Exception", ex);
            System.exit(1);
        }
        addShutdownHook();
        state = GameServerState.STARTED;
    }
    
    private void init()
    {
        long startTime = System.currentTimeMillis();
        initLastGCTime();
        loadConfig(); // 加载配置
        loadGameData(); // 加载GameData
        UniqueId.init(); // 初始化ID分配器
        ClientVersion.getInstance().load(); // 加载客户端版本配置
        initGameLogger(); // 初始化GameLogger
        initScriptManager(); // 初始化脚本系统
        initSensitiveWordFilter(); // 初始化敏感词过滤器
        GameLineManager.getInstance().init(); // 初始化逻辑线程池
        ServicePool.getInstance().init(); // 初始化所有Service
        GameDBOperator.getInstance().start(); // 启动GameDBOperator
        //loadRobot(); // 生成并加载机器人
        initializeGameWorld(); // 异步初始化GameWorld
        RankDao.initRankLists(); // 初始化成就排行榜
        MailService.getInstance().load(); // 加载定时邮件
        LoginGiftService.getInstance().load(); //加载
        NoticeManager.getInstance().load(); // 加载定时公告
        addUniqueIdSaveTimer(); // 定时器
        
// 不同的游戏服务器组装的消息体格式不一样
        int _serverType = ServerConfig.getInstance().getServerType();
        ServerType _type = ServerType.getTypeByValue(_serverType);
        if(_type == ServerType.CREATOR_SERVER){
            clientAcceptor = newWebSokcetClientAcceptor(); // 初始化配置后才能创建应答器
        } else if(_type == ServerType.COCOS2D_SERVER || _type == ServerType.UNITY3D_SERVER){
            clientAcceptor = newClientAcceptor();//newWebSokcetClientAcceptor(); // 初始化配置后才能创建应答器
            clintUDPAcceptor = CommunicationUDP();// udp
            clintUDPAcceptor.accept(ServerConfig.getInstance().getGameUDPPort());
        }

        GameHttpServer.getInstance().accept(ServerConfig.getInstance().getHttpPort()); // 启动内部http server
        clientAcceptor.accept(ServerConfig.getInstance().getGamePort()); // 最后才启动监听端口
        gmCommand = new GMCommandServer();
        gmCommand.getServer().accept(ServerConfig.getInstance().getGmPort());
       // new Thread(new KeysResultGetter()).start(); // 激活码，兑换码
        logger.info("===============================================");
        logger.info("\t服务器启动成功! 启动消耗时间： " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
        logger.info("===============================================");
    }


    public void executeStopTask()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    stop();
                    Runtime.getRuntime().halt(0);
                }
                catch (Throwable t)
                {
                    logger.error("executeStopTask", t);
                }
            }
        }).start();
    }

    /**
     * 停止GameServer
     */
    public void stop()
    {
        logger.info("JVM exit, call GameServer.stop");
        { // 停止获取充值单验证结果线程
            logger.info("stop and await RechargeResultGetter...");
            RechargeResultGetter.getInstance().stopAndAwaitStop();
        }
        { // 停服通知
            PlayerManager.serverRestartNotify();
        }
        { // 禁止连接
            clientAcceptor.unaccept();
            gmCommand.getServer().unaccept();
        }
        { // 停止消息分发线程
            logger.info("stop and await DispatchProcessor...");
            DispatchProcessor.getInstance().stopAndAwaitStop(false);
        }
        { // 停止各逻辑线程
            logger.info("stop and await GameLineManager...");
            GameLineManager.getInstance().stopAndAwaitStop();
        }
        {// 停止GameWorld线程
            logger.info("stop and await GameWorld...");
            GameWorld.getInstance().stopAndAwaitStop();
        }
        { // 关闭GameDB数据库操作线程
            logger.info("stop and await GameDBOperator ...");
            GameDBOperator.getInstance().stopAndAwaitStop();
        }
        { // 回存ID序列数
            logger.info("save UniqueId sequences ...");
            UniqueId.save();
        }
        { // 停止定时器线程
            logger.info("stop and await SimpleTimerProcessor ...");
            SimpleTimerProcessor.getInstance().stopAndAwaitStop(false);
        }
        { // 回存定时邮件
            logger.info("save MailService schedule mail ...");
            MailService.getInstance().save();
        }
        { // 回存定时公告
            logger.info("save NoticeManager ...");
            NoticeManager.getInstance().save();
        }
        {
            logger.info("===============================================");
            logger.info("\t服务器关闭!");
            logger.info("===============================================");
        }
    }

    
    
    public long getLastAcrossDayTime()
    {
        return lastAcrossDayTime;
    }

    public void setLastAcrossDayTime(long time)
    {
        lastAcrossDayTime = time;
    }

    public GameServerState getGameServerState()
    {
        return state;
    }

    /**
     * 加载配置
     */
    private void loadConfig()
    {
        try
        {
            loadLog4JConfig(); // 加载log4j配置信息
            loadServerConfig(); // 加载ServerConfig配置
            loadMessageDictionaryConfig();
        }
        catch (Exception ex)
        {
            logger.error("load config failure", ex);
            System.exit(1);
        }
    }

    /**
     * 加载GameData
     */
    private void loadGameData()
    {
        GameDataManager.getInstance().setSqlSessionFactory(DBFactory.GAME_DATA_DB.getSessionFactory()).loadAll(); // 加载配置数据
        BeanTemplet.loadAllItemContainer();
    }

    /**
     * 加载log4j配置信息.
     */
    private void loadLog4JConfig()
    {
        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
        if (MiscUtils.isIDEEnvironment())
            pathBuilder.append("log4j_devel.xml");
        else
            pathBuilder.append("log4j_server.xml");
        DOMConfigurator.configureAndWatch(pathBuilder.toString());
    }

    /**
     * 加载ServerConfig配置信息
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private void loadServerConfig() throws ParserConfigurationException, SAXException, IOException
    {
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }

    /**
     * 加载系统配置信息.
     *
     * @param initialize 系统启动时初始化请传true，系统启动后重新加载请传false.
     */
    private void loadMessageDictionaryConfig() throws Exception
    {
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "messages.xml";
        MessageDictionary.getInstance().load(filePath);
    }

    /**
     * 生成并加载机器人
     */
    private void loadRobot()
    {
        RobotNames.load(); // 加载机器人随机名称组合
        logger.info("加载机器人：" + PlayerManager.loadRobots()); // 加载机器人数据
    }

    /**
     * 异步初始化GameWorld
     */
    private void initializeGameWorld()
    {
        GameWorld.getInstance().init(8000); // 初始化GameWorld线程
    }

    /**
     * 注册JVM退出hook
     */
    private void addShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                stop();
            }
        }));
    }

    private CommunicationS newClientAcceptor()
    {
        return CommunicationS();
    }

    private CommunicationS newWebSokcetClientAcceptor()
    {
        return new CommunicationS(new WebSocketServer()
        {
            @Override
            public void doCommand(IoSession session, IoBuffer buf)
            {
                try
                {
                    GameServer.this.doWebSocketCommand(session, buf);
                }
                catch (InvalidProtocolBufferException ex)
                {
                    java.util.logging.Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void sessionCreate(IoSession session)
            {
                logger.info("client session " + session.getId() + " create");
            }

            @Override
            public void sessionClosed(IoSession session)
            {
                logger.info("client session " + session.getId() + " close");
                // 服务端内部调用的handler没有消息号，不能由DispatchProcessor分派处理器，直接选择处理器
                Object valObj = session.getAttribute(SessionKey.GAME_LINE.name());
                if (valObj != null && valObj instanceof GameLine)
                {
                    GameLine line = (GameLine) valObj;
                    Handler handler = new SessionCloseHandler();
                    handler.setAttribute(HandlerKey.SESSION.name(), session);
                    GameLineManager.getInstance().addCommand(line.getLineId(), handler);
                }
                else
                {
                    logger.warn("session has not SESSION_LINE_ID attribute, session: " + SessionUtils.allGameAttributesToString(session));
                }
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status)
            {
                logger.info("client session " + session.getId() + " heart timeout");
                session.close(true);
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause)
            {
                logger.info("in exceptionCaught, session id = " + session.getId());
                if (logger.isDebugEnabled())
                    logger.debug("in exceptionCaught, session id = " + session.getId() + ", exceptionCaught", cause);
                session.close(true);
            }

            @Override
            public ProtocolCodecFactory getCodecFactory()
            {
                return new MinaWebSocketClientProtocolCodecFactory();
            }
        }, ServerConfig.getInstance().getMaxConnect(), ServerConfig.getInstance().getHeartTime()); // 设置最大连接数和心跳时间);
    }

    private CommunicationS CommunicationS()
    {
        return new CommunicationS(new IServer()
        {
            @Override
            public void doCommand(IoSession session, IoBuffer buf)
            {
                GameServer.this.doCommand(session, buf);
            }

            @Override
            public void sessionCreate(IoSession session)
            {
                logger.info("tcp-client session " + session.getId() + " create");
            }

            @Override
            public void sessionClosed(IoSession session)
            {
                logger.info("tcp-client session " + session.getId() + " close");
                // 服务端内部调用的handler没有消息号，不能由DispatchProcessor分派处理器，直接选择处理器
                Object valObj = session.getAttribute(SessionKey.GAME_LINE.name());
                if (valObj != null && valObj instanceof GameLine)
                {
                    GameLine line = (GameLine) valObj;
                    Handler handler = new SessionCloseHandler();
                    handler.setAttribute(HandlerKey.SESSION.name(), session);
                    GameLineManager.getInstance().addCommand(line.getLineId(), handler);
                }
                else
                {
                    logger.warn("session has not SESSION_LINE_ID attribute, session: " + SessionUtils.allGameAttributesToString(session));
                }
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status)
            {
                logger.info("tcp-client session " + session.getId() + " heart timeout");
                session.close(true);
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause)
            {
                logger.info("in exceptionCaught, session id = " + session.getId());
                if (logger.isDebugEnabled())
                    logger.debug("in exceptionCaught, session id = " + session.getId() + ", exceptionCaught", cause);
                session.close(true);
            }

            @Override
            public ProtocolCodecFactory getCodecFactory()
            {
                return new ServerProtocolCodecFactory();
            }
        }, ServerConfig.getInstance().getMaxConnect(), ServerConfig.getInstance().getHeartTime()); // 设置最大连接数和心跳时间
    }

    private CommunicationUDP CommunicationUDP()
    {
        return new CommunicationUDP(new IServer()
        {
            @Override
            public void doCommand(IoSession session, IoBuffer buf)
            {
                GameServer.this.doCommand(session, buf);
            }

            @Override
            public void sessionCreate(IoSession session)
            {
                logger.info("udp-client session  " + session.getId() + " create");
            }

            @Override
            public void sessionClosed(IoSession session)
            {
                logger.info(" udp-client session " + session.getId() + " close");
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status)
            {
                logger.info("CommunicationUDP client session " + session.getId() + " heart timeout");
                session.close(true);
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause)
            {
                logger.info("in exceptionCaught, session id = " + session.getId());
                if (logger.isDebugEnabled())
                    logger.debug("in exceptionCaught, session id = " + session.getId() + ", exceptionCaught", cause);
                session.close(true);
            }

            @Override
            public ProtocolCodecFactory getCodecFactory()
            {
                return new ServerProtocolCodecFactory();
            }
        }, ServerConfig.getInstance().getMaxConnect(), ServerConfig.getInstance().getHeartTime());
    }

    
    // websokcet消息命令执行函数
    private void doWebSocketCommand(IoSession session, IoBuffer buf) throws InvalidProtocolBufferException
    {
        try
        {

            byte[] rpcMsgData = new byte[buf.remaining()];
            buf.get(rpcMsgData);
            BaseMessage.Rpc rpc = BaseMessage.Rpc.parseFrom(rpcMsgData);
            BaseMessage.RpcData rpcData = rpc.getRpcData();
            int msgId = rpcData.getMsgId();
            logger.info("msg from sessionId = " + msgId + "\t" + rpcData.getSerializedMsgData().toString());

            int order = 1;//buf.getInt();
            int msgTimes = (int) System.currentTimeMillis(); //buf.getInt();

            // 获取消息字典
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
            if (dic != null)
            {
                Handler handler = dic.getHandlerInstance();
                if (handler != null)
                {
                    // 具体的消息数据
                    byte[] serializedMsgData = rpcData.getSerializedMsgData().toByteArray();

                    // 创建消息对象
                    RMessage message = new RMessage();
                    message.setId(msgId);
                    message.setOrder(order);
                    message.setTimes(msgTimes);
                    message.setSession(session);
                    message.setData(dic.getMessage(), serializedMsgData);
                    // 交给handler处理消息
                    handler.setMessage(message);
                    // 由命令分发器来负责分派线程去执行handler
                    DispatchProcessor.getInstance().addCommand(handler);
                }
                else
                {
                    // do someting...
                    logger.error("Not found message handler! msgId = " + msgId);
                }
            }
            else
            {
                // do someting...
                logger.error("Not found message dictionary! msgId = " + msgId);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
        {
            logger.error("error", e);
        }
        finally
        {
            LoginMessage.ResAck.Builder ackMsg = LoginMessage.ResAck.newBuilder();
            int _ackTime = (int) (System.currentTimeMillis() / 1000L);
            logger.info("ack time:"+ _ackTime);
            ackMsg.setTime(_ackTime);
            MessageUtils.send(session, new SMessage(LoginMessage.ResAck.MsgID.eMsgID_VALUE, ackMsg.build().toByteArray()));

        }
    }

    /**
     * 根据message配置构造对应的protobuf消息和处理handler
     *
     * @param session
     * @param buf
     */
    private void doCommand(IoSession session, IoBuffer buf)
    {
        try
        {
            logger.debug("msg from sessionId = " + session.getId() + "\t" + buf.toString());

            int order = buf.getInt();
            int msgTimes = buf.getInt();

            // 读取消息ID
            int msgId = buf.getInt();
            logger.debug("msg id: " + msgId);

            // 获取消息字典
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
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
                    message.setOrder(order);
                    message.setTimes(msgTimes);
                    message.setSession(session);
                    message.setData(dic.getMessage(), msgData);
                    // 交给handler处理消息
                    handler.setMessage(message);
                    // 由命令分发器来负责分派线程去执行handler
                    DispatchProcessor.getInstance().addCommand(handler);
                }
                else
                {
                    // do someting...
                    logger.error("Not found message handler! msgId = " + msgId);
                }
            }
            else
            {
                // do someting...
                logger.error("Not found message dictionary! msgId = " + msgId);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
        {
            logger.error("error", e);
        }
        finally
        {
           // LoginMessage.ResAck.Builder ackMsg = LoginMessage.ResAck.newBuilder();
           // int _ackTime = (int) (System.currentTimeMillis() / 1000L);
           // ackMsg.setTime(_ackTime);
           // MessageUtils.send(session, new SMessage(LoginMessage.ResAck.MsgID.eMsgID_VALUE, ackMsg.build().toByteArray()));
        }
    }

    private class IdSaveTimerEvent extends TimerEvent
    {

        public IdSaveTimerEvent(long initialDelay, long delay, boolean loopFixed)
        {
            super(initialDelay, delay, loopFixed);
        }

        @Override
        public void run()
        {
            GameDBOperator.getInstance().submitRequest(new DBOperatorHandler(0)
            {
                @Override
                public void action()
                {
                    UniqueId.save();
                }
            });
        }
    }

    private void addUniqueIdSaveTimer()
    {
        long delay = 30; // （单位：秒）回存间隔
        if (MiscUtils.isIDEEnvironment())
            delay = 1;
        SimpleTimerProcessor.getInstance().addEvent(new IdSaveTimerEvent(delay * 1000, delay * 1000, true));
    }

    public long getLastGCTime()
    {
        return lastGCTime;
    }

    public void setLastGCTime(long lastGCTime)
    {
        this.lastGCTime = lastGCTime;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {

        INSTANCE;

        GameServer processor;

        Singleton()
        {
            this.processor = new GameServer();
        }

        GameServer getProcessor()
        {
            return processor;
        }
    }

    /**
     * 启动GameServer时将lastGCTime初始化为前一天的时间, 保证第一天开始就能正确执行GC
     */
    private void initLastGCTime()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        lastGCTime = cal.getTimeInMillis();
    }

    /**
     * 初始化GameLogger
     */
    private void initGameLogger()
    {
        MongoDBConfig mongoDBConfig = ServerConfig.getInstance().getMongoDBConfig();
        ServicesFactory.getSingleLogService().initGameLogger(
                mongoDBConfig.getGameId(),
                mongoDBConfig.getMongodbHost(),
                Integer.toString(mongoDBConfig.getMongodbPort()),
                mongoDBConfig.getMongodbName(),
                mongoDBConfig.getMongodbUser(),
                mongoDBConfig.getMongodbPasswd());

        //ServicesFactory.getSingleVerifyService().initLibrary("172:18021:1.0.0:553", "553", "szdc");

        //RechargeResultGetter.getInstance().start(); // 启动获取充值单验证结果线程
    }

    /**
     * 初始化脚本系统
     */
    private void initScriptManager()
    {
        Map<Integer, String> scriptNames = new HashMap<>();
        for (Map.Entry entry : GameDataManager.getInstance().q_scriptContainer.getMap().entrySet())
        {
            int scriptId = (Integer) entry.getKey();
            String scriptName = ((q_scriptBean) entry.getValue()).getQ_script_name();

            scriptNames.put(scriptId, scriptName);
        }

        try
        {
            ScriptManager.getInstance().initialize("scripts/java", "./scriptBin", scriptNames, "scripts/js");
        }
        catch (FileNotFoundException | ScriptException ex)
        {
            logger.error("FileNotFoundException | ScriptException", ex);
        }
    }

    /**
     * 初始化敏感词过滤器
     */
    public static void initSensitiveWordFilter()
    {
        List<String> words = new LinkedList<>();
        for (q_filterwordBean bean : GameDataManager.getInstance().q_filterwordContainer.getList())
        {
            String[] strings = bean.getQ_keys().split(",");
            words.addAll(Arrays.asList(strings));
        }
        SensitiveWordFilter.getInstance().initSensitiveWords(words);
    }


}
