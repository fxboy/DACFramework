package icu.weboys.dacf.core.inter;

import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.info.DataInfo;

public interface IModule<T> {
    void init(ModuleInfo moduleInfo);
    void recv(T data);
    void save(DataInfo dataInfo);
}
