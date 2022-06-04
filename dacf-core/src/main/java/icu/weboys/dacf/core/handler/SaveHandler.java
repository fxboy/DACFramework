package icu.weboys.dacf.core.handler;

import icu.weboys.dacf.core.ObjectContainer;
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
    @After("execution(* icu.weboys.dacf.core.init.DataSave.save(..) )")
    public void register(JoinPoint joinPoint){
        ModuleInfo mi = (ModuleInfo) joinPoint.getArgs()[0];
        DataInfo   di = (DataInfo) joinPoint.getArgs()[1];
        ObjectContainer.dataHandlerInit(mi,di);
    }
}
