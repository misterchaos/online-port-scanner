package cn.hellochaos.portscanner.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** @ClassName BeanUtils @Description bean工具类 @Author huange7 @Date 2020-05-22 10:20 @Version 1.0 */
public class BeanUtils {

  /** 将管理上下文的applicationContext设置成静态变量，供全局调用 */
  public static ConfigurableApplicationContext applicationContext =
      new AnnotationConfigApplicationContext();

  /** 定义一个获取已经实例化bean的方法 */
  public static <T> T getBean(Class<T> c) {
    return applicationContext.getBean(c);
  }
}
