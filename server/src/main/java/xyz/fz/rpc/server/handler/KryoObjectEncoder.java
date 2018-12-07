package xyz.fz.rpc.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.fz.rpc.util.KryoUtil;
import xyz.fz.rpc.model.Response;

public class KryoObjectEncoder extends MessageToByteEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, ByteBuf out) throws Exception {
        KryoUtil.encodeByteBuf(msg, out);
    }
}
