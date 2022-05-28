package icu.weboys.dacf.start;


import icu.weboys.dacf.annotation.DACFComponentScan;
import icu.weboys.dacf.annotation.DACFConnector;
import icu.weboys.dacf.annotation.DACFModule;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.ThreadContainer;
import icu.weboys.dacf.services.DACFService;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DACFApplication extends SpringApplication {



    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        return init(SpringApplication.run(primarySource, args),primarySource);
    }

    public static ConfigurableApplicationContext init(ApplicationContext applicationContext,Class<?> primarySource){
        DACFComponentScan dacfcs = primarySource.getAnnotation(DACFComponentScan.class);
        List<String> packages = Arrays.asList("icu.weboys");
        if(dacfcs != null) {
            packages.addAll(Arrays.asList(dacfcs.basePackages()));
        }
        Reflections reflections = new Reflections(packages);
        Set<Class<?>> moduleList = reflections.getTypesAnnotatedWith(DACFModule.class);
        Set<Class<?>> connectorList = reflections.getTypesAnnotatedWith(DACFConnector.class);
        moduleList.forEach(clazz -> ObjectContainer.add(clazz.getName(),1));
        connectorList.forEach(clazz -> ObjectContainer.add(clazz.getName(),2));

        // 加载用户模块列表，并存入 REG MODULS中
        // ...... 待写入

        DACFService.init(applicationContext);
        return (ConfigurableApplicationContext) applicationContext;
    }
}
