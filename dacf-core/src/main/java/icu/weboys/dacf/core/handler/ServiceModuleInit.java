package icu.weboys.dacf.core.handler;

import com.alibaba.fastjson2.JSON;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceModuleInit {

    @Before("execution(* icu.weboys.dacf.core.init.DACFModuleInit.init(..) )")
    public void init(){
        ObjectContainer.moduleHandlerInit();
    }
}
