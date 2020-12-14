package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.other.OtherServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author XQChen
 * @version 创建时间：2020/12/14 下午9:39
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@Transactional
public class DubboTest {
    @Autowired
    AftersaleService aftersaleService;

    @Test
    public void test() {
        aftersaleService.test();
    }
}
