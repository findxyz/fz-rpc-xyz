package xyz.fz.rpc.factory;

import org.springframework.beans.factory.FactoryBean;
import xyz.fz.rpc.client.RpcClient;
import xyz.fz.rpc.exception.NetErrorException;
import xyz.fz.rpc.exception.RpcException;
import xyz.fz.rpc.model.Request;
import xyz.fz.rpc.model.Response;
import xyz.fz.rpc.util.SnowFlake;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RpcFactoryBean<T> implements FactoryBean<T> {

    private Class<T> proxyInterface;

    public void setProxyInterface(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;
    }

    private static final SnowFlake snowFlake = new SnowFlake(1, 1);

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{proxyInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Request request = new Request();
                request.setId(snowFlake.generateNextId());
                request.setClazz(proxyInterface.getName());
                request.setMethod(method.getName());
                request.setArgs(args);
                CompletableFuture<Object> completableFuture = new CompletableFuture<>();
                try {
                    RpcClient.call(request, completableFuture);
                } catch (Exception e) {
                    RpcClient.RPC_REQUEST_MAP.remove(request.getId());
                    throw new NetErrorException(e.getCause());
                }
                Response response = (Response) completableFuture.get(5, TimeUnit.SECONDS);
                if (response.isSuccess()) {
                    return response.getData();
                } else {
                    RpcClient.RPC_REQUEST_MAP.remove(request.getId());
                    throw new RpcException(response.getErrorMessage());
                }
            }
        });
        return (T) proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return proxyInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
