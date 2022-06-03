package icu.weboys.dacf.api.ctrl;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.RegObject;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.info.ResultInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/dacf/process")
public class SetCtrl {

    @PostMapping("/regModule")
    public Object regModule(@RequestBody ModuleInfo moduleInfo){
        try {
            if(!ObjectContainer.getRegModuleInfo().containsKey(moduleInfo.getName())){
                ObjectContainer.add(moduleInfo.getName(),moduleInfo);
                RegObject.regModule(moduleInfo);
            }
            return new ResultInfo(200,"OK");
        } catch (ClassNotFoundException e) {
            ObjectContainer.getModuleInfo(moduleInfo.getName()).setEnable(false);
            return new ResultInfo(500,e.getMessage());
        }
    }

    @PostMapping("/regModules")
    public Object regModule(@RequestBody List<ModuleInfo> modules){
        AtomicReference<ResultInfo> resultInfo = new AtomicReference<>();
        modules.forEach(moduleInfo -> {
            try {
                    if(ObjectContainer.getRegModuleInfo().containsKey(moduleInfo.getName())){
                        return;
                    }
                    ObjectContainer.add(moduleInfo.getName(),moduleInfo);
                    RegObject.regModule(moduleInfo);
                    resultInfo.set(new ResultInfo(200,"OK"));
                } catch (ClassNotFoundException e) {
                    ObjectContainer.getModuleInfo(moduleInfo.getName()).setEnable(false);
                    resultInfo.set(new ResultInfo(500, e.getMessage()));
                }
            });
        return resultInfo.get();
    }


    @PostMapping("/unRegModule")
    public Object unRegModule(String module){
        try {
            if(ObjectContainer.getRegModuleInfo().containsKey(module)){
                RegObject.unReg(module,true);
            }
            return new ResultInfo(200,"OK");
        } catch (Exception e) {
            return new ResultInfo(500,e.getMessage());
        }
    }

    @PostMapping("/uptModule")
    public Object uptModule(@RequestBody ModuleInfo moduleInfo){
        try {
            if(ObjectContainer.getRegModuleInfo().containsKey(moduleInfo.getName())){
                RegObject.unReg(moduleInfo.getName(),false);
                ObjectContainer.add(moduleInfo.getName(),moduleInfo);
                RegObject.regModule(moduleInfo);
            }
            return new ResultInfo(200,"OK");
        } catch (ClassNotFoundException e) {
            return new ResultInfo(500,e.getMessage());
        }
    }

}
