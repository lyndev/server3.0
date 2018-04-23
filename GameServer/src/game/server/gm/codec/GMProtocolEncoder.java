package game.server.gm.codec;

import game.core.util.SessionUtils;
import game.server.gm.GMCommandMessage;
import java.nio.ByteOrder;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * <b>GM指令的通信编码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class GMProtocolEncoder implements ProtocolEncoder
{

    static final Logger LOG = Logger.getLogger(GMProtocolEncoder.class);

    private final int maxSize;

    /**
     * 通信编码器的构造函数.
     *
     * @param maxSize 设置每次向一个Session发送的最大字节数，超过将拒绝服务并断开连接
     */
    public GMProtocolEncoder(int maxSize)
    {
        this.maxSize = maxSize;
    }

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out)
            throws Exception
    {
        if (session.getScheduledWriteBytes() > maxSize)
        {
            SessionUtils.closeSession(session, "服务端准备发送的消息字节过长("
                    + session.getScheduledWriteBytes() + "bytes)");
        }

        if (obj instanceof GMCommandMessage)
        {
            GMCommandMessage msg = (GMCommandMessage) obj;
            IoBuffer buff = IoBuffer.allocate(1024);
            buff.order(ByteOrder.LITTLE_ENDIAN);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);

            // 4字节包长 + 2字节加密标识 + 2字节json长度 + json字符串
            buff.putInt(0);
            buff.putShort(msg.getSecret());

            byte[] data = msg.getData().toJSONString().getBytes();
            buff.putShort((short) data.length);
            buff.put(data);

            buff.flip();
            buff.putInt(buff.limit()); // 发送给平台的包长包含自身长度
            buff.rewind();

            out.write(buff);
        }
        else
        {
            LOG.error("Class type error! obj must be game.server.gm.GMCommandMessage: " + obj.getClass().getName());
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
    }

}
