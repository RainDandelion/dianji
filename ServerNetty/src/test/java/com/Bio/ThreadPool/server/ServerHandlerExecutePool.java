package com.Bio.ThreadPool.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *线程池
 */
public class ServerHandlerExecutePool {

    private ExecutorService executorService;

    public ServerHandlerExecutePool(int maxPoolSize,int QueueSize){
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,
                10L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(QueueSize));
    }


    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }

}
