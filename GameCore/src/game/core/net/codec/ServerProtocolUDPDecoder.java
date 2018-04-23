package game.core.net.codec;

import game.core.net.server.ServerContext;
import game.core.util.SessionUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 
 * <b>通信解码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 * 
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ServerProtocolUDPDecoder implements ProtocolDecoder
{

    static final Logger LOG = Logger.getLogger(ServerProtocolDecoder.class);

    private static final String CONTEXT = "CONTEXT";

    private static final String START_TIME = "START_TIME";

    private static final String RECEIVE_COUNT = "RECEIVE_COUNT";
    
    private static final List<Integer> ignoreMessage = new ArrayList<>();
    
    private final int maxSize, maxCount;
   
    /**
     * 通信解码器的构造函数.
     *
     * @param maxSize 设置每次从一个Session接收的最大字节数，超过将拒绝服务并断开连接
     * @param maxCount 设置每秒最多处理同一个Session发送的多少条消息，超过将拒绝服务并断开连接
     */
    public ServerProtocolUDPDecoder(int maxSize, int maxCount)
    {
       this.maxSize = maxSize;
       this.maxCount = maxCount;
    }

    @Override
    public void decode(IoSession session, IoBuffer buff,
            ProtocolDecoderOutput out) throws Exception
    {
        LOG.info("call");
        do
        {
            byte[] _data = buff.array();
            LOG.info("收到客户端的udp数据长度为："+ _data.length + "int:" + buff.getInt());
            break;
        }while(true);
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
        if (session.getAttribute(CONTEXT) != null)
        {
            session.removeAttribute(CONTEXT);
        }
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception
    {

    }

    private int peekInt32(int beg, byte[] bytes)
    {
        return (int)((bytes[0 + beg] & 0xFF) << 24)   | 
               (int)((bytes[beg + 1] & 0xFF) << 16)   |
               (int)((bytes[beg + 2] & 0xFF) <<  8)   |
               (int)((bytes[beg + 3] & 0xFF));
    }

}
