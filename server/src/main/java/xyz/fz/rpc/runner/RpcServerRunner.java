package xyz.fz.rpc.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.rpc.server.RpcServer;

import javax.annotation.Resource;

@Component
public class RpcServerRunner implements CommandLineRunner {

    @Resource
    private RpcServer rpcServer;

    @Override
    public void run(String... args) throws Exception {
        rpcServer.start();
    }
}
