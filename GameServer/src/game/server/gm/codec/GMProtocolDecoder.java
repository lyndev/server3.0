package game.server.gm.codec;

import game.core.net.server.ServerContext;
import game.core.util.SessionUtils;
import java.nio.ByteOrder;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * <b>GM指令的通信解码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class GMProtocolDecoder implements ProtocolDecoder
{

    static final Logger LOG = Logger.getLogger(GMProtocolDecoder.class);

    private static final String CONTEXT = "CONTEXT";

    private static final String START_TIME = "START_TIME";

    private static final String RECEIVE_COUNT = "RECEIVE_COUNT";

    private final int maxSize, maxCount;

    /**
     * 通信解码器的构造函数.
     *
     * @param maxSize 设置每次从一个Session接收的最大字节数，超过将拒绝服务并断开连接
     * @param maxCount 设置每秒最多处理同一个Session发送的多少条消息，超过将拒绝服务并断开连接
     */
    public GMProtocolDecoder(int maxSize, int maxCount)
    {
        this.maxSize = maxSize;
        this.maxCount = maxCount;
    }

    @Override
    public void decode(IoSession session, IoBuffer buff,
            ProtocolDecoderOutput out) throws Exception
    {
        long startTime = 0;
        if (session.containsAttribute(START_TIME))
        {
            startTime = (Long) session.getAttribute(START_TIME);
        }

        int count = 0;
        if (session.containsAttribute(RECEIVE_COUNT))
        {
            count = (Integer) session.getAttribute(RECEIVE_COUNT);
        }

        if (System.currentTimeMillis() - startTime > 1000)
        {
            if (count > 10)
            {
                LOG.error(session + " --> sendmsg:" + count);
            }
            startTime = System.currentTimeMillis();
            count = 0;
        }

        count++;

        if (count > maxCount)
        {
            // 如果客户端在1秒之内发送了超过MAX_COUNT条消息, 则认为客户端在恶意刷包, 断开其连接
            LOG.error(session + " --> sendmsg:" + count + "-->close-->buffer:"
                    + buff.remaining() + "(" + buff + ")");
            SessionUtils.closeSession(session, "客户端发送的消息次数太频繁(" + count + ")");
            return;
        }
        else
        {
            session.setAttribute(START_TIME, startTime);
            session.setAttribute(RECEIVE_COUNT, count);
        }

        // 初始化服务器上下文
        ServerContext context = (ServerContext) session.getAttribute(CONTEXT);
        if (context == null)
        {
            context = new ServerContext();
            session.setAttribute(CONTEXT, context);
        }

        // 读取信息
        IoBuffer contextBuff = context.getBuffer();
        // 将接收到的信息添加到Context
        contextBuff.put(buff);

        do
        {
            contextBuff.flip();
            if (contextBuff.remaining() < Integer.SIZE / Byte.SIZE)
            {
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }

            contextBuff.order(ByteOrder.LITTLE_ENDIAN);
            // 获得信息长度
            int length = contextBuff.getInt();
            LOG.info(session + ":message length " + length + " bytes, buff remain " + contextBuff.remaining() + " bytes，max bytes " + maxSize);
            if (length > maxSize || length <= 0)
            {
                contextBuff.rewind();

                int remain = contextBuff.remaining();
                if (remain > 64)
                {
                    remain = 64;
                }
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < remain / 4; i++)
                {
                    str.append(" ").append(Integer.toHexString(contextBuff.getInt()));
                }
                SessionUtils.closeSession(session, "客户端发送的消息字节过长("
                        + length + "bytes), 消息前64字节为(" + str.toString() + ")");
                break;
            }

            int len = length - Integer.SIZE / Byte.SIZE; // 平台发送过来的包长包含了自身长度
//            System.out.println("len=" + len);
            if (contextBuff.remaining() < len)
            {
                contextBuff.rewind();
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }
            else
            {
                // 读取指定长度的字节数
                byte[] bytes = new byte[len];
                contextBuff.get(bytes);

                //写入指定长度的字节数
                out.write(bytes);

                if (contextBuff.remaining() == 0)
                {
                    contextBuff.clear();
                    break;
                }
                else
                {
                    contextBuff.compact();
                }
            }
        } while (true);

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

}
