package cn.hellochaos.portscanner.task;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ExecutorService;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 扫描线程池
 * @date 2020-05-30 18:40
 */

public class ScanExecutor {

    private static final ExecutorService EXECUTOR = ThreadUtil.newExecutor(10000);

    public static void execute(Runnable command) {
        EXECUTOR.execute(command);
    }
}
