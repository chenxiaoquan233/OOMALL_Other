package cn.edu.xmu.oomall.other.controller.Favorite;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.UserController;
import cn.edu.xmu.oomall.other.model.vo.User.UserLoginVo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteTest {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(FavoriteTest.class);


    /*succeed*/
    @Test
    public void deleteFavoriteTest() throws Exception{
        String token = new JwtHelper().createToken(1L, -2L, 20000);

        this.mvc.perform(delete("/favorites/9999998")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"));
    }

    /*not existed favoriteId*/
    @Test
    public void deleteFavoriteTest1() throws Exception{
        String token = new JwtHelper().createToken(1L, -2L, 20000);

        this.mvc.perform(delete("/favorites/9999997")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("504"))
                .andExpect(jsonPath("$.errmsg").value("操作的资源id不存在"));
    }

    /*another customer's favorite*/
    @Test
    public void deleteFavoriteTest2() throws Exception{
        String token = new JwtHelper().createToken(1L, -2L, 20000);

        this.mvc.perform(delete("/favorites/9999999")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("505"))
                .andExpect(jsonPath("$.errmsg").value("操作的资源id不是自己的对象"));
    }



}
