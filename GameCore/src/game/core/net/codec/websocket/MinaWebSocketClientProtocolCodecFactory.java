/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec.websocket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *
 * @author Administrator
 */
public class MinaWebSocketClientProtocolCodecFactory  implements ProtocolCodecFactory
{
   // 每次向一个Session发送的最大字节数，超过将拒绝服务并断开连接
    private int sendMaxSize = 1 * 1024 * 1024;

    // 每次从一个Session接收的最大字节数，超过将拒绝服务并断开连接
    private int receivedMaxSize = 10 * 1024;

    // 每秒最多处理同一个Session发送的多少条消息，超过将拒绝服务并断开连接
    private int receivedMaxCount = 30;
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception
    {
        return new MinaWebSocketClientProtocolDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception
    {
        return new MinaWebSocketClientProtocolEncoder(receivedMaxSize);
    }
}
