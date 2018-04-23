package game.core.net.codec;

import game.core.message.SMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import org.apache.log4j.Logger;

/**
 *
 * <b>服务器内部通信使用的编码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class InnerProtocolEncoder implements ProtocolEncoder
{

    static final Logger LOG = Logger.getLogger(InnerProtocolEncoder.class);

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out)
            throws Exception
    {
        if (obj instanceof SMessage)
        {
            SMessage msg = (SMessage) obj;
            IoBuffer buff = IoBuffer.allocate(256);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);
            buff.putInt(0);
            buff.putInt(msg.getId());
            buff.putLong(msg.getSender());
            buff.put(msg.getData());
            buff.flip();
            buff.putInt(buff.limit() - Integer.SIZE / Byte.SIZE);
            buff.rewind();

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
