package cn.edu.xmu.other;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.other"})
@MapperScan("cn.edu.xmu.other.mapper")
public class OtherServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OtherServiceApplication.class, args);
	}
}
