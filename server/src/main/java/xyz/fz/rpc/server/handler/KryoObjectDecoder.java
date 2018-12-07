package xyz.fz.rpc.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import xyz.fz.rpc.util.KryoUtil;
import xyz.fz.rpc.exception.NotEnoughException;
import xyz.fz.rpc.model.Request;

import java.util.List;

public class KryoObjectDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Request request;
        try {
            request = KryoUtil.decodeByteBuf(in, Request.class);
        } catch (NotEnoughException e) {
            return;
        }
        out.add(request);
    }
}
