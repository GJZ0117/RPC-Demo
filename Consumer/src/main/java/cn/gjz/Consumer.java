package cn.gjz;

import cn.gjz.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("GJZ");
        System.out.println(result);
    }
}
