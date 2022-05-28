package icu.weboys.dacf.inter;

import icu.weboys.dacf.info.DataInfo;
import icu.weboys.dacf.info.ModuleInfo;

public interface IModule<T> {
    void init(ModuleInfo moduleInfo);
    void recv(T data);
    void save(DataInfo dataInfo);
}
