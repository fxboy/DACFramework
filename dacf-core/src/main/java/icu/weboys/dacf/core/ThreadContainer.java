package icu.weboys.dacf.core;

import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;

public class ThreadContainer {
    public static ExecutorService connectotThreadPool = null;

    public static void connectorExecute(Thread thread){
        Assert.notNull(connectotThreadPool,"Connector thread pool not initialized");
        connectotThreadPool.execute(thread);
    }
}
