package icu.weboys.dacf.services.socket;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IConnector;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.util.BaseUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import icu.weboys.dacf.core.util.Assert;

public class SocketHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        String remoteName = BaseUtils.getRemoteName(channelHandlerContext);
        ModuleInfo mdi = ObjectContainer.getRemoteModuleInfo(remoteName);
        Assert.notNull(mdi,String.format("Corresponding module information not found,RemoteName %s",remoteName));
        IModule obj = mdi.getModuleObject();
        Assert.notNull(obj,String.format("[%s] The current module information does not contain entity objects %s",mdi.getName(),mdi.getClassName()));
        obj.recv(bytes);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg){
        if (!(msg instanceof ByteBuf)) {
           return;
        }
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0,bytes);
        buf.release();
        String remoteName = BaseUtils.getRemoteName(channelHandlerContext);
        ModuleInfo mdi = ObjectContainer.getRemoteModuleInfo(remoteName);
        Assert.notNull(mdi,String.format("Corresponding module information not found,RemoteName %s",remoteName));
        IModule obj = mdi.getModuleObject();
        Assert.notNull(obj,String.format("[%s] The current module information does not contain entity objects %s",mdi.getName(),mdi.getClassName()));
        obj.recv(bytes);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        String remoteName = BaseUtils.getRemoteName(channelHandlerContext);
        ModuleInfo mdi = ObjectContainer.getRemoteModuleInfo(remoteName);
        Assert.notNull(mdi,String.format("Corresponding module information not found,RemoteName %s",remoteName));
        IConnector obj = mdi.getModuleConnector();
        Assert.notNull(obj,String.format("[%s] The current module information does not contain entity objects %s",mdi.getName(),mdi.getClassName()));
        obj.close();
        super.channelInactive(channelHandlerContext);
    }



}
