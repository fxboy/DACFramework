package icu.weboys.dacf.services.socket;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.info.ModuleInfo;
import icu.weboys.dacf.inter.IModule;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.Assert;

public class SocketHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        String[] sn = channelHandlerContext.channel().remoteAddress().toString().split("/");
        String remoteName = sn.length > 1?sn[1]:sn[0];
        ModuleInfo mdi = ObjectContainer.getRegRemoteModuleInfo().get(remoteName);
        Assert.notNull(mdi,String.format("Corresponding module information not found,RemoteName %s",remoteName));
        IModule obj = mdi.getModuleObject();
        Assert.notNull(obj,String.format("[%s] The current module information does not contain entity objects %s",mdi.getName(),mdi.getModuleName()));
        obj.recv(bytes);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        if (!(msg instanceof ByteBuf)) {
           return;
        }
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0,bytes);
        buf.release();
        String[] sn = ctx.channel().remoteAddress().toString().split("/");
        String remoteName = sn.length > 1?sn[1]:sn[0];
        ModuleInfo mdi = ObjectContainer.getRegRemoteModuleInfo().get(remoteName);
        Assert.notNull(mdi,String.format("Corresponding module information not found,RemoteName %s",remoteName));
        IModule obj = mdi.getModuleObject();
        Assert.notNull(obj,String.format("[%s] The current module information does not contain entity objects %s",mdi.getName(),mdi.getModuleName()));
        obj.recv(bytes);
    }
}
