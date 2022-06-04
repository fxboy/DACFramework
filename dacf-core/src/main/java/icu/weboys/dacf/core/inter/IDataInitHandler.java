package icu.weboys.dacf.core.inter;

import icu.weboys.dacf.core.info.DataInfo;
import icu.weboys.dacf.core.info.ModuleInfo;

public interface IDataInitHandler {
    void save(ModuleInfo module, DataInfo data);
}
