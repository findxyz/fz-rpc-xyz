package xyz.fz.rpc.server;

import java.lang.reflect.Method;

public class RpcCmd {

    private Object serviceObject;

    private Method serviceMethod;

    RpcCmd(Object serviceObject, Method serviceMethod) {
        this.serviceObject = serviceObject;
        this.serviceMethod = serviceMethod;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}
