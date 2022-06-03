package icu.weboys.dacf.core;

import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.util.Assert;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectContainer {
    private static Map<String,String> MODULE_CLASS = new ConcurrentHashMap<>();
    private static Map<String,String> CONNECTOR_CLASS = new ConcurrentHashMap<>();
    private static Map<String, ModuleInfo> REG_MODULE_INFO = new ConcurrentHashMap<>();
    private static Map<String, String> REG_REMOTE_MODULE_INFO = new ConcurrentHashMap<>();

    public static void add(String packageName,Integer t){
        String[] name = packageName.replace(".",",").split(",");
        switch (t){
            case 1 -> {MODULE_CLASS.put(name[name.length - 1],packageName); break;}
            case 2 -> {CONNECTOR_CLASS.put(name[name.length - 1],packageName);break;}
            default -> {break;}
        }
    }

    public static Map<String, String> getModuleClass() {
        return MODULE_CLASS;
    }

    public static Map<String, String> getConnectorClass() {
        return CONNECTOR_CLASS;
    }

    public static Map<String, ModuleInfo> getRegModuleInfo() {
        return REG_MODULE_INFO;
    }

    public static Map<String, String> getRegRemoteModuleInfo() {
        return REG_REMOTE_MODULE_INFO;
    }

    public static void add(String moduleName, ModuleInfo moduleInfo){
        REG_MODULE_INFO.put(moduleName,moduleInfo);
        ObjectContainer.addRemote(String.format("%s:%s",moduleInfo.getHost(),moduleInfo.getPort()),moduleInfo.getName());
    }

    public static void addRemote(String remoteAddr,String name){
        REG_REMOTE_MODULE_INFO.put(remoteAddr,name);
    }

    public static ModuleInfo get(String moduleName){
        return REG_MODULE_INFO.get(moduleName);
    }

    public static ModuleInfo getModuleInfo(String moduleName){
        ModuleInfo mdi = ObjectContainer.get(moduleName);
        Assert.notNull(mdi,"Module not found");
        return mdi;
    }

    public static IModule getModuleObject(String moduleName){
        IModule ime = getModuleInfo(moduleName).getModuleObject();
        Assert.notNull(ime,"Module does not register entity object");
        return ime;
    }

    public static IModule getModuleObject(ModuleInfo moduleInfo){
        IModule ime = moduleInfo.getModuleObject();
        Assert.notNull(ime,"Module does not register entity object");
        return ime;
    }

    public static ModuleInfo getRemoteModuleInfo(String remote){
        return REG_MODULE_INFO.get(REG_REMOTE_MODULE_INFO.get(remote));
    }



}
