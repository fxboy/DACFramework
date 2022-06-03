package icu.weboys.dacf.api.cache;

import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;
import lombok.Data;

@Data
public class CacheModules {
    private String name;
    private ModuleInfo module;
    private DataInfo data;

    public CacheModules(ModuleInfo module, DataInfo data) {
        this.module = module;
        this.data = data;
        this.name = module.getName();
    }
}
