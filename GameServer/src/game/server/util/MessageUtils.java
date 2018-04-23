package game.server.util;

import com.google.protobuf.ByteString;
import game.core.command.Handler;
import game.core.message.SMessage;
import game.message.BaseMessage;
import game.message.LoginMessage;
import game.message.NotifyMessage;
import game.server.config.ServerConfig;
import game.server.config.ServerType;
import game.server.fight.FightConnector;
import game.server.logic.constant.CloseSocketReason;
import game.server.logic.constant.NoticeLevel;
import game.server.logic.line.GameLineManager;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

/**
 *
 * <b>消息工具类.</b>
 * <p>
 * 服务端发送消息的方法都写在这里，包括之后要做的群聊，跨服等.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class MessageUtils
{
    private final static Logger LOG = Logger.getLogger(MessageUtils.class);
    private final static boolean EnableWebsocket = false;

    /**
     * 向指定玩家发送消息.
     *
     * @param player 玩家对象
     * @param message 消息对象
     */
    public static void send(Player player, SMessage message)
    {
        send(player.getSession(), message);
    }

    /**
     * 向指定会话发送消息.
     *
     * @param session 会话对象
     * @param message 消息对象
     * @return
     */
    public static WriteFuture send(IoSession session, SMessage message)
    {
        Validate.notNull(message);
        WriteFuture future = null;
        
        // 不同的游戏服务器组装的消息体格式不一样
        int _serverType = ServerConfig.getInstance().getServerType();
        if (session != null)
        {
            switch (ServerType.getTypeByValue(_serverType))
            {
                case CREATOR_SERVER:
                    BaseMessage.RpcData.Builder rpcData = BaseMessage.RpcData.newBuilder();
                    rpcData.setMsgId(message.getId());
                    rpcData.setSerializedMsgData(ByteString.copyFrom(message.getData()));
                    BaseMessage.Rpc.Builder rpc = BaseMessage.Rpc.newBuilder();
                    rpc.setRpcData(rpcData.build());
                    message = new SMessage(message.getId(), rpc.build().toByteArray());
                    LOG.info("发送CREATOR_SERVER 的 websocket消息。");
                    break;
                case COCOS2D_SERVER:
                    LOG.info("发送COCOS2D_SERVER 的 socket消息。");
                    break;
                case UNITY3D_SERVER:
                    LOG.info("发送UNITY3D_SERVER 的 socket消息。");
                    break;
            }
            System.err.println("========================== server response:"+ message.getId() + "==========================");
            future = session.write(message);
        }
        else
        {
            LOG.debug("session is null");
        }

        return future;
    }

    /**
     * 玩家没有登录或者会话已过期的消息处理.
     * <p>
     * 先发送notLogin消息，再掐断客户端连接，故调用后必须退出当前逻辑.
     *
     * @param session 会话对象
     * @param msgId 本次请求的消息ID
     */
    public static void notLogin(IoSession session, int msgId)
    {
        send(session, new SMessage(
                LoginMessage.ResNotLogin.MsgID.eMsgID_VALUE,
                LoginMessage.ResNotLogin.newBuilder().build().toByteArray()));
        SessionUtils.closeSession(session, "玩家没有登录或者会话已过期！msgId：" + msgId);
    }

    /**
     * 发送消息到GameWorld
     *
     * @param handler
     */
    public static void sendToGameWorld(Handler handler)
    {
        GameWorld.getInstance().addCommand(handler);
    }

    /**
     * 发送消息到GameLine
     *
     * @param lineId
     * @param handler
     */
    public static void sendToGameLine(Integer lineId, Handler handler)
    {
        if (lineId != null)
        {
            GameLineManager.getInstance().addCommand(lineId, handler);
        }
        else
        {
            LOG.error("lineId is null！handler：" + handler.getClass());
        }

    }

    /**
     * 给战斗服务器发送消息
     *
     * @param message
     */
    public static void sendToFightServer(SMessage message)
    {
        FightConnector.getSingleton().send(message);
    }

    /**
     * 对单个玩家发送提示消息
     *
     * @param session
     * @param type
     * @param message
     */
    public static void noticePlayer(IoSession session, NoticeLevel type, String message)
    {
        NotifyMessage.ResNotifyPlayer.Builder builder = NotifyMessage.ResNotifyPlayer.newBuilder();
        builder.setNotifyType(type.value());
        builder.setContext(message);
        send(session, new SMessage(NotifyMessage.ResNotifyPlayer.MsgID.eMsgID_VALUE, builder.build().toByteArray()));
    }

    /**
     * 主动关闭连接, 如: 被顶号
     *
     * @param session
     * @param reason
     */
    public static WriteFuture closeSocket(IoSession session, CloseSocketReason reason)
    {
        LoginMessage.ResCloseSocket.Builder resMsg = LoginMessage.ResCloseSocket.newBuilder();
        resMsg.setReason(reason.getValue());
        return MessageUtils.send(session, new SMessage(LoginMessage.ResCloseSocket.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }
}
