package cn.hellochaos.portscanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/static/**")
        .addResourceLocations("classpath:.static")
        .setCachePeriod(31556926);
  }
  /** 前端跨域 */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        // 设置允许跨域的路径
        .addMapping("/**")
        // 设置允许跨域请求的域名
        .allowedOrigins("*")
        // 是否允许证书 不再默认开启
        .allowCredentials(true)
        // 设置允许的方法
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        // 跨域允许时间
        .maxAge(3600);
  }
}
