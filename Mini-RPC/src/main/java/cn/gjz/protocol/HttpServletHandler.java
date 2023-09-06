package cn.gjz.protocol;

import cn.gjz.common.Invocation;
import cn.gjz.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServletHandler {
    public void Handler(HttpServletRequest req, HttpServletResponse resp) {
        // 处理请求  核心参数：接口、方法、参数
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            String interfaceName = invocation.getInterfaceName();
            Class classImpl = LocalRegister.get(interfaceName, "1.0");
            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            String result =(String) method.invoke(classImpl.newInstance(), invocation.getParameters());

            IOUtils.write(result, resp.getOutputStream());
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
