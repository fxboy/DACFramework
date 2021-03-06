package icu.weboys.dacf.start;


import icu.weboys.dacf.core.annotation.*;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.inter.IModuleInitHandler;
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
        Set<Class<?>> moduleInitHandlers = reflections.getTypesAnnotatedWith(ModuleInitialization.class);
        Set<Class<?>> dataInitHandlers = reflections.getTypesAnnotatedWith(DataInitialization.class);

        moduleList.forEach(clazz -> ObjectContainer.add(clazz.getName(),1));
        connectorList.forEach(clazz -> ObjectContainer.add(clazz.getName(),2));
        moduleInitHandlers.forEach(clazz->ObjectContainer.putModuleHandlerInit(clazz.getName()));
        dataInitHandlers.forEach(clazz->ObjectContainer.putDataHandlerInit(clazz.getName()));
        DACFService.init(applicationContext);

        return (ConfigurableApplicationContext) applicationContext;
    }
}
