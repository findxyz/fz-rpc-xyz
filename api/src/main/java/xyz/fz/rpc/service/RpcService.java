package xyz.fz.rpc.service;

import xyz.fz.rpc.model.FooDTO;

import java.util.Map;

public interface RpcService {
    String sayHello(String name);

    Map<String, Object> sayMap(Map<String, Object> params);

    FooDTO sayFoo(FooDTO fooDTO);

    void sayNothing();

    void sayError();
}
