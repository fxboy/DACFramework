package icu.weboys.dacf.services;

import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.RegObject;
import icu.weboys.dacf.core.ThreadContainer;
import icu.weboys.dacf.info.ModuleInfo;
import icu.weboys.dacf.inter.IModule;
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
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Log4j2
public class DACFService {
    public static void init(ApplicationContext applicationContext){
        ((DACFServiceInit) applicationContext.getBean("DACFServiceInit")).init(applicationContext);
        Map<String, ModuleInfo> modules = ObjectContainer.getRegModuleInfo();
        Integer moduleNumber = modules.size() == 0?null:modules.size();
        Assert.notNull(moduleNumber,String.format("No modules to register"));
        ThreadContainer.connectotThreadPool = new ThreadPoolExecutor(moduleNumber,
                moduleNumber * 2 + 1,
                60 * 3,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        socketmessageBus(applicationContext);
        modules.forEach( (k,v) -> {
            try {
                String beanName = "module_" + v.getName();
                String packageName = ObjectContainer.getModuleClass().get(v.getModuleName());
                Assert.notNull(packageName,String.format("Module object named %s not found",v.getModuleName()));
                Class cz = Class.forName(packageName);
                RegObject.createBean(applicationContext,beanName,cz);
                v.setModuleObject((IModule) applicationContext.getBean(beanName));
                v.getModuleObject().init(v);
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
                                sh.pipeline().addLast(new SocketHandler());
                                log.info(String.format("Socket server has added a new connection,Company Registered Address %s",sh.remoteAddress()));
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
