package xyz.fz.rpc.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.fz.rpc.util.KryoUtil;
import xyz.fz.rpc.model.Request;

public class KryoObjectEncoder extends MessageToByteEncoder<Request> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) throws Exception {
        KryoUtil.encodeByteBuf(msg, out);
    }
}
