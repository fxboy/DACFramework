package icu.weboys.dacf.connect;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IConnector;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import icu.weboys.dacf.core.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Aspect
public class RegisterConnector {

    @After("execution(* icu.weboys.dacf.core.inter.IModule.init(..) )")
    public void register(JoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        ModuleInfo moduleInfo = ObjectContainer.getModuleInfo(joinPoint.getArgs()[0].toString()) ;
        // 创建连接器
        String packageName = ObjectContainer.getConnectorClass().get(moduleInfo.getConnectorName());
        Assert.notNull(packageName,String.format("Connector object named %s not found",moduleInfo.getConnectorName()));
        Class cz = Class.forName(packageName);
        Constructor constructor = cz.getConstructor();
        IConnector ico = (IConnector) constructor.newInstance(null);
        Method ms = cz.getDeclaredMethod("init",String.class);
        moduleInfo.setModuleConnector(ico);
        ms.invoke(ico,moduleInfo.getName());
    }
}
