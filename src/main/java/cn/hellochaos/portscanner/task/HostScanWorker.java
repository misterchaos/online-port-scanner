package cn.hellochaos.portscanner.task;

import cn.hellochaos.portscanner.entity.HostInfo;
import cn.hellochaos.portscanner.entity.PortInfo;
import cn.hellochaos.portscanner.exception.bizException.BizException;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 负责扫描一个主机的所有端口
 * @date 2020-05-30 11:29
 */
@Slf4j
public class HostScanWorker implements Runnable {

    private final HostInfo hostInfo;

    private final CountDownLatch hostCountDownLatch;

    private int minPort = PortInfo.MIN_PORT;

    private int maxPort = PortInfo.MAX_PORT;

    public HostScanWorker(HostInfo hostInfo, CountDownLatch hostCountDownLatch) {
        this.hostInfo = hostInfo;
        this.hostCountDownLatch = hostCountDownLatch;
    }

    public HostScanWorker(HostInfo hostInfo, CountDownLatch hostCountDownLatch, int minPort, int maxPort) {
        this.hostInfo = hostInfo;
        this.hostCountDownLatch = hostCountDownLatch;
        if (minPort > maxPort || !NetUtil.isValidPort(minPort) || !NetUtil.isValidPort(maxPort)) {
            throw new BizException("不正确的端口范围！");
        }
        this.minPort = minPort;
        this.maxPort = maxPort;
    }

    @Override
    public void run() {


        List<PortInfo> portInfoList = new LinkedList<>();
        hostInfo.setPortList(portInfoList);

        //解析域名
        if (!StrUtil.isBlank(hostInfo.getHost())) {
            hostInfo.setIp(NetUtil.getIpByHost(hostInfo.getHost()));
        }

        //启动端口扫描
        log.info("正在开始扫描主机 {} ,端口范围: {} - {} ", hostInfo.getIp(), minPort, maxPort);


        for (int i = minPort; i <= maxPort; i++) {
            PortInfo portInfo = new PortInfo();
            portInfo.setIp(hostInfo.getIp());
            portInfo.setPort(i);
            portInfoList.add(portInfo);
            ScanExecutor.execute(new PortScanWorker(portInfo, hostCountDownLatch));
        }


    }

}
