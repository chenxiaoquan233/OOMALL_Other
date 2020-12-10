package cn.edu.xmu.oomall.other.controller.User;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.UserController;
import cn.edu.xmu.oomall.other.model.vo.User.UserLoginVo;
import cn.edu.xmu.oomall.other.util.TestStub;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author XQChen
 * @version 创建时间：2020/12/3 上午11:06
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class releaseUserTest {

    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private String expectedOutput;
    private String adminToken;

    public releaseUserTest() throws Exception {
        expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/releaseUser.json")));
        adminToken = TestStub.createToken(1L, 0L, 3600);
    }

    /***
     * 获取token
     */
    public String loginResponse(String userName, String password) throws Exception {
        UserLoginVo vo = new UserLoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);

        String requireJson = JacksonUtil.toJson(vo);

        return this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /***
     * 成功解禁用户
     * @throws Exception
     */
    @Test
    public void releaseUserTest1() throws Exception {
        String response = this.mvc.perform(put("/users/20001/release")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/1/response");

        JSONAssert.assertEquals(expect, response, true);

        String result = loginResponse("banuser", "123456");

        String expectResult = JacksonUtil.parseSubnodeToString(expectedOutput, "/1/result");

        JSONAssert.assertEquals(expectResult, result, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data", (o1, o2) -> true)));
    }

    /***
     * 解禁不存在的用户
     * @throws Exception
     */
    @Test
    public void releaseUserTest2() throws Exception {
        String response = this.mvc.perform(put("/users/99999/release")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/2");

        JSONAssert.assertEquals(expect, response, true);
    }
}
