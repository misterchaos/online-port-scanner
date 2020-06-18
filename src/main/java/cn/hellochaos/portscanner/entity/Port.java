package cn.hellochaos.portscanner.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "port_info")
public class Port extends Model<Port> {
  private static final long serialVersionUID = 1L;
  private String serverName;
  private String info;
  private Integer port;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
