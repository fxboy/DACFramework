package icu.weboys.dacf.core;

import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IDataInitHandler;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.inter.IModuleInitHandler;
import icu.weboys.dacf.core.util.Assert;
import icu.weboys.dacf.core.util.BaseUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class RegObject {
    public static ApplicationContext temp_applicationContext;
    public static void createBean(ApplicationContext context, String name , Class clazz){
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
    }


    public static void regModuleInitHandler(String type, String className) throws ClassNotFoundException{
        String beanName = String.format("%s_%s",type, BaseUtils.reword("abcdefghijk"));
        Assert.notNull(className,String.format("Object named %s not found",className));
        Class cz = Class.forName(className);
        RegObject.createBean(temp_applicationContext,beanName,cz);
        ((IModuleInitHandler) temp_applicationContext.getBean(beanName)).init();
    }

    public static void regDataInitHandler(String type, String className, ModuleInfo mi, DataInfo di) throws ClassNotFoundException{
        String beanName = String.format("%s_%s",type, BaseUtils.reword("lmnopqrstu"));
        Assert.notNull(className,String.format("Object named %s not found",className));
        Class cz = Class.forName(className);
        RegObject.createBean(temp_applicationContext,beanName,cz);
        ((IDataInitHandler) temp_applicationContext.getBean(beanName)).save(mi,di);
    }


    public static void regModule(ModuleInfo v) throws ClassNotFoundException {
        String beanName = "module_" + v.getName();
        String packageName = ObjectContainer.getModuleClass().get(v.getClassName());
        Assert.notNull(packageName,String.format("Module object named %s not found",v.getClassName()));
        Class cz = Class.forName(packageName);
        RegObject.createBean(temp_applicationContext,beanName,cz);
        v.setModuleObject((IModule) temp_applicationContext.getBean(beanName));
        v.getModuleObject().init(v.getName());
    }

    public static void unReg(String name,Boolean del){
        ObjectContainer.getModuleInfo(name).setEnable(false);
        unRegBean("module_" + name);
        ObjectContainer.getModuleInfo(name).getModuleConnector().close();
        ObjectContainer.getRegRemoteModuleInfo().remove(name);
        if(del){
            ObjectContainer.getRegModuleInfo().remove(name);
        }
    }

    public static void unRegBean(String beanName) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext)temp_applicationContext).getBeanFactory();
        if (beanFactory.containsBeanDefinition(beanName)) {
            beanFactory.destroySingleton(beanName);
            beanFactory.removeBeanDefinition(beanName);
        }
    }

}
