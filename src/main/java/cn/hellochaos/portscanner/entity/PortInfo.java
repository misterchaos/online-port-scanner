package cn.hellochaos.portscanner.entity;

import lombok.Data;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 端口信息
 * @date 2020-05-28 21:17
 */
@Data
public class PortInfo {
    /**
     * ip
     */
    private String ip;
    /**
     * 端口
     */
    private String port;
    /**
     * 端口类型
     */
    private String type;
    /**
     * 服务名称
     */
    private String service;

    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
}
