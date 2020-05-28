package cn.hellochaos.portscanner.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 扫描结果
 * @date 2020-05-28 21:16
 */
@Data
public class ScanTask {

    /**
     * ip范围
     */
    private String ip;
    /**
     * 端口范围
     */
    private String port;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 扫描结果
     */
    private List<PortInfo> results;
    /**
     * 启动时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 任务状态
     */
    private String status;

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String RUNNING = "running";

}
