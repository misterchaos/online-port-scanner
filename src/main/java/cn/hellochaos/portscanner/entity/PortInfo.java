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
    private Integer port;

    /**
     * 端口类型
     */
    private String protocol = NONE;
    /**
     * 服务名称
     */
    private String service = UNKNOWN;

    /**
     * 是否开放
     */
    private String status = CLOSE;

    /**
     * 最小端口
     */
    public static final Integer MIN_PORT = 1;
    /**
     * 最大端口
     */
    public static final Integer MAX_PORT = 65535;
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    public static final String TCP_AND_UDP = "TCP,UDP";
    public static final String OPEN = "开启";
    public static final String CLOSE = "关闭";
    public static final String UNKNOWN = "未知";
    public static final String NONE = "无";
}
