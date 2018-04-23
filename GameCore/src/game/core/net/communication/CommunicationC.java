/**
 * @date 2014/3/18 17:15
 * @author ChenLong
 */
package game.core.net.communication;

import game.core.net.server.IServer;
import java.net.InetSocketAddress;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * <b>Client端通信</b>
 *
 * @author ChenLong
 */
public class CommunicationC extends AbstractCommunication
{
    private final Logger log = Logger.getLogger(CommunicationC.class);
    private final NioSocketConnector connector = new NioSocketConnector();
    private boolean isInitialize;

    /**
     * Client端通信构造函数
     *
     * @param server 处理接口
     */
    public CommunicationC(IServer server)
    {
        super(server);
        isInitialize = false;
    }

    /**
     * Client端通信构造函数
     *
     * @param server 处理接口
     * @param maxConnectNum 支持的最大连接数
     */
    public CommunicationC(IServer server, int maxConnectNum)
    {
        super(server, maxConnectNum);
        isInitialize = false;
    }
    
    public synchronized void initialize()
    {   
        if (!isInitialize)
        {
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(super.server.getCodecFactory()));
            connector.setHandler(this);

            int recsize = 512 * 1024;
            int sendsize = 1024 * 1024;
            SocketSessionConfig sc = connector.getSessionConfig();
            sc.setReceiveBufferSize(recsize); // 设置输入缓冲区的大小
            sc.setSendBufferSize(sendsize); // 设置输出缓冲区的大小
            //sc.setTcpNoDelay(true);
            sc.setSoLinger(0);
            
            isInitialize = true;
        }

    }

    /**
     * 获取连接数
     *
     * @return
     */
    @Override
    public int getSessionCount()
    {
        return connector.getManagedSessionCount();
    }

    /**
     * 连接
     *
     * @param ip
     * @param port
     * @return
     */
    @Override
    public boolean connect(String ip, int port)
    {
        Validate.notNull(ip);
        Validate.notEmpty(ip);

        boolean connected = false;
        
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ip, port));
        connectFuture.awaitUninterruptibly(120 * 1000); // 最长两分钟等待
        if (connectFuture.isConnected())
        {
            log.info("connect success, ip: [" + ip + "], port = " + port);
            connected = true;
        }
        else
        {
            log.error("connect failure, ip: [" + ip + "], port = " + port);
            Throwable t = connectFuture.getException();
            if (t != null)
                log.error("connect exception, ip: [" + ip + "], port = " + port, t);
        }
        return connected;
    }
}
