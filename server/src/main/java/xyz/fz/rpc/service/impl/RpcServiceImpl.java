package xyz.fz.rpc.service.impl;

import org.springframework.stereotype.Service;
import xyz.fz.rpc.model.FooDTO;
import xyz.fz.rpc.service.RpcService;

import java.util.Map;

@Service
public class RpcServiceImpl implements RpcService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @Override
    public Map<String, Object> sayMap(Map<String, Object> params) {
        return params;
    }

    @Override
    public FooDTO sayFoo(FooDTO fooDTO) {
        return fooDTO;
    }

    @Override
    public void sayNothing() {
    }

    @Override
    public void sayError() {
        String a = null;
        System.out.println(a.toString());
    }
}
