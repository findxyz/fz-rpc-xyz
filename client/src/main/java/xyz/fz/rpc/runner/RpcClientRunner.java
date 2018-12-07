package xyz.fz.rpc.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.rpc.client.RpcClient;

import javax.annotation.Resource;

@Component
public class RpcClientRunner implements CommandLineRunner {

    @Resource
    private RpcClient rpcClient;

    @Override
    public void run(String... args) throws Exception {
        rpcClient.start();
    }
}
