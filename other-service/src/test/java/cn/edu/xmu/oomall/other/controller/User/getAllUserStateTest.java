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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author XQChen
 * @version 创建时间：2020/12/8 下午4:34
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = OtherServiceApplication.class)
public class getAllUserStateTest {

    @Autowired
    private MockMvc mvc;

    private String expectedOutput;

    public getAllUserStateTest() {
        try {
            this.expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User/getAllUserState.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllUserStateTest1() throws Exception{

        String token = TestStub.createToken(1L, 0L, 2000);

        String response = this.mvc.perform(get("/users/states")
                .header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/1");

        JSONAssert.assertEquals(expect, response, true);
    }
}
