package xyz.fz.rpc.model;

import java.util.Arrays;

public class Request {
    private Long id;

    private String clazz;

    private String method;

    private Object[] args;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", clazz='" + clazz + '\'' +
                ", method='" + method + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
