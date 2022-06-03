package icu.weboys.dacf.api.cache;

import icu.weboys.dacf.core.info.DataInfo;
import lombok.Data;
import org.springframework.stereotype.Component;
import icu.weboys.dacf.core.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class CacheData {
    private Map<String,CacheModules> cache = new ConcurrentHashMap<>();
    private List<CacheModules> hisCache = new ArrayList<>();
    public void add(String name,CacheModules module){
        cache.put(name,module);
        hisCache.add(module);
    }

    public Object get(String name){
        CacheModules cms  = cache.get(name);
        if(cms == null){
            return new Object();
        }
        return cms.getData();
    }
}

