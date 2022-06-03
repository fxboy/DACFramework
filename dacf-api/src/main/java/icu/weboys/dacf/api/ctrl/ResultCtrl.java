package icu.weboys.dacf.api.ctrl;

import icu.weboys.dacf.api.cache.CacheData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/dacf/api")
public class ResultCtrl {
    @Resource
    CacheData cacheData;
    public Object getModules(){
        return null;
    }

    public Object getModulesAllResult(){
        return null;
    }

    @RequestMapping("/result/{module}")
    public Object getModuleResult(@PathVariable("module") String module){
        return cacheData.get(module);
    }
}
