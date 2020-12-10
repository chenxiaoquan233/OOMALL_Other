package cn.edu.xmu.oomall.other.controller.User;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.UserController;
import cn.edu.xmu.oomall.other.model.vo.User.UserLoginVo;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author XQChen
 * @version 创建时间：2020/12/2 下午9:13
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class logoutTest {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private String expectedOutput;

    public logoutTest() {
        try {
            expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/logout.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 获取token
     */
    public String login(String userName, String password) throws Exception {
        UserLoginVo vo = new UserLoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);

        String requireJson = JacksonUtil.toJson(vo);
        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson)).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        return  JacksonUtil.parseString(response, "data");
    }

    /***
     * 正常登出
     * @throws Exception
     */
    @Test
    public void logoutTest1() throws Exception {
        String token = login("testuser", "123456");

        String response = this.mvc.perform(get("/users/logout")
                .header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/1");

        JSONAssert.assertEquals(expect, response,  true);
    }
}
