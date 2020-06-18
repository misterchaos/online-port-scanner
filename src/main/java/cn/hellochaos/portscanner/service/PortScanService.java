package cn.hellochaos.portscanner.service;

import cn.hellochaos.portscanner.entity.PortInfo;
import cn.hellochaos.portscanner.entity.ScanTask;

import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 端口扫描服务
 * @date 2020-05-28 19:56
 */
public interface PortScanService {

  /**
   * 提交扫描任务
   *
   * @param scanTask 扫描任务
   * @return 返回任务id
   */
  String submitScanTask(ScanTask scanTask);

  /**
   * 获取扫描任务
   *
   * @param taskId 任务id
   * @return 返回任务
   */
  ScanTask getScanTask(String taskId);

  /**
   * 快速扫描任务
   *
   * @param portInfo 任务
   * @return 结果
   */
  PortInfo submitSimpleScanTask(PortInfo portInfo);

  /**
   * 查询扫描任务列表
   *
   * @return 返回任务列表
   */
  List<ScanTask> listScanTask();
}
