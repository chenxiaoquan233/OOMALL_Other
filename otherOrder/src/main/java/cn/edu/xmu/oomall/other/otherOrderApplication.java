package cn.edu.xmu.oomall.other;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/30 0:14
 **/
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.oomall.other"})
@EnableScheduling
@MapperScan("cn.edu.xmu.oomall.other.mapper")
public class otherOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(otherOrderApplication.class, args);
    }
}
