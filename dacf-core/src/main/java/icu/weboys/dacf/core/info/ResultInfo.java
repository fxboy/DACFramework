package icu.weboys.dacf.core.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ResultInfo {
    private Integer code;
    private Object datas;
    private String msg;
    private Long timeunit = System.currentTimeMillis();

    public ResultInfo(Integer code, Object datas, String msg) {
        this.code = code;
        this.datas = datas;
        this.msg = msg;
    }

    public ResultInfo(Integer code, Object datas) {
        this.code = code;
        this.datas = datas;
    }

    public ResultInfo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
