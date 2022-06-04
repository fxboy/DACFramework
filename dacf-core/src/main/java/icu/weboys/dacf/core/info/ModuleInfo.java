package icu.weboys.dacf.core.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import icu.weboys.dacf.core.inter.IConnector;
import icu.weboys.dacf.core.inter.IModule;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties({"moduleObject","moduleConnector"})
public class ModuleInfo {
    private String name;
    private Boolean enable;
    private String flag;
    private String host;
    private Integer port;
    private String local;
    private String tag;
    private String category;
    private Map<String,Object> params;
    private String className;
    private String connectorName;
    private Boolean connectFlag = false;
    private IModule moduleObject;
    private IConnector moduleConnector;

    public ModuleInfo(String name, Boolean enable, String flag, String host, Integer port, String className, String connectorName,String local,String tag,String category) {
        this.name = name;
        this.enable = enable;
        this.flag = flag;
        this.host = host;
        this.port = port;
        this.className = className;
        this.connectorName = connectorName;
        this.local = local;
        this.tag = tag;
        this.category = category;
    }

    public ModuleInfo() {
    }
}
