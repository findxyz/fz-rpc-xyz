package xyz.fz.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fz.rpc.model.Request;
import xyz.fz.rpc.model.Response;
import xyz.fz.rpc.server.RpcCmd;
import xyz.fz.rpc.util.BaseUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private Map<String, RpcCmd> rpcInvokeMap;

    public RpcServerHandler(Map<String, RpcCmd> rpcInvokeMap) {
        this.rpcInvokeMap = rpcInvokeMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        RpcCmd rpcCmd = rpcInvokeMap.get(msg.getClazz() + "@" + msg.getMethod() + "@" + (msg.getArgs() == null ? 0 : msg.getArgs().length));
        Response response = new Response();
        response.setId(msg.getId());
        try {
            Object result = rpcCmd.getServiceMethod().invoke(rpcCmd.getServiceObject(), msg.getArgs());
            response.setSuccess(true);
            response.setData(result);
        } catch (Exception e) {
            LOGGER.error(BaseUtil.getExceptionStackTrace(e));
            response.setSuccess(false);
            if (e instanceof InvocationTargetException) {
                response.setErrorMessage(((InvocationTargetException) e).getTargetException().getMessage());
            } else {
                response.setErrorMessage(e.getMessage());
            }
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
