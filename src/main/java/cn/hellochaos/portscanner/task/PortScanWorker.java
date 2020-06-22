package cn.hellochaos.portscanner.task;

import cn.hellochaos.portscanner.entity.Port;
import cn.hellochaos.portscanner.entity.PortInfo;
import cn.hellochaos.portscanner.mapper.PortMapper;
import cn.hellochaos.portscanner.util.BeanUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 负责扫描某主机的一个端口
 * @date 2020-05-30 11:36
 */
@Slf4j
public class PortScanWorker implements Runnable {

  private final PortInfo portInfo;

  private final CountDownLatch portCountDownLatch;

  public PortScanWorker(PortInfo portInfo, CountDownLatch portCountDownLatch) {
    this.portInfo = portInfo;
    this.portCountDownLatch = portCountDownLatch;
  }

  @Override
  public void run() {
    scanPort(portInfo);
    complete();
  }

  public static void scanPort(PortInfo portInfo) {
    log.info("正在开始扫描主机{}的{}端口", portInfo.getIp(), portInfo.getPort());

    TimeInterval timer = DateUtil.timer();

    InetAddress ip;
    // 解析ip地址
    try {
      ip = InetAddress.getByName(portInfo.getIp());
    } catch (UnknownHostException e) {
      portInfo.setComplete(true);
      return;
    }
    InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, portInfo.getPort());

    log.info("解析ip耗时{}秒", timer.intervalSecond());
    timer.intervalRestart();
    PortMapper portMapper = BeanUtils.getBean(PortMapper.class);
    Port port = portMapper.selectOne(new QueryWrapper<Port>().eq("port", portInfo.getPort()));
    if (port != null) {
      portInfo.setService(port.getServerName());
      portInfo.setServiceDescription(port.getInfo());
    }

    // 扫描TCP端口
    try {
      Socket socket = new Socket();
      socket.connect(inetSocketAddress,10000);
      portInfo.setProtocol(PortInfo.TCP);
      portInfo.setStatus(PortInfo.OPEN);
    } catch (IOException e) {
      log.info("检测到{}的TCP {}端口不开放", inetSocketAddress.getAddress(), inetSocketAddress.getPort());
    }

    log.info("扫描TCP耗时{}秒", timer.intervalSecond());
    timer.intervalRestart();

    // 扫描UDP端口
    try {
      DatagramSocket datagramSocket = new DatagramSocket();
      String message = "hello";
      DatagramPacket packet =
          new DatagramPacket(message.getBytes(), message.length(), inetSocketAddress);
      datagramSocket.setSoTimeout(1000);
      datagramSocket.send(packet);
      datagramSocket.receive(new DatagramPacket(new byte[0], 0));

      // 如果收到数据报则认为UDP端口打开
      if (PortInfo.OPEN.equals(portInfo.getStatus())) {
        portInfo.setProtocol(PortInfo.TCP_AND_UDP);
      } else {
        portInfo.setProtocol(PortInfo.UDP);
      }
    } catch (SocketException socketException) {
      log.warn("无法创建用于扫描UDP端口的Socket");
    } catch (SocketTimeoutException e) {
      log.warn("UDP响应超时");
    } catch (IOException ioException) {
      log.warn("发送UDP数据包发生异常");
    }

    portInfo.setComplete(true);
    log.info("扫描UDP耗时{}秒", timer.intervalSecond());
  }

  private void complete() {
    log.info("{}:{} 扫描完成!", portInfo.getIp(), portInfo.getPort());
    // 计数器
    portCountDownLatch.countDown();
  }
}
