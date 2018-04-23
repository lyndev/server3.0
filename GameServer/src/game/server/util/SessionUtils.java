/**
 * @date 2014/4/25
 * @author ChenLong
 */
package game.server.util;

import game.server.logic.constant.SessionKey;
import game.server.logic.line.GameLine;
import game.server.logic.line.GameLineManager;
import game.server.logic.struct.Player;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class SessionUtils extends game.core.util.SessionUtils
{
    private static final Logger log = Logger.getLogger(SessionUtils.class);

    /**
     * 复制IoSession中所有以"GAME_"开头的Attribute
     *
     * @param fromSession
     * @param toSession
     */
    public static void copyAllGameAttributes(IoSession fromSession, IoSession toSession)
    {
        Validate.notNull(fromSession);
        Validate.notNull(toSession);
        for (SessionKey key : SessionKey.values())
        {
            if (key.needCopy)
            {
                Object objVal = fromSession.getAttribute(key.name());
                if (objVal != null)
                {
                    toSession.setAttribute(key.name(), objVal);
                }
                else
                {
                    log.error("cannot find attribute key: [" + key.name() + "]");
                }
            }
        }
    }

    public static void removeAllGameAttributes(IoSession session)
    {
        Validate.notNull(session);
        for (SessionKey key : SessionKey.values())
        {
            session.removeAttribute(key.name());
        }
    }

    /**
     * 获取所有以"GAME_"开头的Key-Attribute字符串
     *
     * @param session
     * @return
     */
    public static String allGameAttributesToString(IoSession session)
    {
        Validate.notNull(session);
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (SessionKey key : SessionKey.values())
        {
            Object objVal = session.getAttribute(key.name());
            if (objVal != null)
                sb.append(key.name()).append("=").append(objVal.toString()).append(",");
            else
                sb.append(key.name()).append("=").append("NULL").append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',')
            sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public static void fillInGameAttributes(IoSession session, Player player)
    {
        Validate.notNull(session);
        Validate.notNull(player);
        for (SessionKey key : SessionKey.values())
        {
            switch (key)
            {
                case GAME_LINE:
                    GameLine gameLine = GameLineManager.getInstance().getLine(player.getLineId());
                    if (gameLine != null)
                        session.setAttribute(key.name(), gameLine);
                    else
                        log.error("cannot get GameLine, lineId = " + player.getLineId());
                    break;
                case PLATFORM_ID:
                    session.setAttribute(key.name(), player.getPlatformId());
                    break;
                case SERVER_ID:
                    session.setAttribute(key.name(), player.getServerId());
                    break;
                case USER_NAME:
                    session.setAttribute(key.name(), player.getUserName());
                    break;
                case USER_ID:
                    session.setAttribute(key.name(), player.getUserId());
                    break;
                case LOGIN_TYPE:
                    break;
                case LOGIN_MSG_PROC:
                    break;
                case FGI:
                    session.setAttribute(key.name(), player.getFgi());
                    break;
                default:
                    log.warn("not case key: [" + key.name() + "]");
                    break;
            }
        }
    }

    /**
     * 获取对端IP
     *
     * @param session
     * @return
     */
    public static String getPeerIp(IoSession session)
    {
        String ip = StringUtils.EMPTY;
        if (session != null)
        {
            SocketAddress addr = session.getRemoteAddress();
            if (addr != null && addr instanceof InetSocketAddress)
            {
                InetSocketAddress inetAddr = (InetSocketAddress) addr;
                ip = inetAddr.getAddress().getHostAddress();
            }
        }
        return ip;
    }
}
