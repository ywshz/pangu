package org.yws.pangu.rpc.master.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yws.pangu.domain.RegisterInfo;
import org.yws.pangu.domain.TaskNode;
import org.yws.pangu.rpc.master.handler.RegisterHandler;

public class RegisterServer {
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap server;
	private RegisterHandler registerHandler;
	private Map<Channel, TaskNode> taskNodes;

	public RegisterServer(Map<Channel, TaskNode> taskNodes) {
		this.taskNodes = taskNodes;

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		this.registerHandler = new RegisterHandler(taskNodes);
		server = new ServerBootstrap();
		server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(
								new ObjectDecoder(ClassResolvers.cacheDisabled(RegisterInfo.class
										.getClassLoader())), new ObjectEncoder(), registerHandler);
					}
				});
	}

	public void start() throws InterruptedException {
		server.bind(8008).sync().channel().closeFuture().sync();
	}

	public void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	public List<TaskNode> getAllWokers() {
		List<TaskNode> tdl = new ArrayList<TaskNode>();
		for (TaskNode node : this.taskNodes.values()) {
			tdl.add(node);
		}
		return tdl;
	}
}
