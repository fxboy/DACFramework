package icu.weboys.dacf.api;

import icu.weboys.dacf.api.cache.CacheData;
import icu.weboys.dacf.api.cache.CacheModules;
import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class SaveHandler {
    @Resource
    CacheData cacheData;
    @After("execution(* icu.weboys.dacf.core.save.DataSave.save(..) )")
    public void register(JoinPoint joinPoint){
        ModuleInfo mi = (ModuleInfo) joinPoint.getArgs()[0];
        DataInfo   di = (DataInfo) joinPoint.getArgs()[1];
        cacheData.add(mi.getName(),new CacheModules(mi,di));
    }
}
