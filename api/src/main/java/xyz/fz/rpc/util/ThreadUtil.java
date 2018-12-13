package xyz.fz.rpc.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtil {

    private static final int cpuCores = Runtime.getRuntime().availableProcessors();

    private static final BasicThreadFactory basicThreadFactory = new BasicThreadFactory.Builder().namingPattern("method-invoke-pool-%d").daemon(true).build();

    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(cpuCores * 2, basicThreadFactory);

    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
