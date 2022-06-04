package icu.weboys.dacf.api;

import icu.weboys.dacf.api.cache.CacheData;
import icu.weboys.dacf.api.cache.CacheModules;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.annotation.DataInitialization;
import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IDataInitHandler;

import javax.annotation.Resource;

@DataInitialization
public class DataSaveInit implements IDataInitHandler {
    @Resource
    CacheData cacheData;
    @Override
    public void save(ModuleInfo module, DataInfo data) {
        cacheData.add(module.getName(),new CacheModules(module,data));
    }
}
