package cn.gjz.proxy;

import cn.gjz.common.Invocation;
import cn.gjz.common.URL;
import cn.gjz.locadbalance.LoadBalance;
import cn.gjz.protocol.HttpClient;
import cn.gjz.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            // 得到传入接口的代理对象
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // ------------------------------
                // 需要调用的服务没有开发完 需要一个结果
                String mock = System.getProperty("mock");
                if (mock!=null && mock.startsWith("return:")) {
                    String result = mock.replace("return: ", "");
                    return result;
                }
                // ------------------------------

                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args);
                HttpClient httpClient = new HttpClient();

                // 通过注册中心找ip、端口
                List<URL> list = MapRemoteRegister.get(interfaceClass.getName()); // 服务发现

                String result = null;
                List<URL> invokedURLs = new ArrayList<>();
                int max = 3;
                // 服务重试
                while (max > 0) {
                    // 负载均衡
                    list.remove(invokedURLs);
                    URL url = LoadBalance.random(list);
                    invokedURLs.add(url);
                    max--;

                    try {
                        // 服务调用
                        result = httpClient.send(url.getHostname(), url.getPort(), invocation);
                    } catch (Exception e) {
                        if (max != 0) {
                            continue;
                        }
                        // 服务容错
                        return "报错了...";
                    }
                }

                return result;
            }
        });
        return (T) proxyInstance;
    }
}
