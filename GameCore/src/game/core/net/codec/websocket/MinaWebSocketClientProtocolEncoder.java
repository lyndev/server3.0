/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec.websocket;

import game.core.message.SMessage;
import game.core.util.SessionUtils;
import java.util.Base64;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author Administrator
 */
public class MinaWebSocketClientProtocolEncoder extends DemuxingProtocolEncoder
{

    static final Logger LOG = Logger.getLogger(MinaWebSocketClientProtocolEncoder.class);

    private final int maxSize;

    /**
     * 通信编码器的构造函数.
     *
     * @param maxSize 设置每次向一个Session发送的最大字节数，超过将拒绝服务并断开连接
     */
    public MinaWebSocketClientProtocolEncoder(int maxSize)
    {
        this.maxSize = maxSize;
    }

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out)
            throws Exception
    {

        if (session.getScheduledWriteBytes() > maxSize)
        {
            SessionUtils.closeSession(session, "服务端准备发送的消息字节过长("+ session.getScheduledWriteBytes() + "bytes)");
        }
        
        // 握手连接
        if (obj instanceof MinaBean)
        {
            MinaBean message = (MinaBean) obj;
            byte[] _protocol = null;
            _protocol = message.getContent().getBytes("UTF-8");
            int length = _protocol.length;
            IoBuffer buffer = IoBuffer.allocate(length);
            buffer.put(_protocol);
            buffer.flip();
            out.write(buffer);
            LOG.info("握手连接 回复");
        }
        // 数据通信
        else if (obj instanceof SMessage)
        {
            SMessage msg = (SMessage) obj;
            
            String str = Base64.getEncoder().encodeToString(msg.getData());
                        
            byte[] _msg = WebSocketUtil.encode(str);    
            IoBuffer buff = IoBuffer.allocate(_msg.length);
            buff.put(_msg);
            buff.flip();
            out.write(buff);
        }
        else
        {
            LOG.error("Class type error! obj must be game.core.message.SMessage: " + obj.getClass().getName());
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
    }
}
