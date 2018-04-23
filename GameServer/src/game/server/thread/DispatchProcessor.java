/**
 * @date 2014/3/25 15:22
 * @author ChenLong
 */
package game.server.thread;

import game.core.command.CommandProcessor;
import game.core.command.Handler;
import game.core.command.ICommand;
import game.message.LoginMessage;
import game.message.LoginMessage.ReqReconnect;
import game.message.RoomMessage;
import game.server.http.HttpRequestHandler;
import game.server.logic.constant.CloseSocketReason;
import game.server.logic.constant.SessionKey;
import game.server.logic.line.GameLine;
import game.server.logic.line.GameLineManager;
import game.server.logic.login.service.LoginService;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.util.MessageNumber;
import game.server.util.MessageUtils;
import game.server.world.GameWorld;
import game.server.world.friend.FriendManager;
import game.server.world.room.RoomManager;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

/**
 * <b>命令分发线程</b>
 *
 * @author ChenLong
 */
public class DispatchProcessor extends CommandProcessor
{
    private final Logger log = Logger.getLogger(DispatchProcessor.class);

    private DispatchProcessor()
    {
        super(DispatchProcessor.class.getSimpleName());
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static DispatchProcessor getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 命令处理入口，这里并不执行命令，而是根据命令类型或属性为其分派对应的线程Processor.
     *
     * @param command 待处理的命令
     */
    @Override
    protected void doCommand(ICommand command)
    {
        if (command instanceof Handler)
        {
            Handler handler = (Handler) command;

            int msgId = handler.getMessage().getId();
            int msgFunction = MessageNumber.getFunction(msgId);
            switch (msgFunction)
            {
                case LoginService.LOGIN_FUNCTION: // 登录相关功能
                {
                    doLogin(handler);
                    break;
                }
                case FriendManager.FRIEND_FUNCTION: // 好友
                {
                    doDispatch(handler);
                    break;
                }
                case RoomManager.ROOM_FUNCTION: // 战斗房间
                {
                    doFightRoom(handler);
                    break;  
                }
                default: // 其他功能暂时投递到主逻辑线程池处理
                {
                    doOther(handler);
                    break;
                }
            }
        }
        else if (command instanceof HttpRequestHandler) // http消息
        {
            HttpProcessor.getInstance().addCommand(command);
        }
        else
        {
            log.error("不支持的命令类型！command class: " + command.getClass().getName());
        }
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

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;
        DispatchProcessor processor;

        Singleton()
        {
            this.processor = new DispatchProcessor();
        }

        DispatchProcessor getProcessor()
        {
            return processor;
        }
    }

    /**
     * 登录相关消息预处理
     *
     * @param handler
     */
    private void doLogin(Handler handler)
    {
        Validate.notNull(handler);
        Validate.notNull(handler.getMessage().getSession());

        int lineId = 0;
        boolean availableLineId = false;
        int msgId = handler.getMessage().getId();
        IoSession session = handler.getMessage().getSession();

        switch (msgId)
        {
            case LoginMessage.ReqLogin.MsgID.eMsgID_VALUE: // 登录
            {
                Object data = handler.getMessage().getData();
                if (data instanceof LoginMessage.ReqLogin)
                {
                    // 唯一id
                    LoginMessage.ReqLogin reqMsg = (LoginMessage.ReqLogin) data;
                    GameLine gameLine = null;
                    if (reqMsg.hasSoleId())
                    {
                        gameLine = GameLineManager.getInstance().chooseLineBySoleid(reqMsg.getSoleId());

                    }
                    else
                    {
                       //没有发送唯一id，服务器判断为游客
                        gameLine =  GameLineManager.getInstance().randomLine();
                    }
                    lineId = gameLine.getLineId();
                    availableLineId = true;
                    session.setAttribute(SessionKey.GAME_LINE.name(), gameLine);
                }
                else
                {
                    log.error("data not instanceof LoginMessage.ReqLogin");
                }
                break;
            }
            case LoginMessage.ReqCreateRole.MsgID.eMsgID_VALUE: // 创建角色
            {
                Object valObj = session.getAttribute(SessionKey.GAME_LINE.name());
                if (valObj != null && valObj instanceof GameLine)
                {
                    lineId = ((GameLine) valObj).getLineId();
                    availableLineId = true;
                }
                else
                {
                    log.error("SESSION_LINE_ID invaliable");
                }
                break;
            }
            case LoginMessage.ReqReconnect.MsgID.eMsgID_VALUE: // 断线重连
            {
                Object msgData = handler.getMessage().getData();
                if (msgData != null && msgData instanceof ReqReconnect)
                {
                    ReqReconnect reqMsg = (ReqReconnect) msgData;
                    Player player = PlayerManager.getPlayerByUserId(reqMsg.getUserId());
                    if (player != null)
                    {
                        lineId = player.getLineId();
                        availableLineId = true;
                    }
                }
                break;
            }
            case LoginMessage.ResPingPong.MsgID.eMsgID_VALUE: // PingPong测试
            {
                doOther(handler);
                return;
            }
            default:
            {
                log.error("unknow msg, msgId = " + msgId);
                break;
            }
        }
        if (availableLineId)
        {
            GameLineManager.getInstance().addCommand(lineId, handler);
        }
        else
        {
            log.error("msgId = " + msgId + " has not available lineId");
            WriteFuture future = MessageUtils.closeSocket(session, CloseSocketReason.ACCOUNT_ABNORMAL);
        }
    }

    private void doFightRoom(Handler handler){
       int lineId = 0;
        boolean availableLineId = false;
        int msgId = handler.getMessage().getId();
        Player player = null;
        switch (msgId)
        {
            // 进入房间确认
            case RoomMessage.ReqUDPEnterRoom.MsgID.eMsgID_VALUE:
            {
                Object msgData = handler.getMessage().getData();
                if (msgData != null)
                {
                    RoomMessage.ReqUDPEnterRoom reqMsg = (RoomMessage.ReqUDPEnterRoom) msgData;
                    player = PlayerManager.getPlayerByUserId(reqMsg.getUserId());
                    if (player != null)
                    {
                        lineId = player.getLineId();
                        availableLineId = true;
                    }
                }

                break;
            }
            
            // 移动同步
            case RoomMessage.ReqMoveKeyData.MsgID.eMsgID_VALUE:{
                Object msgData = handler.getMessage().getData();
                if (msgData != null)
                {
                    RoomMessage.ReqMoveKeyData reqMsg = (RoomMessage.ReqMoveKeyData) msgData;
                    player = PlayerManager.getPlayerByUserId(reqMsg.getUserId());
                    if (player != null)
                    {
                        lineId = player.getLineId();
                        availableLineId = true;
                    }
                }
                break;
            }
        }
        if(player != null){
            handler.setAttribute("player", player);
            GameLineManager.getInstance().addCommand(player.getLineId(), handler);
        } else {
            log.error("处理战斗房间的udp消息分发失败。");
        }
    }
    
    /**
     * 游戏正常逻辑消息
     *
     * @param handler
     */
    private void doOther(Handler handler)
    {
        // 游戏逻辑消息
        int msgId = handler.getMessage().getId();
        
        String _classTypeName = handler.getMessage().getSession().getClass().getName();
        
         Player player = null;
        // udp报文数据
        if("NioDatagramSession".equals(_classTypeName)){
           // player = PlayerManager.getPlayerByUserId(handler.getMessage().getSession());
        } else {
            player = PlayerManager.getPlayerBySession(handler.getMessage().getSession());
        }
        
        if (player == null)
        {
            // 玩家没有登录或者会话已过期
            MessageUtils.notLogin(handler.getMessage().getSession(), msgId);
            return;
        }
        handler.setAttribute("player", player);
        GameLineManager.getInstance().addCommand(player.getLineId(), handler);
    }

    /**
     * world消息分发
     *
     * @param handler
     */
    private void doDispatch(Handler handler)
    {
        int msgId = handler.getMessage().getId();
        int source = MessageNumber.getSource(msgId);
        switch (source)
        {
            case MessageNumber.MESSAGE_SOURCE_CS:
            {
                doOther(handler);
                break;
            }
            case MessageNumber.MESSAGE_SOURCE_CW:
            {
                doGameWorld(handler);
                break;
            }
            default:
            {
                doOther(handler);
                break;
            }
        }
    }

    /**
     * 到GameWorld处理的消息
     *
     * @param handler
     */
    private void doGameWorld(Handler handler)
    {
        int msgId = handler.getMessage().getId();
        Player player = PlayerManager.getPlayerBySession(handler.getMessage().getSession());
        if (player == null)
        {
            // 玩家没有登录或者会话已过期
            MessageUtils.notLogin(handler.getMessage().getSession(), msgId);
            return;
        }
        handler.setAttribute("userId", player.getUserId());
        handler.setAttribute("roleId", player.getRoleId());
        GameWorld.getInstance().addCommand(handler);
    }
}
