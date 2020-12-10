package cn.edu.xmu.oomall.other.controller.Favorite;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
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


/*运行本test之前请先运行sql
* 此时9991127用户仅有一条9999997的favourite记录
* 9991128用户仅有一条9999998的favourite记录
* 不存在9999999的favourite的记录*/
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteTest {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(FavoriteTest.class);

    /*normal get favourite
    * 由于考虑到sku list来源于商品模块，可能产生差异，因此不比较sku list*/
    @Test
    public void FavouriteTest1() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(get("/favorites?page=1&pageSize=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.total").value("1"))
                .andExpect(jsonPath("$.data.pages").value("1"))
                .andExpect(jsonPath("$.data.pageSize").value("1"))
                .andExpect(jsonPath("$.data.page").value("1"));
    }

    /*normal get favourite without page
     * 此时应该按照默认page返回*/
    @Test
    public void FavouriteTest2() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(get("/favorites?pageSize=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.total").value("1"))
                .andExpect(jsonPath("$.data.pages").value("1"))
                .andExpect(jsonPath("$.data.pageSize").value("1"));
    }

    /*normal get favourite without pageSize
     * 此时应该按照默认pageSize返回*/
    @Test
    public void FavouriteTest3() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(get("/favorites?page=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.total").value("1"))
                .andExpect(jsonPath("$.data.pages").value("1"))
                .andExpect(jsonPath("$.data.page").value("1"));
    }

    /*normal get favourite with out of boundary page
     * 此时应该按照最后一页返回*/
    @Test
    public void FavouriteTest4() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(get("/favorites?page=9999999&pageSize=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.total").value("1"))
                .andExpect(jsonPath("$.data.pages").value("1"))
                .andExpect(jsonPath("$.data.page").value("1"))
                .andExpect(jsonPath("$.data.pageSize").value("1"));
    }



    /*normal delete */
    @Test
    public void FavoriteTest5() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(delete("/favorites/9999997")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"));
    }

    /*delete not existed favoriteId*/
    @Test
    public void FavoriteTest6() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(delete("/favorites/9999998")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"));
//                .andExpect(jsonPath("$.errno").value("504"))
//                .andExpect(jsonPath("$.errmsg").value("操作的资源id不存在"));
    }

    /*delete another customer's favorite
    * 删除别人的收藏被禁止，另一个用户的收藏记录应不被影响*/
    @Test
    public void FavoriteTest7() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);
        String token2 = new JwtHelper().createToken(9991128L, -2L, 20000);

        this.mvc.perform(delete("/favorites/9999999")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"));
//                .andExpect(jsonPath("$.errno").value("505"))
//                .andExpect(jsonPath("$.errmsg").value("操作的资源id不是自己的对象"));

        this.mvc.perform(get("/favorites?page=1&pageSize=1")
                .header("authorization", token2)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.data.total").value("1"));
    }

    /*normal add favourite
    * 需要sku表中有一条id为1的记录，可以任意修改该id
    * 添加后有两条*/
    @Test
    public void FavoriteTest8() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(post("/favorites/goods/2")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"));

        this.mvc.perform(get("/favorites?page=1&pageSize=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.data.total").value("2"));
    }

    /*add favourite with wrong sku id
     * 添加不合法的sku，应禁止添加，添加后仍只有一条记录*/
    @Test
    public void FavoriteTest9() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(post("/favorites/goods/-1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""));

        this.mvc.perform(get("/favorites?page=1&pageSize=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.data.total").value("1"));
    }

    /* add repeat favourite
     * 应禁止重复添加，添加后仍只有一条记录*/
    @Test
    public void FavoriteTest10() throws Exception{
        String token = new JwtHelper().createToken(9991127L, -2L, 20000);

        this.mvc.perform(post("/favorites/goods/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.errno").value("0"))
                .andExpect(jsonPath("$.errmsg").value("成功"));

        this.mvc.perform(get("/favorites?page=1&pageSize=1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(""))
                .andExpect(jsonPath("$.data.total").value("1"));
    }

}
