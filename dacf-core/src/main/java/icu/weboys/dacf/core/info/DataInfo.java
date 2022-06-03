package icu.weboys.dacf.core.info;

import lombok.Data;

import java.util.Date;

@Data
public class DataInfo {
    String name;
    Object value;
    Object rawData;
    Long timeunit;

    public DataInfo(String name, Object value, Object rawData) {
        this.name = name;
        this.value = value;
        this.rawData = rawData;
        this.timeunit = System.currentTimeMillis();
    }
}
