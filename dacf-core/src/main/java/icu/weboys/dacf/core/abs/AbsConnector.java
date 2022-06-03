package icu.weboys.dacf.core.abs;


import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IConnector;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbsConnector<T> implements IConnector<T> {
    protected Boolean connectFlag = false;
    protected String name;
    @Override
    public void init(String name) {
        this.name = name;
        ObjectContainer.get(name).setModuleConnector(this);
    }

    @Override
    public void connect() {
        this.connectFlag = true;
         ObjectContainer.get(this.name).setConnectFlag(true);
        this.remoteReg(ObjectContainer.get(this.name).getHost() + ":" + ObjectContainer.get(this.name).getPort());
        log.info(String.format("[%s] Connection enabled", this.name));
    }

    @Override
    public void reconnect() {
        this.connectFlag = false;
         ObjectContainer.get(this.name).setConnectFlag(false);
        log.info(String.format("[%s] Connection reconnected", this.name));
        this.connect();
    }

    @Override
    public void close() {
        this.connectFlag = false;
         ObjectContainer.get(this.name).setConnectFlag(false);
        ObjectContainer.getRegRemoteModuleInfo().remove(name);
        log.info(String.format("[%s] Connection closed", this.name));
    }

    @Override
    public void recv(T data) {
        // 这边data.tostring 需要加类型判断 字节，文件
        log.debug("[%s] recv:%s", this.name,data.toString());
        ObjectContainer.getModuleObject(this.name).recv(data);
    }

    @Override
    public void send(T data) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void remoteReg(String remoteAddr) {
        ObjectContainer.addRemote(remoteAddr, this.name);
    }
}
