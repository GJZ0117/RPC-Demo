package cn.gjz;

import cn.gjz.common.URL;
import cn.gjz.protocol.HttpServer;
import cn.gjz.register.LocalRegister;
import cn.gjz.register.MapRemoteRegister;

public class Provider {
    public static void main(String[] args) {
        // 服务的本地注册
        LocalRegister.regist(HelloService.class.getName(), "1.0", HelloServiceImpl.class);

        // 注册中心注册
        URL url = new URL("localhost", 9999);
        MapRemoteRegister.regist(HelloService.class.getName(), url); // 服务注册

        // 启动tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }
}
