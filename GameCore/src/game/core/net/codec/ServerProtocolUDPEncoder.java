package game.core.net.codec;

import game.core.message.SMessage;
import game.core.util.SessionUtils;
import game.core.util.ZLibUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * <b>通信编码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ServerProtocolUDPEncoder implements ProtocolEncoder
{

    static final Logger LOG = Logger.getLogger(ServerProtocolEncoder.class);

    private final int maxSize;

    private boolean bU3dProject = false;

    /**
     * 通信编码器的构造函数.
     *
     * @param maxSize 设置每次向一个Session发送的最大字节数，超过将拒绝服务并断开连接
     */
    public ServerProtocolUDPEncoder(int maxSize)
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

        if (obj instanceof SMessage)
        {
            SMessage msg = (SMessage) obj;
            IoBuffer buff = IoBuffer.allocate(1024);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);

            // 是否压缩
            boolean zip = false;
            buff.putInt(0);
            buff.putInt(msg.getId());
            if (msg.getData().length > 512)
            {
                byte[] zipBytes = ZLibUtils.compress(msg.getData());
                if (bU3dProject)
                {
                    // uniyt3d项目这里加了长度，cocos2d这里不需要长度
                    buff.putInt(zipBytes.length);
                }
                buff.put(zipBytes);
                zip = true;
            }
            else
            {
                if (bU3dProject)
                {
                    // uniyt3d项目这里加了长度，cocos2d这里不需要长度
                    buff.putInt(msg.getData().length);
                }
                buff.put(msg.getData());
            }

            buff.flip();
            if (zip)
            {
                buff.putInt(((buff.limit() - Integer.SIZE / Byte.SIZE) | (((int) 1) << 24)));
            }
            else
            {

                buff.putInt(buff.limit() - Integer.SIZE / Byte.SIZE);
            }
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
