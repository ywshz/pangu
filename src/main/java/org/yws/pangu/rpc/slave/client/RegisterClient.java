package org.yws.pangu.rpc.slave.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import org.yws.pangu.domain.RegisterInfo;
import org.yws.pangu.enums.ERegisterType;
import org.yws.pangu.rpc.slave.handler.RegisterClientHandler;

public class RegisterClient {
	private EventLoopGroup group;
	private Channel channel = null;

	public RegisterClient() throws InterruptedException {
		group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(
								new ObjectDecoder(ClassResolvers.cacheDisabled(RegisterInfo.class
										.getClassLoader())), new ObjectEncoder(),
								new RegisterClientHandler());
					}
				});

		channel = bootstrap.connect("127.0.0.1", 8008).sync().channel();
	}

	public void register(String localAddress, String localName) {
		channel.writeAndFlush(new RegisterInfo(localAddress, localName, ERegisterType.REGISTER));
	}

	public void heartBeat(String localAddress, String localName) {
		channel.writeAndFlush(new RegisterInfo(localAddress, localName, ERegisterType.HEART_BEAT));
	}

	public void stop() {
		channel.disconnect();
		channel.close();
		group.shutdownGracefully();
	}
}
