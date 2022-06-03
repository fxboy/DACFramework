package icu.weboys.dacf.api.exce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthHandler {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Value("${dacf.app.id}")
    String appid;
    @Value("${dacf.app.key}")
    String appkey;

    public boolean bs() {
        String id = request.getHeader("appid");
        String key = request.getHeader("appkey");
        if(appid.equals(id) && appkey.equals(appkey)){
            return true;
        }
        return false;
    }
}
