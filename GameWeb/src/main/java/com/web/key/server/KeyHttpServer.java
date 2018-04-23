/**
 * @author ChenLong
 */
package com.web.key.server;

//import game.server.thread.DispatchProcessor;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.http.HttpRequestImpl;
import org.apache.mina.http.HttpServerCodec;
import org.apache.mina.http.api.HttpEndOfContent;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 内部Http服务器 用于和后台通信, 目前暂支持Http GET方法
 *
 * @author xu
 */
public class KeyHttpServer {
	private final Logger log = Logger.getLogger(KeyHttpServer.class);

	private static final int KEYSERVER_PORT = 55555;

	private NioSocketAcceptor acceptor;

	/**
	 * 处理请求消息
	 * 
	 * @param session
	 * @param message
	 */
	private void processRequest(IoSession session, Object message) {
		log.info("Received : " + message.getClass().getName() + "\n"
				+ message.toString());
		if (message instanceof HttpRequestImpl) {
			KeyHttpService.getInstance().dispatcherRequest(session,
					(HttpRequestImpl) message);
		} else if (message instanceof IoBuffer) // POST/PUT方法的"表单"数据通过IoBuffer传递到这里
		{
			log.error("激活码验证服务器目前只支持GET请求。");
		} else if (message instanceof HttpEndOfContent) {
			log.info(message.getClass().getName());
		} else {
			log.error("unknow message: [" + message.getClass().getName() + "]");
		}
	}

	/**
	 * 启动HTTP监听
	 * 
	 * @return
	 */
	public void accept() {
		log.info("鸡和马验证服务器开始启动。");
		acceptor = new NioSocketAcceptor();
		// acceptor.setBacklog(port);
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("codec", new HttpServerCodec());// 添加数据编解码器
		// 添加有序线程池，以保证消息按先后顺序到达
		OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor(
				500);
		chain.addLast("threadPool", new ExecutorFilter(threadpool));
		acceptor.setHandler(new KeyHttpServerHandler());

		// int recsize = 5120;
		// int sendsize = 20480;
		acceptor.setReuseAddress(true); // 设置每一个非主监听连接的端口可以重用
		SocketSessionConfig sc = acceptor.getSessionConfig();
		sc.setReuseAddress(true); // 设置每一个非主监听连接的端口可以重用
		// sc.setReceiveBufferSize(recsize); // 设置输入缓冲区的大小
		// sc.setSendBufferSize(sendsize); // 设置输出缓冲区的大小
		// sc.setTcpNoDelay(true);
		// sc.setSoLinger(0);

		try {
			acceptor.bind(new InetSocketAddress(KEYSERVER_PORT));
			log.info("鸡和马验证服务器监听端口port： " + KEYSERVER_PORT);
		} catch (IOException e) {
			log.error("鸡和马验证服务器异常：" + e.toString());
			System.exit(1);
		}
	}

	public void unaccept() {
		if (acceptor != null) {
			acceptor.unbind();
			log.info("鸡和马验证服务器注销。");
		} else {
			log.error("NioSocketAcceptor is null.");
		}

	}

	public static KeyHttpServer getInstance() {
		return KeyHttpServer.Singleton.INSTANCE.getServer();
	}

	public class KeyHttpServerHandler extends IoHandlerAdapter {
		@Override
		public void sessionCreated(IoSession session) {
			session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120); // 空闲Http连接保持120秒
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			log.info("CLOSED");
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			log.info("OPENED");
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status) {
			log.info("*** IDLE #" + session.getIdleCount(IdleStatus.BOTH_IDLE)
					+ " ***");
			session.close(true);
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) {
			log.info(
					"*** exceptionCaught #"
							+ session.getIdleCount(IdleStatus.BOTH_IDLE)
							+ " ***", cause);
			session.close(true);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			processRequest(session, message);
		}
	}

	private enum Singleton {
		INSTANCE;

		KeyHttpServer server;

		Singleton() {
			this.server = new KeyHttpServer();
		}

		KeyHttpServer getServer() {
			return server;
		}
	}
}
