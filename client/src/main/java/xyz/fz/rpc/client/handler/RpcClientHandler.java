package xyz.fz.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xyz.fz.rpc.client.RpcClient;
import xyz.fz.rpc.model.Response;

import java.util.concurrent.CompletableFuture;

public class RpcClientHandler extends SimpleChannelInboundHandler<Response> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        CompletableFuture<Object> future = RpcClient.RPC_REQUEST_MAP.get(msg.getId());
        RpcClient.RPC_REQUEST_MAP.remove(msg.getId());
        future.complete(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        RpcClient.RPC_REQUEST_MAP.clear();
        cause.printStackTrace();
        ctx.close();
    }
}
