package icu.weboys.dacf.abs;

import icu.weboys.dacf.info.DataInfo;
import icu.weboys.dacf.info.ModuleInfo;
import icu.weboys.dacf.inter.IModule;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbsModule<T> implements IModule<T> {
    protected ModuleInfo moduleInfo;
    @Override
    public void init(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        log.info(String.format("[%s] Module created",this.moduleInfo.getName()));
    }

    @Override
    public void save(DataInfo dataInfo) {
        // Cut to other modules for processing
    }
}
