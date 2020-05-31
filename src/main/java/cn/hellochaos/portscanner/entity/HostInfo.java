package cn.hellochaos.portscanner.entity;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 主机信息
 * @date 2020-05-30 11:23
 */
@Data
public class HostInfo {

    /**
     * ip
     */
    private String ip;

    /**
     * 域名
     */
    private String host;

    /**
     * 端口集合
     */
    private List<PortInfo> portList;

    /**
     * 最小地址和最大地址的分隔符
     */
    public static final String SEPARATOR = "-";
}
