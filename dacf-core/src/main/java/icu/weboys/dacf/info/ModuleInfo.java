package icu.weboys.dacf.info;

import icu.weboys.dacf.inter.IConnector;
import icu.weboys.dacf.inter.IModule;
import lombok.Data;

import java.util.Map;

@Data
public class ModuleInfo {
    private String name;
    private Boolean enable;
    private String flag;
    private String host;
    private Integer port;
    private Map<String,Object> param;
    private String moduleName;
    private String connectorName;
    private IModule moduleObject;
    private IConnector moduleConnector;

    public ModuleInfo(String name, Boolean enable, String flag, String host, Integer port, String moduleName, String connectorName) {
        this.name = name;
        this.enable = enable;
        this.flag = flag;
        this.host = host;
        this.port = port;
        this.moduleName = moduleName;
        this.connectorName = connectorName;
    }

    public ModuleInfo() {
    }
}
