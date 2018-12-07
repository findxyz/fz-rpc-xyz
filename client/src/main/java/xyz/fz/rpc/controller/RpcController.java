package xyz.fz.rpc.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.rpc.model.FooDTO;
import xyz.fz.rpc.service.RpcService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/rpc")
public class RpcController {

    @Resource
    private RpcService rpcService;

    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam("name") String name) {
        return rpcService.sayHello(name);
    }

    @RequestMapping("/sayMap")
    public Map<String, Object> sayMap(@RequestBody Map<String, Object> params) {
        return rpcService.sayMap(params);
    }

    @RequestMapping("/sayFoo")
    public FooDTO sayFoo() {
        FooDTO fooDTO = new FooDTO();
        fooDTO.setId(System.currentTimeMillis());
        fooDTO.setName("Ya ha ha: " + Math.random());
        fooDTO.setValue(new BigDecimal(Math.random() * 1000));
        fooDTO.setDate(new Date());
        return rpcService.sayFoo(fooDTO);
    }

    @RequestMapping("/sayNothing")
    public void sayNothing() {
        rpcService.sayNothing();
    }

    @RequestMapping("/sayError")
    public void sayError() {
        rpcService.sayError();
    }
}
