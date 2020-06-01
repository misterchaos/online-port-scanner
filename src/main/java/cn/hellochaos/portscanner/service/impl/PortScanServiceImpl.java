package cn.hellochaos.portscanner.service.impl;

import cn.hellochaos.portscanner.entity.PortInfo;
import cn.hellochaos.portscanner.entity.ScanTask;
import cn.hellochaos.portscanner.exception.bizException.BizException;
import cn.hellochaos.portscanner.service.PortScanService;
import cn.hellochaos.portscanner.task.PortScanWorker;
import cn.hellochaos.portscanner.task.ScanMaster;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
    public String submitScanTask(ScanTask scanTask) {
        //初始化任务
        scanTask.setTaskId(IdUtil.randomUUID().replace("-", ""));
        ThreadUtil.execute(new ScanMaster(scanTask));
        return scanTask.getTaskId();
    }

    /**
     * 获取扫描任务
     *
     * @param taskId 任务id
     * @return 返回任务
     */
    @Override
    public ScanTask getScanTask(String taskId) {
        return ScanMaster.getScanTask(taskId);
    }

    @Override
    public PortInfo submitSimpleScanTask(PortInfo portInfo) {
        if (StrUtil.isBlank(portInfo.getIp())) {
            throw new BizException("请输入正确的IP地址或域名!");
        }
        if (!NetUtil.isValidPort(portInfo.getPort())) {
            throw new BizException("请输入有效的端口(1-65536)!");
        }
        PortScanWorker.scanPort(portInfo);
        return portInfo;
    }
}
