package game.testrobot.utils;

import game.core.command.Handler;
import game.core.message.SMessage;
import game.testrobot.line.LineManager;
import game.testrobot.robot.RobotPlayer;
import org.apache.log4j.Logger;
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

    /**
     * 向指定玩家发送消息.
     *
     * @param player 玩家对象
     * @param message 消息对象
     */
    public static void send(RobotPlayer player, SMessage message)
    {
        send(player.getSession(), message);
    }

    /**
     * 向指定会话发送消息.
     *
     * @param session 会话对象
     * @param message 消息对象
     */
    public static void send(IoSession session, SMessage message)
    {
        if (session != null)
            session.write(message);
        else
            LOG.debug("session is null");
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
            LineManager.getInstance().addCommand(lineId, handler);
        }
        else
        {
            LOG.error("lineId is null！handler：" + handler.getClass());
        }
    }
}
