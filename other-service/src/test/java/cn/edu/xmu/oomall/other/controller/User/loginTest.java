package cn.edu.xmu.oomall.other.controller.User;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.UserController;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author XQChen
 * @version 创建时间：2020/12/2 下午8:33
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class loginTest {

    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private String testInput;
    private String expectedOutput;

    public loginTest() {
        try {
            testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/User/login.json")));
            expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/login.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 成功登录
     * @throws Exception
     */
    @Test
    public void loginTest1() throws Exception {

        String input = JacksonUtil.parseSubnodeToString(testInput, "/1");

        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/1");

        JSONAssert.assertEquals(expect, response,  new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data", (o1, o2) -> true)));
    }

    /***
     * 密码错误
     * @throws Exception
     */
    @Test
    public void loginTest2() throws Exception {

        String input = JacksonUtil.parseSubnodeToString(testInput, "/2");

        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/2");

        JSONAssert.assertEquals(expect, response,  true);
    }

    /***
     * 用户被禁止登录
     * @throws Exception
     */
    @Test
    public void loginTest3() throws Exception {

        String input = JacksonUtil.parseSubnodeToString(testInput, "/3");

        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/3");

        JSONAssert.assertEquals(expect, response,  true);
    }

    /***
     * 用户不存在
     * @throws Exception
     */
    @Test
    public void loginTest4() throws Exception {

        String input = JacksonUtil.parseSubnodeToString(testInput, "/4");

        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/4");

        JSONAssert.assertEquals(expect, response, true);
    }

    /***
     * 不输入用户名
     * @throws Exception
     */
    @Test
    public void loginTest5() throws Exception {

        String input = JacksonUtil.parseSubnodeToString(testInput, "/5");

        this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest());
    }

    /***
     * 不输入密码
     * @throws Exception
     */
    @Test
    public void loginTest6() throws Exception {

        String input = JacksonUtil.parseSubnodeToString(testInput, "/6");

        this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest());
    }
}
