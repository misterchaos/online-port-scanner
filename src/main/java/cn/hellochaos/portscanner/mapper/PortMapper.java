package cn.hellochaos.portscanner.mapper;

import cn.hellochaos.portscanner.entity.Port;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PortMapper extends BaseMapper<Port> {}
