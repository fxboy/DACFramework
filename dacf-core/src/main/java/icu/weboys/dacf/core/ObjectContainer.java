package icu.weboys.dacf.core;

import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IConnector;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.inter.IModuleInitHandler;
import icu.weboys.dacf.core.util.Assert;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectContainer {
    private static Map<String,String> MODULE_CLASS = new ConcurrentHashMap<>();
    private static Map<String,String> CONNECTOR_CLASS = new ConcurrentHashMap<>();
    private static Map<String, ModuleInfo> REG_MODULE_INFO = new ConcurrentHashMap<>();
    private static Map<String, String> REG_REMOTE_MODULE_INFO = new ConcurrentHashMap<>();
    private static Map<String,Object> MODULE_INIT_HANDLERS = new ConcurrentHashMap<>();
    private static Map<String,Object> DATA_INIT_HANDLERS = new ConcurrentHashMap<>();


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

    public static void moduleHandlerInit(){
        MODULE_INIT_HANDLERS.keySet().forEach(i-> {
            try{
                RegObject.regModuleInitHandler("mhd",i);
            }catch (Exception e){
                return;
            }
        });
    }

    public static void putModuleHandlerInit(String handler){
        MODULE_INIT_HANDLERS.put(handler,new Object());
    }

    public static void dataHandlerInit(ModuleInfo mi, DataInfo di){
        DATA_INIT_HANDLERS.keySet().forEach(i-> {
            try{
                RegObject.regDataInitHandler("mdt",i,mi,di);
            }catch (Exception e){
                return;
            }
        });
    }

    public static void putDataHandlerInit(String handler){
        DATA_INIT_HANDLERS.put(handler,new Object());
    }



}
