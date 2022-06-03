package icu.weboys.dacf.services.init;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceModuleInit {
    @Before("execution(* icu.weboys.dacf.services.init.DACFServiceInit.init(..) )")
    public void init(){
        ObjectContainer.add("TEST",new ModuleInfo("TEST",true,"t","127.0.0.1",4909,"TestMoudle","NettyServerConnector"));
    }
}
