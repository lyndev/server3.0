/**
 * @date 2014/9/2
 * @author ChenLong
 */
package chenlong.gmtesttool.function.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import chenlong.gmtesttool.function.codec.GMProtocolCodecFactory;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author ChenLong
 */
public class GMClient {

    private final static Logger logger = Logger.getLogger(GMClient.class);
    private final static GMClient instance;
    private final NioSocketConnector connector = new NioSocketConnector();
    private boolean isInitialize;
    private IoSession session = null;
    private INotify notify = null;

    static {
        instance = new GMClient();
    }

    public static GMClient getInstance() {
        return instance;
    }

    public boolean connect(String ip, int port) {
        Validate.notNull(ip);
        Validate.notEmpty(ip);
        boolean connected = isConnected();
        if (!connected) {
            initialize();
            connected = false;
            ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ip, port));
            connectFuture.awaitUninterruptibly(120 * 1000); // 最长两分钟等待
            if (connectFuture.isConnected()) {
                logger.info("connect success, ip: [" + ip + "], port = " + port);
                connected = true;
            } else {
                logger.error("connect failure, ip: [" + ip + "], port = " + port);
                Throwable t = connectFuture.getException();
                if (t != null) {
                    logger.error("connect exception, ip: [" + ip + "], port = " + port, t);
                }
            }
        }
        return connected;
    }

    public boolean send(String jsonStr) {
        boolean success = false;
        if (isConnected()) {

            JSONObject json = JSON.parseObject(jsonStr);
            String jsonData = json.toJSONString();

            byte[] data = jsonData.getBytes(Charset.forName("UTF-8"));
            // 4字节包长 + 2字节加密标识 + 2字节json长度 + json字符串
            IoBuffer buffer = IoBuffer.allocate(data.length + 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(data.length + 8);
            buffer.putShort((short) 889);
            buffer.putShort((short) data.length);
            buffer.put(data);
            buffer.flip();

            session.write(buffer);
        }
        return success;
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    public IoSession getSession() {
        return session;
    }

    private void setSession(IoSession session) {
        this.session = session;
        if (session != null && session.isConnected()) {
            notify.connected();
        } else {
            notify.disconnected();
        }
    }

    public void setNotify(INotify connectNotify) {
        this.notify = connectNotify;
    }

    private synchronized void initialize() {
        if (!isInitialize) {
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new GMProtocolCodecFactory()));
            connector.setHandler(new ClientIOHandler());
            int recsize = 512 * 1024;
            int sendsize = 1024 * 1024;
            SocketSessionConfig sc = connector.getSessionConfig();
            sc.setReceiveBufferSize(recsize); // 设置输入缓冲区的大小
            sc.setSendBufferSize(sendsize); // 设置输出缓冲区的大小
            //sc.setTcpNoDelay(true);
            sc.setSoLinger(0);
            isInitialize = true;
        }
    }

    private void doCommand(IoSession session, IoBuffer buf) {
        // 读取加密标识（暂不做解密处理）
        short secret = buf.getShort();
        // 读取消息长度（无聊至极的设计）
        short dataLen = buf.getShort();
        // 读取消息内容
        byte[] msgData = new byte[buf.remaining()];
        buf.get(msgData);

        // 创建消息对象
        JSONObject json;
        try {
            json = JSONObject.parseObject(new String(msgData));
        } catch (Exception e) {
            throw new RuntimeException("GMCommand parsing failed! cause:", e);
        }

        notify.received(json.toJSONString());
    }

    public class ClientIOHandler extends IoHandlerAdapter {

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
            logger.info("messageSent sessionId = " + session.getId());
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            super.messageReceived(session, message);
            logger.info("messageReceived sessionId = " + session.getId());
            if (message instanceof byte[]) {
                byte[] bytes = (byte[]) message;
                IoBuffer buf = IoBuffer.allocate(bytes.length);
                buf.put(bytes);
                buf.flip();
                doCommand(session, buf);
            } else if (message instanceof IoBuffer) {
                doCommand(session, (IoBuffer) message);
            } else {
                logger.error("message instanceof " + message.getClass().getName());
            }
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            super.exceptionCaught(session, cause);
            logger.error("exceptionCaught sessionId = " + session.getId() + ", cause: " + cause.getMessage());
            session.close(true);
            setSession(null);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
            logger.info("sessionClosed sessionId = " + session.getId());
            setSession(null);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            logger.info("sessionOpened sessionId = " + session.getId());
            setSession(session);
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
            logger.info("sessionCreated sessionId = " + session.getId());
            setSession(session);
        }
    }
}
