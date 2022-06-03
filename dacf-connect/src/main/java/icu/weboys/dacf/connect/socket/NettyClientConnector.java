package icu.weboys.dacf.connect.socket;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.abs.AbsConnector;
import icu.weboys.dacf.core.annotation.DACFConnector;
import icu.weboys.dacf.core.ThreadContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j2;
import icu.weboys.dacf.core.util.Assert;

import java.util.concurrent.TimeUnit;

@Log4j2
@DACFConnector
public class NettyClientConnector extends AbsConnector<byte[]> {
    Thread that;
    ChannelFuture future;
    EventLoopGroup group = null;
    Bootstrap client;

    @Override
    public void init(String name) {
        super.init(name);
        client = new Bootstrap();
        group =  new NioEventLoopGroup();
        client.group(group );
        client.channel(NioSocketChannel.class);
        client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ByteBuf[] _bf = Delimiters.lineDelimiter();
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,_bf));
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new IdleStateHandler(30, 40, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        if (msg instanceof ByteBuf) {
                            ByteBuf buf = (ByteBuf) msg;
                            byte[] barray = new byte[buf.readableBytes()];
                            buf.getBytes(0,barray);
                            buf.release();
                            recv(barray);
                        }
                    }
                });
            }
        });

        if(ObjectContainer.get(this.name).getEnable()){
            this.connect();
            return;
        }
        log.info(String.format("[%s] Module not enabled, connector skipped",this.name));
    }

    @Override
    public void connect() {
        try{
            future = client.connect( ObjectContainer.get(this.name).getHost(),  ObjectContainer.get(this.name).getPort()).sync();
        }catch (Exception ex){
            future = null;
            log.debug(String.format("[%s] future client creat ExceptionMessage:%s", ObjectContainer.get(this.name).getName(),ex.getMessage()));
        }
        Assert.notNull(future,String.format("[%s] Connection failed, waiting for the timer to reconnect", ObjectContainer.get(this.name).getName()));
        super.connect();
        ThreadContainer.connectorExecute(new Thread(() -> {
            try {
                future.channel().closeFuture().sync();
                super.connect();
            } catch (InterruptedException ex) {
                log.debug(String.format("[%s] future client start ExceptionMessage:%s", ObjectContainer.get(this.name).getName(),ex.getMessage()));
                connectFlag = false;
            }
        }));
    }

    @Override
    public void reconnect() {
        super.reconnect();
    }

    @Override
    public void close() {
        future.channel().close();
        that.interrupt();
        super.close();
    }

    @Override
    public void recv(byte[] data) {
        super.recv(data);
    }

    @Override
    public void send(byte[] data) {
        if(!connectFlag){
            return;
        }
        future.channel().write(data);
        super.send(data);
    }
}
