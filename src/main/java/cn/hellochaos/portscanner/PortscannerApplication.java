package cn.hellochaos.portscanner;

import cn.hellochaos.portscanner.util.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PortscannerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(PortscannerApplication.class, args);
        BeanUtils.applicationContext = applicationContext;
    }

}
