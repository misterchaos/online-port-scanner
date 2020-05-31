package cn.hellochaos.portscanner.task;

import cn.hellochaos.portscanner.entity.HostInfo;
import cn.hellochaos.portscanner.entity.ScanTask;
import cn.hellochaos.portscanner.exception.bizException.BizException;
import cn.hellochaos.portscanner.util.IpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 扫描执行任务
 * @date 2020-05-28 21:49
 */
@Slf4j
public class ScanMaster implements Runnable {


    /**
     * 任务列表
     */
    private static final Map<String, ScanTask> TASK_MAP = new ConcurrentHashMap<>();

    /**
     * countDownLatch列表
     */
    private static final Map<String, CountDownLatch> COUNT_DOWN_LATCH_MAP = new ConcurrentHashMap<>();

    private final ScanTask scanTask;

    private CountDownLatch portCountDownLatch = null;

    private int countNumber;

    private final BigInteger startIp;
    private final BigInteger endIp;

    TimeInterval timeInterval;


    public static ScanTask getScanTask(String taskId) {
        ScanTask scanTask = TASK_MAP.get(taskId);
        CountDownLatch countDownLatch = COUNT_DOWN_LATCH_MAP.get(taskId);
        double count = countDownLatch.getCount();
        double countNumber = scanTask.getCountNumber();
        if (count != 0) {
            String rate = NumberUtil.formatPercent((countNumber - count) / countNumber, 0);
            scanTask.setStatus(rate);
        }
        return scanTask;
    }

    public ScanMaster(ScanTask scanTask) {

        scanTask.setStartTime(LocalDateTime.now());
        timeInterval = DateUtil.timer();
        scanTask.setStatus(ScanTask.RUNNING);
        //每个主机的端口数
        countNumber = (scanTask.getMaxPort() - scanTask.getMinPort()) + 1;

        //解析IP
        startIp = IpUtil.stringToBigInt(scanTask.getStartIp());
        endIp = IpUtil.stringToBigInt(scanTask.getEndIp());
        BigInteger ipNumber = endIp.subtract(startIp).add(BigInteger.ONE);
        if (ipNumber.compareTo(BigInteger.ZERO) < 0) {
            TASK_MAP.remove(scanTask.getTaskId());
            throw new BizException("无效的地址段！");
        }

        //计算总端口数
        countNumber = ipNumber.multiply(BigInteger.valueOf(countNumber)).intValue();
        scanTask.setCountNumber(countNumber);


        log.info("创建扫描任务: taskId = {} ,ip = {}-{} ,port = {}-{}",
                scanTask.getTaskId(), scanTask.getStartIp(), scanTask.getEndIp(),
                scanTask.getMinPort(), scanTask.getMinPort());
        this.scanTask = scanTask;
        TASK_MAP.put(scanTask.getTaskId(), scanTask);
    }


    @Override
    public void run() {

        List<HostInfo> hostInfoList = new LinkedList<>();

        //扫描主机
        portCountDownLatch = ThreadUtil.newCountDownLatch(scanTask.getCountNumber());
        for (BigInteger i = startIp; i.compareTo(endIp) <= 0; i = i.add(BigInteger.ONE)) {
            HostInfo hostInfo = new HostInfo();
            hostInfo.setIp(IpUtil.bigIntToString(i));
            ScanExecutor.execute(new HostScanWorker(hostInfo, portCountDownLatch,
                    scanTask.getMinPort(), scanTask.getMaxPort()));
            hostInfoList.add(hostInfo);
        }

        //加入集合
        scanTask.setResults(hostInfoList);
        COUNT_DOWN_LATCH_MAP.put(scanTask.getTaskId(), portCountDownLatch);

        //等待执行完成
        try {
            portCountDownLatch.await(1000, TimeUnit.SECONDS);
            scanTask.setEndTime(LocalDateTime.now());
            if (timeInterval.intervalSecond() < 60) {
                scanTask.setRunTime(timeInterval.intervalSecond() + " seconds");
            } else if (timeInterval.intervalMinute() < 60) {
                scanTask.setRunTime(timeInterval.intervalMinute() + " minutes");
            } else if (timeInterval.intervalHour() < 24) {
                scanTask.setRunTime(timeInterval.intervalHour() + " hours");
            }
            scanTask.setStatus(ScanTask.SUCCESS);
        } catch (InterruptedException e) {
            scanTask.setStatus(ScanTask.OVERTIME);
        }

    }


}
