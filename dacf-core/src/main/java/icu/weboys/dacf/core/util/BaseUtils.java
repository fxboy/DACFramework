package icu.weboys.dacf.core.util;

import io.netty.channel.ChannelHandlerContext;

public class BaseUtils {
    public static String getRemoteName(ChannelHandlerContext channelHandlerContext){
        return getRemoteName(channelHandlerContext.channel().remoteAddress().toString());
    }

    public static String getRemoteName(String address){
        String[] sn =address.split("/");
        return sn.length > 1?sn[1]:sn[0];
    }
}
