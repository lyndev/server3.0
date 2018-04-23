/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.communication;

import game.core.net.codec.DefaultProtocolCodecFactory;
import game.core.net.codec.websocket.MinaBean;
import game.core.net.server.IServer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * @author Administrator
 */
public class WebSocketServer implements IServer
{

    private IoSession session = null;

    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        System.out.println("msg from sessionId = " + session.getId() + "[" + buf.toString() + "]");
    }
            
    @Override
    public void sessionCreate(IoSession session)
    {
        this.session = session;
        System.out.println("sessionId = " + session.getId());
    }

    @Override
    public void sessionClosed(IoSession session)
    {
        System.out.println("sessionId = " + session.getId());
    }

    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
        return new DefaultProtocolCodecFactory();
    }

    public void start(int port)
    {
        ICommunication connector = new CommunicationC(this);
        connector.connect("127.0.0.1", 8989);
        int count = 128;
        IoBuffer buffer = IoBuffer.allocate(1024);
        buffer.putInt(count);
        for (int i = 0; i < count; ++i)
            buffer.put((byte) 8);
        buffer.flip();
        byte[] bytes = new byte[Integer.SIZE / Byte.SIZE + count];
        buffer.get(bytes);

        // 发送数据
        for (int i = 0; i < 6; ++i)
        {
            IoBuffer b = IoBuffer.allocate(256);
            b.put(bytes);
            b.flip();
            connector.send(session, b);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        cause.printStackTrace();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
        session.close(true);
    } 
}
