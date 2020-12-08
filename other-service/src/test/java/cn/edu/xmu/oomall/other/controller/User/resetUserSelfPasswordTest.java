package cn.edu.xmu.oomall.other.controller.User;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.util.TestStub;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author XQChen
 * @version 创建时间：2020/12/8 下午8:51
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = OtherServiceApplication.class)
public class resetUserSelfPasswordTest {

    @Autowired
    private MockMvc mvc;

    private String testInput;
    private String expectedOutput;

    public resetUserSelfPasswordTest() {
        try {
            this.testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/User/resetUserSelfPassword.json")));
            this.expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/resetUserSelfPassword.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 用户成功重置密码
     * @throws Exception
     */
    @Test
    public void resetUserSelfPasswordTest1() throws Exception{
        String token = TestStub.createToken(1L, 0L, 2000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/1");

        String response = this.mvc.perform(put("/users/password/reset")
                .header("authorization", token)
                .content(input).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/1");

        JSONAssert.assertEquals(expect, response, true);
    }

    /***
     * 邮箱不正确
     * @throws Exception
     */
    @Test
    public void resetUserSelfPasswordTest2() throws Exception{
        String token = TestStub.createToken(1L, 0L, 2000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/2");

        String response = this.mvc.perform(put("/users/password/reset")
                .header("authorization", token)
                .content(input).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/2");

        JSONAssert.assertEquals(expect, response, true);
    }

    /***
     * 用户名不存在
     * @throws Exception
     */
    @Test
    public void resetUserSelfPasswordTest3() throws Exception{
        String token = TestStub.createToken(1L, 0L, 2000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/3");

        String response = this.mvc.perform(put("/users/password/reset")
                .header("authorization", token)
                .content(input).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/3");

        JSONAssert.assertEquals(expect, response, true);
    }
}
