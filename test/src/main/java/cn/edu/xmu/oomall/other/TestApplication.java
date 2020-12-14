package cn.edu.xmu.oomall.other;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author XQChen
 * @version 创建时间：2020/12/14 下午2:47
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TestApplication{
    public static void main(String[] args) {
        SpringApplication.run(cn.edu.xmu.oomall.other.TestApplication.class, args);
    }
}
