package cn.hellochaos.portscanner.service.impl;

import cn.hellochaos.portscanner.entity.ScanTask;
import cn.hellochaos.portscanner.service.PortScanService;
import cn.hellochaos.portscanner.task.PortScanner;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-05-28 21:33
 */
@Service
@Slf4j
public class PortScanServiceImpl implements PortScanService {


    /**
     * 提交扫描任务
     *
     * @param scanTask 扫描任务
     */
    @Override
    public void submitScanTask(ScanTask scanTask) {
        ThreadUtil.execute(new PortScanner(scanTask));
    }

    /**
     * 获取扫描任务
     *
     * @param taskId 任务id
     * @return 返回任务
     */
    @Override
    public ScanTask getScanTask(String taskId) {
        return PortScanner.getScanTask(taskId);
    }
}
