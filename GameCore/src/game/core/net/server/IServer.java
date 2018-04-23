/**
 * @date 2014/3/18 15:00
 * @author ChenLong
 */
package game.core.net.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * @author ChenLong
 */
public interface IServer
{
    /**
     * 执行消息处理
     *
     * @param session
     * @param buf 消息包
     */
    void doCommand(IoSession session, IoBuffer buf);
    
    /**
     * 连接创建
     *
     * @param session
     */
    void sessionCreate(IoSession session);

    /**
     * 连接关闭
     *
     * @param session
     */
    void sessionClosed(IoSession session);
    
    /**
     * 
     * @param session
     * @param status 
     */
    public void sessionIdle(IoSession session, IdleStatus status);
    
    /**
     * 连接异常
     * @param session
     * @param cause 
     */
    void exceptionCaught(IoSession session, Throwable cause);

    /**
     * Codec工厂方法
     *
     * @return
     */
    ProtocolCodecFactory getCodecFactory();
}
