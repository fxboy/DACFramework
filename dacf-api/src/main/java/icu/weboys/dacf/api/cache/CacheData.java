package icu.weboys.dacf.api.cache;

import icu.weboys.dacf.api.exce.AuthHandler;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ResultInfo;
import lombok.Data;
import org.springframework.stereotype.Component;
import icu.weboys.dacf.core.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class CacheData {
    @Resource
    AuthHandler authHandler;
    private Map<String,CacheModules> cache = new ConcurrentHashMap<>();
    private Map<String,List<DataInfo>> hisCache = new ConcurrentHashMap<>();
    public void add(String name,CacheModules module){
        cache.put(name,module);
        if(hisCache.containsKey(name)){
            hisCache.get(name).add(module.getData());
        }else{
            List<DataInfo> temp = new ArrayList<>();
            temp.add(module.getData());
            hisCache.put(name, temp);
        }
    }

    public Object get(String name){
        if(!authHandler.bs()){
            return new ResultInfo(403,"Authentication information comparison failed");
        }
        CacheModules cms  = null;
        if((cms = cache.get(name)) == null){
            return new ResultInfo(404,"No results found");
        }
        return new ResultInfo(200,cms.getData());
    }

    public Object get(){
        if(!authHandler.bs()){
            return new ResultInfo(403,"Authentication information comparison failed");
        }
        return new ResultInfo(200, ObjectContainer.getRegModuleInfo().values());
    }

    public Object getHis(String name){
        if(!authHandler.bs()){
            return new ResultInfo(403,"Authentication information comparison failed");
        }
        return new ResultInfo(200, hisCache.get(name));
    }

    public Object getHis(){
        if(!authHandler.bs()){
            return new ResultInfo(403,"Authentication information comparison failed");
        }
        List<DataInfo> adt = new ArrayList<>();
        hisCache.values().forEach(e -> adt.addAll(e));
        return new ResultInfo(200, adt);
    }
}

