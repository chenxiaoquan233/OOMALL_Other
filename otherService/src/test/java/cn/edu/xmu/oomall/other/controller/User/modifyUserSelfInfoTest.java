package cn.edu.xmu.oomall.other.controller.User;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.UserController;
import cn.edu.xmu.oomall.other.model.vo.User.UserLoginVo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author XQChen
 * @version 创建时间：2020/12/1 下午3:38
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class modifyUserSelfInfoTest {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private String testInput;
    private String expectedOutput;

    public modifyUserSelfInfoTest() {
        try {
            testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/User/modifyUserSelfInfo.json")));
            expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/modifyUserSelfInfo.json")));
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
     * 获取用户信息
     */
    public String getUserSelfInfo(String userName, String password) throws Exception{
        String token = login(userName, password);

        String responseString = this.mvc.perform(get("/users")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        return responseString;
    }


    /***
     * 成功修改个人信息
     * @throws Exception
     */
    @Test
    public void modifyUserSelfInfoTest1() throws Exception{
        String token = login("testuser", "123456");

        String input = JacksonUtil.parseSubnodeToString(testInput, "/1");

        logger.debug("input :" + input);

        String responseString = this.mvc.perform(put("/users")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectString = JacksonUtil.parseSubnodeToString(expectedOutput, "/1/response");

        JSONAssert.assertEquals(responseString, expectString, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.gmtModified", (o1, o2) -> true)));

        String result = getUserSelfInfo("testuser", "123456");

        String expectResult = JacksonUtil.parseSubnodeToString(expectedOutput, "/1/result");

        JSONAssert.assertEquals(result, expectResult, true);
    }

    /***
     * 不修改个人信息
     * @throws Exception
     */
    @Test
    public void modifyUserSelfInfoTest2() throws Exception{
        String token = login("testuser", "123456");

        String input = JacksonUtil.parseSubnodeToString(testInput, "/2");

        String responseString = this.mvc.perform(put("/users")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectString = JacksonUtil.parseSubnodeToString(expectedOutput, "/2/response");

        JSONAssert.assertEquals(responseString, expectString, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.gmtModified", (o1, o2) -> true)));

        String result = getUserSelfInfo("testuser", "123456");

        String expectResult = JacksonUtil.parseSubnodeToString(expectedOutput, "/2/result");

        JSONAssert.assertEquals(result, expectResult, true);
    }

    /***
     * 性别有误
     * @throws Exception
     */
    @Test
    public void modifyUserSelfInfoTest3() throws Exception{
        String token = login("testuser", "123456");

        String input = JacksonUtil.parseSubnodeToString(testInput, "/3");

        this.mvc.perform(put("/users")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest());
    }

    /***
     * 生日范围错误
     * @throws Exception
     */
    @Test
    public void modifyUserSelfInfoTest4() throws Exception{
        String token = login("testuser", "123456");

        String input = JacksonUtil.parseSubnodeToString(testInput, "/4");

        this.mvc.perform(put("/users")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest());
    }

    /***
     * 生日格式错误
     * @throws Exception
     */
    @Test
    public void modifyUserSelfInfoTest5() throws Exception{
        String token = login("testuser", "123456");

        String input = JacksonUtil.parseSubnodeToString(testInput, "/5");

        this.mvc.perform(put("/users")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest());
    }
}
