package cn.edu.xmu.oomall.other.controller.User;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.UserController;
import cn.edu.xmu.oomall.other.util.TestStub;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author XQChen
 * @version 创建时间：2020/12/3 上午10:30
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class findUserInfoTest {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private String expectedOutput;
    private String adminToken;
    public findUserInfoTest() throws Exception {
        expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/findUserInfo.json")));
        adminToken = TestStub.createToken(1L, 0L, 3600);
    }

    /***
     * 正常查找用户
     * @throws Exception
     */
    @Test
    public void findUserInfoTest1() throws Exception {

        String response = this.mvc.perform(get("/users/20000")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/1");

        JSONAssert.assertEquals(expect, response, true);
    }

    /***
     * 查找不存在的用户
     * @throws Exception
     */
    @Test
    public void findUserInfoTest2() throws Exception {

        String response = this.mvc.perform(get("/users/2000000")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/2");

        JSONAssert.assertEquals(expect, response, true);
    }
}
