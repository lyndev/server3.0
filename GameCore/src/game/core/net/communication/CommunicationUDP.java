/**
 * @date 2014/3/18 15:40
 * @author ChenLong
 */
package game.core.net.communication;

import game.core.net.server.IServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

/**
 * <b>Server端通信</b>
 * 
 * @author ChenLong
 */
public class CommunicationUDP extends AbstractCommunication
{
    private final Logger log = Logger.getLogger(CommunicationS.class);
    private NioDatagramAcceptor acceptor;
    private int idleTime;

    /**
     * Server端通信构造函数
     *
     * @param server 处理接口
     * @param maxConnectNum 支持的最大连接数
     * @param idleTime 心跳时间，单位秒，心跳超时将回调sessionIdle
     */
    public CommunicationUDP(IServer server, int maxConnectNum, int idleTime)
    {
        super(server, maxConnectNum);
        this.idleTime = idleTime;
    }

    /**
     * Server端通信构造函数
     *
     * @param server 处理接口
     * @param maxConnectNum 支持的最大连接数
     */
    public CommunicationUDP(IServer server, int maxConnectNum)
    {
        this(server, maxConnectNum, 0);
    }

    /**
     * 获取连接数
     *
     * @return
     */
    @Override
    public int getSessionCount() {
        return acceptor.getManagedSessionCount();
    }
    
    /**
     * 启动Server监听
     *
     * @param port 监听端口
     * @return
     */
    @Override
    public int accept(int port)
    {
        acceptor = new NioDatagramAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("codec", new ProtocolCodecFilter(server.getCodecFactory())); // 添加数据编解码器
        // 添加有序线程池，以保证消息按先后顺序到达
        OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor(500);
        chain.addLast("threadPool", new ExecutorFilter(threadpool));
        acceptor.setHandler(this);
        try
        {
            acceptor.bind(new InetSocketAddress(port));//绑定端口
        }
        catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(CommunicationUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int recsize = 5120;
        int sendsize = 20480;
        int readSize = 4096;
        DatagramSessionConfig sc = acceptor.getSessionConfig();
        sc.setReadBufferSize(readSize);//设置接收最大字节默认2048
        sc.setReuseAddress(true); // 设置每一个非主监听连接的端口可以重用
        sc.setReceiveBufferSize(recsize); // 设置输入缓冲区的大小
        sc.setSendBufferSize(sendsize); // 设置输出缓冲区的大小
        
        return 0;
    }
    
    public void unaccept()
    {
        acceptor.unbind();
    }
}
