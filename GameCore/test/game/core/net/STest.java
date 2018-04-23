/*
 * 2014/3/18 21:12 by ChenLong
 * Server端简单测试程序
 */
package game.core.net;

import game.core.net.codec.DefaultProtocolCodecFactory;
import game.core.net.communication.ICommunication;
import game.core.net.communication.CommunicationS;
import game.core.net.server.IServer;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

class TServer implements IServer
{
    private final Logger log = Logger.getLogger(TServer.class);

    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        System.out.println("msg from sessionId = " + session.getId() + "\t" + buf.toString());
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
        System.out.println("sessionId = " + session.getId() + " " + status.toString() + " , close");
        session.close(false);
    }

    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
        return new DefaultProtocolCodecFactory();
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
        cause.printStackTrace();
    }
}

/**
 *
 * @author ChenLong
 */
public class STest
{
    public static void main(String[] args)
    {
        new TServer().start(8989);
        System.out.println("STest started");
    }
}
