package icu.weboys.dacf.core.abs;

import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.save.DataSave;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Resource;

@Log4j2
public abstract class AbsModule<T> implements IModule<T> {
    protected ModuleInfo moduleInfo;

    @Resource
    DataSave dataSave;

    @Override
    public void init(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        log.info(String.format("[%s] Module created",this.moduleInfo.getName()));
    }

    @Override
    public void save(DataInfo dataInfo) {
        // Cut to other modules for processing
        this.dataSave.save(this.moduleInfo,dataInfo);
    }

}
