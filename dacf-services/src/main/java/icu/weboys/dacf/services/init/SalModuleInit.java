package icu.weboys.dacf.services.init;

import com.alibaba.fastjson2.JSON;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.annotation.ModuleInitialization;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IModuleInitHandler;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;
import java.util.Scanner;

@ModuleInitialization
public class SalModuleInit implements IModuleInitHandler {
    @Value("${dacf.services.modulesPath}")
    String path;

    @Override
    public void init() {
        File file = new File(path);
        try {
            if(file.exists()){
                Scanner sc = new Scanner(file);
                sc.useDelimiter("\\Z");
                String set = sc.next();
                sc.close();
                if(set.trim().length() > 0){
                    List<ModuleInfo> l = JSON.parseArray(set,ModuleInfo.class);
                    l.forEach(k-> ObjectContainer.add(k.getName(),k));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
