package org.yws.pangu.rpc.slave.handler;

import org.yws.pangu.domain.RegisterInfo;
import org.yws.pangu.enums.EOperateResult;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RegisterClientHandler extends SimpleChannelInboundHandler<RegisterInfo> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RegisterInfo msg) throws Exception {
		System.out.println("Server say : " + msg.getHostAddress() + " --> "
				+ ((msg.getOperateResult() == EOperateResult.SUCCESS) ? "OK" : "FAILED"));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}