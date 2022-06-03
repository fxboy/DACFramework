package icu.weboys.dacf.core.abs;


import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IConnector;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbsConnector<T> implements IConnector<T> {
    protected Boolean connectFlag = false;
    protected ModuleInfo moduleInfo;
    @Override
    public void init(ModuleInfo info) {
        this.moduleInfo = info;
        this.moduleInfo.setModuleConnector(this);
    }

    @Override
    public void connect() {
        this.connectFlag = true;
        log.info(String.format("[%s] Connection enabled",this.moduleInfo.getName()));
    }

    @Override
    public void reconnect() {
        this.connectFlag = false;
        log.info(String.format("[%s] Connection reconnected",this.moduleInfo.getName()));
        this.connect();
    }

    @Override
    public void close() {
        this.connectFlag = false;
        log.info(String.format("[%s] Connection closed",this.moduleInfo.getName()));
    }

    @Override
    public void recv(T data) {
        // 这边data.tostring 需要加类型判断 字节，文件
        log.debug("[%s] recv:%s",this.moduleInfo.getName(),data.toString());
        ObjectContainer.getModuleObject(moduleInfo).recv(data);
    }

    @Override
    public void send(T data) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void remoteReg(String remoteAddr) {
        ObjectContainer.addRemote(remoteAddr,this.moduleInfo);
    }
}
