package icu.weboys.dacf.services.init;

import com.alibaba.fastjson2.JSON;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

@Component
@Aspect
public class ServiceModuleInit {
    @Value("${dacf.services.modulesPath}")
    String path;
    @Before("execution(* icu.weboys.dacf.services.init.DACFServiceInit.init(..) )")
    public void init(){
        File file = new File(path);
        try {
            if(file.exists()){
                Scanner sc = new Scanner(file);
                sc.useDelimiter("\\Z");
                String set = sc.next();
                sc.close();
                if(set.trim().length() > 0){
                    List<ModuleInfo> l = JSON.parseArray(set,ModuleInfo.class);
                    l.forEach(k->ObjectContainer.add(k.getName(),k));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
