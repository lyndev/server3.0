package game.core.util;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * <b>MINA IoSession工具类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class SessionUtils
{

//    private static final Logger LOG = Logger.getLogger("GATESESSIONCLOSE");
    private static final Logger LOG = Logger.getLogger(SessionUtils.class);

    public static void closeSession(IoSession session, String reason)
    {
        LOG.error(session + "-->close [because] " + reason);
        session.close(true);
    }

    public static void closeSession(IoSession session, String reason, boolean force)
    {
        LOG.error(session + "-->close [because] " + reason);
        session.close(force);
    }

}
