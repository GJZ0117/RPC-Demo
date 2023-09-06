package cn.gjz.register;

import java.util.HashMap;
import java.util.Map;

// 本地注册工具
public class LocalRegister {
    private static Map<String, Class> map = new HashMap<>();

    public static void regist(String interfaceName, String version, Class implClass) {
        map.put(interfaceName+version, implClass);
    }

    public static Class get(String interfaceName, String version) {
        return map.get(interfaceName+version);
    }
}
