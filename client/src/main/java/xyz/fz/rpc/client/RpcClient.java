package xyz.fz.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.fz.rpc.client.handler.KryoObjectDecoder;
import xyz.fz.rpc.client.handler.KryoObjectEncoder;
import xyz.fz.rpc.client.handler.RpcClientHandler;
import xyz.fz.rpc.model.Request;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    @Value("${rpc.server.host}")
    private String rpcServerHost;

    @Value("${rpc.server.port}")
    private int rpcServerPort;

    private static Channel channel;

    public static ConcurrentHashMap<Long, CompletableFuture<Object>> RPC_REQUEST_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("InfiniteRecursion")
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(rpcServerHost, rpcServerPort))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new KryoObjectDecoder());
                            ch.pipeline().addLast(new KryoObjectEncoder());
                            ch.pipeline().addLast(new RpcClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();

            LOGGER.warn("rpc client startup...");

            channel = future.channel();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            channel = null;
            bossGroup.shutdownGracefully();
            long sleep = (long) (Math.random() * 3000) + 1000;
            LOGGER.warn("rpc client reconnect after " + sleep + "ms...");
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            start();
        }
    }

    public static void call(Request request, CompletableFuture<Object> completableFuture) {
        RPC_REQUEST_MAP.put(request.getId(), completableFuture);
        channel.writeAndFlush(request);
    }
}
