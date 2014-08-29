package org.yws.pangu.rpc.master.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

import org.yws.pangu.domain.RegisterInfo;
import org.yws.pangu.domain.TaskNode;
import org.yws.pangu.enums.EOperateResult;

public class RegisterHandler extends SimpleChannelInboundHandler<RegisterInfo> {
	private Map<Channel, TaskNode> taskNodes;

	public RegisterHandler(Map<Channel, TaskNode> taskNodes) {
		this.taskNodes = taskNodes;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RegisterInfo msg) throws Exception {
		msg.setOperateResult(EOperateResult.SUCCESS);
		ctx.writeAndFlush(msg);
		taskNodes.put(ctx.channel(), new TaskNode(msg.getHostName(), msg.getHostAddress()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		taskNodes.remove(ctx.channel());
	}

}