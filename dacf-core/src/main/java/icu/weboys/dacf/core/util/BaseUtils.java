package icu.weboys.dacf.core.util;

import io.netty.channel.ChannelHandlerContext;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;

public class BaseUtils {
    public static String getRemoteName(ChannelHandlerContext channelHandlerContext){
        return getRemoteName(channelHandlerContext.channel().remoteAddress().toString());
    }

    public static String getRemoteName(String address){
        String[] sn =address.split("/");
        return sn.length > 1?sn[1]:sn[0];
    }

    public static String reword(String wd){
        String[] word = wd.replaceAll(" ","").split("");
        String unit = String.format("%s",System.currentTimeMillis());
        String[] ui = unit.split("");
        StringBuilder sb = new StringBuilder();
        for (String s : ui) {
            sb.append(word[Integer.parseInt(s)]);
        }
        return sb.toString();
    }

    public static String getLocalV4IP(){
        try{
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    public static String getLocalV6IP(){
        try{
            return Inet6Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
}
