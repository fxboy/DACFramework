package icu.weboys.dacf.services;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.RegObject;
import icu.weboys.dacf.core.ThreadContainer;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.inter.IConnector;
import icu.weboys.dacf.core.inter.IModule;
import icu.weboys.dacf.core.util.BaseUtils;
import icu.weboys.dacf.services.config.DACFConfig;
import icu.weboys.dacf.services.init.DACFServiceInit;
import icu.weboys.dacf.services.socket.SocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import icu.weboys.dacf.core.util.Assert;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Log4j2
public class DACFService {
    public static void init(ApplicationContext applicationContext){
        RegObject.temp_applicationContext = applicationContext;
        DACFServiceInit dint = ((DACFServiceInit) applicationContext.getBean("DACFServiceInit"));
        dint.init(applicationContext);
        Map<String, ModuleInfo> modules = ObjectContainer.getRegModuleInfo();
        Integer moduleNumber = modules.size() == 0?null:modules.size();
        Assert.notNull(moduleNumber,String.format("No modules to register"));
        ThreadContainer.connectotThreadPool = new ThreadPoolExecutor(moduleNumber,
                dint.getMaximumPoolSize()+ 1,
                60 * 3,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        socketmessageBus(applicationContext);
        modules.forEach( (k,v) -> {
            try {
              RegObject.regModule(v);
            } catch (ClassNotFoundException e) {
                log.info(String.format("[%s] Module creation failed,exception message: %s",v.getName(),e.getMessage()));
            }
        });
    }


    public static void socketmessageBus(ApplicationContext applicationContext){
        DACFConfig dacfConfig = (DACFConfig) applicationContext.getBean("DACFConfig");
        Integer port = dacfConfig.getServerPort();
        Integer enable = dacfConfig.getSocketServer()?1:null;
        Assert.notNull(enable,String.format("Socket server is not enabled"));
        ThreadContainer.connectorExecute(new Thread(() -> {
            try {
                ServerBootstrap sb = new ServerBootstrap();
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                sb.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 9999)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sh){
                               try{
                                   String remoteName = BaseUtils.getRemoteName(sh.remoteAddress().toString());
                                   ModuleInfo mdi = ObjectContainer.getRemoteModuleInfo(remoteName);
                                   Assert.notNull(mdi,String.format("Corresponding module information not found,RemoteName %s",remoteName));
                                   IConnector obj = mdi.getModuleConnector();
                                   Assert.notNull(obj,String.format("[%s] The current module information does not contain entity objects %s",mdi.getName(),mdi.getClassName()));
                                   sh.pipeline().addLast(new SocketHandler());
                                   obj.connect();
                               }catch (Exception ex){
                                   log.debug(String.format("%s not registered",sh.remoteAddress()));
                                   log.debug(String.format("Socket server has added a new connection,Company Registered Address %s ,But not registered,ex %s",sh.remoteAddress(),ex.getMessage()));
                               }
                                //log.info(String.format("Socket server has added a new connection,Company Registered Address %s",sh.remoteAddress()));
                            }
                        });
                ChannelFuture future  = sb.bind(port).sync();
                if (future.isSuccess()) {
                    log.info(String.format("Socket server started,port:%s",port));
                } else {
                    log.debug("Socket server startup failed");
                    future.cause().printStackTrace();
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.debug(String.format("Socket server startup failed,Exception msg %s",e.getMessage()));
            }
        }));

    }
}
