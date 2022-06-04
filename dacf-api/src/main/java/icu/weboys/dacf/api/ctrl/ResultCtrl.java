package icu.weboys.dacf.api.ctrl;

import icu.weboys.dacf.api.cache.CacheData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/dacf/api")
public class ResultCtrl {
    @Resource
    CacheData cacheData;

    @RequestMapping("/modules")
    public Object getModules(){
        return cacheData.get();
    }

    public Object getModulesAllResult(){
        return null;
    }

    @RequestMapping("/result/{module}")
    public Object getModuleResult(@PathVariable("module") String module){
        return cacheData.get(module);
    }

    @RequestMapping("/result/his/{module}")
    public Object getModuleHisResult(@PathVariable("module") String module){
        return cacheData.getHis(module);
    }

    @RequestMapping("/result/his")
    public Object getModuleHisResult(){
        return cacheData.getHis();
    }
}
