package icu.weboys.dacf.core.abs;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.init.DataSave;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Resource;

@Log4j2
public abstract class AbsModule<T> implements IModule<T> {

    @Resource
    DataSave dataSave;
    
    protected String name;

    @Override
    public void init(String name) {
        this.name = name;
        log.info(String.format("[%s] Module created",this.name));
    }

    @Override
    public void save(DataInfo dataInfo) {
        // Cut to other modules for processing
        this.dataSave.save( ObjectContainer.get(this.name),dataInfo);
    }

}
