package cn.hellochaos.portscanner.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 扫描线程池
 * @date 2020-05-30 18:40
 */
@Configuration
public class ScanExecutor {

  @Autowired private static final ThreadPoolTaskExecutor EXECUTOR;

  static {
    // 使用VisiableThreadPoolTaskExecutor
    EXECUTOR = new ThreadPoolTaskExecutor();
    // 配置核心线程数
    EXECUTOR.setCorePoolSize(800);
    // 配置最大线程数
    EXECUTOR.setMaxPoolSize(800);
    // 配置线程池中的线程的名称前缀
    EXECUTOR.setThreadNamePrefix("async-service-");
    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
    EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 执行初始化
    EXECUTOR.initialize();
  }

  public static void submit(Runnable command) {
    EXECUTOR.execute(command);
  }
}
