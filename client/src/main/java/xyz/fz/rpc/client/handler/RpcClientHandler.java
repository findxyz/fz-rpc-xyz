package xyz.fz.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xyz.fz.rpc.model.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientHandler extends SimpleChannelInboundHandler<Response> {

    private ConcurrentHashMap<Long, CompletableFuture<Object>> rpcRecordMap;

    public RpcClientHandler(ConcurrentHashMap<Long, CompletableFuture<Object>> rpcRecordMap) {
        this.rpcRecordMap = rpcRecordMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        CompletableFuture<Object> future = rpcRecordMap.get(msg.getId());
        rpcRecordMap.remove(msg.getId());
        future.complete(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
