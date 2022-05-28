package icu.weboys.dacf.inter;


import icu.weboys.dacf.info.ModuleInfo;

public interface IConnector<T> {
    void init(ModuleInfo info);
    void connect();
    void reconnect();
    void send(T data);
    void recv(T data);
    void dispose();
    void close();
    void remoteReg(String remoteAddr);
}
