/*
 * 2014/3/20 14:07 by ChenLong
 * 连接、流量测试程序
 * 功能：建立connectNum个连接，每隔interval毫秒发送长度为msgLength的数据包
 */
package game.core.net;

import game.core.net.codec.DefaultProtocolCodecFactory;
import game.core.net.communication.CommunicationC;
import game.core.net.communication.ICommunication;
import game.core.net.server.IServer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

class TMultiClient implements IServer
{
    private final Map<Long, IoSession> sessions = new ConcurrentHashMap<>();
    ICommunication connector = null;

    TMultiClient()
    {
        connector = new CommunicationC(this, 20000);
    }

    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        System.out.println("msg from sessionId = " + session.getId() + "[" + buf.toString() + "]");
    }

    @Override
    public void sessionCreate(IoSession session)
    {
        sessions.put(session.getId(), session);
        System.out.println("sessionId = " + session.getId());
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
        return new DefaultProtocolCodecFactory();
    }

    private void closeSession()
    {
        Iterator iter = sessions.entrySet().iterator();
        while (iter.hasNext())
        {
            Entry entry = (Entry) iter.next();
            IoSession session = (IoSession) entry.getValue();
            session.close(false);
        }
    }

    class CloseHook implements Runnable
    {
        @Override
        public void run()
        {
            System.out.println("on CloseHook");
            closeSession();
        }
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
            for (int i = 0; i < connectNum; ++i)
                connector.connect(ip, 8989);
        }
    }

    class SendWork extends TimerTask
    {
        private int msgLength;
        private int interval;
        private byte[] bytes;

        SendWork(int msgLength, int interval)
        {
            constructMsg(msgLength, interval);
        }

        private void constructMsg(int msgLength, int interval)
        {
            this.msgLength = msgLength;
            this.interval = interval;

            IoBuffer buffer = IoBuffer.allocate(1024);
            buffer.setAutoExpand(true);
            buffer.setAutoShrink(true);
            buffer.putInt(this.msgLength);
            for (int i = 0; i < this.msgLength; ++i)
                buffer.put((byte) 8);
            buffer.flip();
            bytes = new byte[Integer.SIZE / Byte.SIZE + this.msgLength];
            buffer.get(bytes);
        }

        @Override
        public void run()
        {

            List<IoSession> sessionList = new LinkedList<>();
            synchronized (sessions)
            {
                sessionList.addAll(sessions.values());
            }
            for (IoSession session : sessionList)
            {
                IoBuffer buffer = IoBuffer.allocate(1024);
                buffer.setAutoExpand(true);
                buffer.setAutoShrink(true);
                buffer.put(bytes);
                buffer.flip();
                connector.send(session, buffer);
            }
        }
    }

    /**
     * 建立connectNum个连接，每隔interval毫秒发送长度为msgLength的数据包
     * @param ip
     * @param port
     * @param connectNum 连接个数
     * @param msgLength 发送数据包长度
     * @param interval 发送间隔，单位毫秒
     */
    public void start(String ip, int port, int connectNum, int msgLength, int interval)
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new CloseHook())); // JVM关闭回调
        new Thread(new ConnectWork(ip, port, connectNum)).start();
        new Timer().schedule(new SendWork(msgLength, interval), 0, interval);
    }
}

/**
 *
 * @author ChenLong
 */
public class MultiConnectTest
{
    public static void main(String[] args)
    {
        new TMultiClient().start("192.168.1.11", 8989, 2000, 3 * 1024, 1000); // 2000个连接，每秒发送3k数据 4MB/s
        //new TMultiClient().start("192.168.1.11", 8989, 1, 3 * 1024, 1000); // 2000个连接，每秒发送3k数据 4KB/s
        //new TMultiClient().start("192.168.1.11", 8989, 10000, 800, 1000); // 10000个连接，每秒发送800字节数据 7.6MB/s
        //new TMultiClient().start("127.0.0.1", 8989, 2, 2*1024, 1000); // 3000个连接，每秒发送2k数据
        //new TMultiClient().start("192.168.20.55", 8989, 1, 3 * 1024, 1000); // 2000个连接，每秒发送3k数据 4KB/s
    }
}
