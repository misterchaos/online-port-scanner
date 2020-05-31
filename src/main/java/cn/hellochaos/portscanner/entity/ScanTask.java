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
     * 任务id
     */
    private String taskId;

    /**
     * 起始IP
     */
    private String startIp;

    /**
     * 终止IP
     */
    private String endIp;

    /**
     * 最小端口
     */
    private int minPort = PortInfo.MIN_PORT;

    /**
     * 最大端口
     */
    private int maxPort = PortInfo.MAX_PORT;

    /**
     * 启动时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 结果
     */
    private List<HostInfo> results;

    /**
     * 任务状态
     */
    private String status = ScanTask.QUEUING;

    /**
     * 总共扫描的端口个数
     */
    private int countNumber;

    /**
     * 执行耗时
     */
    private String runTime;

    public static final String QUEUING = "queuing";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String RUNNING = "running";
    public static final String OVERTIME = "overtime";

}
