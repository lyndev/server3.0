/**
 * @date 2014/10/17
 * @author ChenLong
 */
package chenlong.stopserver;

import chenlong.stopserver.config.ServerConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.http.HttpClientCodec;
import org.apache.mina.http.HttpRequestImpl;
import org.apache.mina.http.api.HttpEndOfContent;
import org.apache.mina.http.api.HttpMethod;
import org.apache.mina.http.api.HttpRequest;
import org.apache.mina.http.api.HttpResponse;
import org.apache.mina.http.api.HttpVersion;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.xml.sax.SAXException;

/**
 *
 * @author ChenLong
 */
public class Main {

    private final static Logger logger = Logger.getLogger(Main.class);
    private volatile IoSession session = null;

    public static void main(String[] args) {
        try {
            //BasicConfigurator.configure();
            new Main().start();
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    /**
     * 启动HTTP监听
     *
     * @param port
     * @return
     */
    public int start() throws ParserConfigurationException, SAXException, IOException {
        String filePath = SystemUtils.USER_DIR + File.separator + "dist" + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);

        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new HttpClientCodec());
        connector.setHandler(new HttpClientHandler());

        boolean connected = false;
        String ip = "localhost";
        int port = ServerConfig.getInstance().getHttpPort();
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

        if (!connected) {
            logger.error("StopServer cannot connect " + ip + ":" + port);
        }

        return 0;
    }

    public class HttpClientHandler extends IoHandlerAdapter {

        @Override
        public void sessionCreated(IoSession session) {
            logger.info("sessionCreated");
            session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120); // 空闲Http连接保持120秒
            Main.this.session = session;
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            logger.info("sessionClosed");
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            logger.info("sessionOpened");
            Map<String, String> headers = new HashMap<>();
            HttpRequest req = new HttpRequestImpl(HttpVersion.HTTP_1_1, HttpMethod.GET, "/", "command=stopserver", headers);
            session.write(req);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) {
            logger.info("sessionIdle idle " + session.getIdleCount(IdleStatus.BOTH_IDLE));
            session.close(true);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) {
            logger.info("exceptionCaught #" + session.getIdleCount(IdleStatus.BOTH_IDLE), cause);
            session.close(true);
            System.exit(1);
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            if (message instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) message;
                logger.info("HttpResponse Status: " + response.getStatus().toString());
            } else if (message instanceof IoBuffer) {
                IoBuffer buf = (IoBuffer) message;
                String content = buf.getString(Charset.forName("UTF-8").newDecoder());
                logger.info("HttpResponse Content: " + content);
            } else if (message instanceof HttpEndOfContent) {
                session.close(true);
                System.exit(0);
            } else {
                logger.error("unknow message: [" + message.getClass().getName() + "]");
            }
        }
    }
}
