package cn.hellochaos.portscanner.task;

import cn.hellochaos.portscanner.entity.ScanTask;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 扫描执行任务
 * @date 2020-05-28 21:49
 */
@Slf4j
public class PortScanner implements Runnable {

    /**
     * 任务列表
     */
    private static final Map<String, ScanTask> TASK_MAP = new ConcurrentHashMap<>();


    private ScanTask scanTask;

    public static ScanTask getScanTask(String taskId) {
        return TASK_MAP.get(taskId);
    }

    public PortScanner(ScanTask scanTask) {
        //初始化任务
        scanTask.setTaskId(IdUtil.randomUUID());
        scanTask.setStartTime(LocalDateTime.now());
        scanTask.setStatus(ScanTask.RUNNING);
        log.info("创建扫描任务: taskId = {} ,ip = {} ,port = {}",
                scanTask.getTaskId(), scanTask.getIp(), scanTask.getPort());
        this.scanTask = scanTask;
        TASK_MAP.put(scanTask.getTaskId(), scanTask);
    }


    @Override
    public void run() {

    }
}
