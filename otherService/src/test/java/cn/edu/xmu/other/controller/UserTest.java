package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.other.OtherServiceApplication;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author XQChen
 * @version 创建时间：2020/11/30 下午1:31
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllUserStateTest(){};

    private static final Logger logger = LoggerFactory.getLogger(OtherController.class);

    private String testInput;
    private String expectedOutput;

    public UserTest() {
        try {
            testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/User.json")));
            expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 注册用户成功测试
     * @throws Exception
     */
    @Test
    public void signUpUserTest1() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "signUp1");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectString = JacksonUtil.parseSubnodeToString(expectedOutput, "signUp1");

        JSONAssert.assertEquals(responseString, expectString, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.id", (o1, o2) -> true),
                new Customization("data.gmtCreate", (o1, o2) -> true)));
    }

    /***
     * 用户名已被注册
     * @throws Exception
     */
    @Test
    public void signUpUserTest2() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "signUp2");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "signUp2");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

}
