package xyz.fz.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xyz.fz.rpc.server.handler.KryoObjectDecoder;
import xyz.fz.rpc.server.handler.KryoObjectEncoder;
import xyz.fz.rpc.server.handler.RpcServerHandler;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class RpcServer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Resource
    private ApplicationContext context;

    @Value("${rpc.server.port}")
    private int rpcServerPort;

    private Map<String, RpcCmd> rpcInvokeMap = new HashMap<>();

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new KryoObjectDecoder());
                            ch.pipeline().addLast(new KryoObjectEncoder());
                            ch.pipeline().addLast(new RpcServerHandler(rpcInvokeMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = serverBootstrap.bind("0.0.0.0", rpcServerPort).sync();

            LOGGER.warn("rpc server startup...");

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            LOGGER.warn("rpc server shutdown...");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> serviceBeans = context.getBeansWithAnnotation(Service.class);
        for (Map.Entry<String, Object> service : serviceBeans.entrySet()) {
            Class[] serviceInterfaces = service.getValue().getClass().getInterfaces();
            assert serviceInterfaces.length == 1;
            Class serviceInterface = serviceInterfaces[0];
            String serviceInterfaceName = serviceInterface.getName();
            Method[] serviceInterfaceMethods = serviceInterface.getDeclaredMethods();
            for (Method serviceInterfaceMethod : serviceInterfaceMethods) {
                String serviceInterfaceMethodName = serviceInterfaceMethod.getName();
                int serviceInterfaceMethodParamCount = serviceInterfaceMethod.getParameterCount();
                String serviceInterfaceMethodKey = serviceInterfaceName + "@" + serviceInterfaceMethodName + "@" + serviceInterfaceMethodParamCount;
                LOGGER.info("service method: {}", serviceInterfaceMethodKey);
                rpcInvokeMap.put(serviceInterfaceMethodKey, new RpcCmd(service.getValue(), serviceInterfaceMethod));
            }
        }
    }
}
