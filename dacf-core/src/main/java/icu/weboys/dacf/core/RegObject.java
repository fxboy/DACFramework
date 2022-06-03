package icu.weboys.dacf.core;

import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.util.Assert;
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
