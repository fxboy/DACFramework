package icu.weboys.dacf.core.inter;


import icu.weboys.dacf.core.info.ModuleInfo;

public interface IConnector<T> {
    void init(String name);
    void connect();
    void reconnect();
    void send(T data);
    void recv(T data);
    void dispose();
    void close();
    void remoteReg(String remoteAddr);
}
