package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.LoginVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;


/**
 * @author 3211 JiangXiao
 * @date 2020-12-12
 */

@SpringBootTest(classes = TestApplication.class)
@Slf4j
public class FavouriteTest {
    //@Value("${public-test.managementgate}")
    private String managementGate;

    //@Value("${public-test.mallgate}")
    private String mallGate="localhost:9999";

    private WebTestClient mallClient;

    private WebTestClient manageClient;

    @BeforeEach
    public void setUp(){
        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://" + managementGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://"+mallGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    /*default login*/
    private String login() throws Exception{
        return login("19769355952","123456",false);
    }

    /*default admin login*/
    private String adminLogin() throws Exception{
        return login("13088admin","123456",true);
    }

    private String login(String userName, String password,Boolean ifAdmin) throws Exception {
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);
        byte[] ret = (ifAdmin?manageClient:mallClient).post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");
    }


    /* 正常获取收藏
     * 由于后面新添加的数据可能产生变化，对于总数、总页数、list只比较存在*/
    @Test
    @Order(4)
    public void FavoriteTest1() throws Exception{
        String token = login();
        mallClient.get().uri("/favorites?page=1&pageSize=1").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.total").exists()
                .jsonPath("$.data.pages").exists()
                .jsonPath("$.data.pageSize").isEqualTo("1")
                .jsonPath("$.data.page").isEqualTo("1")
                .jsonPath("$.data.list").exists();
    }

    /* 不输入page获取收藏
     * 此时应该按照默认page返回*/
    @Test
    @Order(5)
    public void FavoriteTest2() throws Exception{
        String token = login();
        mallClient.get().uri("/favorites?pageSize=1").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.total").exists()
                .jsonPath("$.data.pages").exists()
                .jsonPath("$.data.pageSize").isEqualTo("1")
                .jsonPath("$.data.page").exists()
                .jsonPath("$.data.list").exists();
    }

    /* 不输入pageSize获取收藏
     * 此时应该按照默认pageSize返回*/
    @Test
    @Order(6)
    public void FavoriteTest3() throws Exception{
        String token = login();
        mallClient.get().uri("/favorites?page=1").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.total").exists()
                .jsonPath("$.data.pages").exists()
                .jsonPath("$.data.pageSize").exists()
                .jsonPath("$.data.page").isEqualTo("1")
                .jsonPath("$.data.list").exists();
    }

    /* page超限获取收藏
     * 此时应该返回最后一页*/
    @Test
    @Order(7)
    public void FavoriteTest4() throws Exception{
        String token = login();
        mallClient.get().uri("/favorites?page=2&pageSize=999999999").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.total").exists()
                .jsonPath("$.data.pages").exists()
                .jsonPath("$.data.pageSize").isEqualTo("999999999")
                .jsonPath("$.data.page").isEqualTo("1")
                .jsonPath("$.data.list").exists();
    }

    /*正常添加收藏*/
    @Test
    @Order(8)
    public void FavoriteTest5() throws Exception{
        String token = login();
        mallClient.post().uri("favorites/goods/273").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.id").exists()
                .jsonPath("$.data.goodsSku").exists()
                .jsonPath("$.data.goodsSku.id").isEqualTo("273")
                .jsonPath("$.data.gmtCreate").exists();

    }



    /*重复添加收藏
    * 此时不应插入新收藏，应将原重复收藏返回*/
    @Test
    @Order(9)
    public void FavoriteTest6() throws Exception{
        String token = login();
        byte[] ret = mallClient.post().uri("favorites/goods/273").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data.id").exists()
                .jsonPath("$.data.goodsSku").exists()
                .jsonPath("$.data.goodsSku.id").isEqualTo("273")
                .jsonPath("$.data.gmtCreate").exists()
                .returnResult()
                .getResponseBodyContent();
    }

    /*添加skuId格式不正确的收藏
    * 根据通用错误处理，返回400，errno为503，errmsg自定义*/
    @Test
    @Order(10)
    public void FavoriteTest7() throws Exception{
        String token = login();
        mallClient.post().uri("favorites/goods/-1").header("authorization",token)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").exists();

    }

    /* 正常删除收藏*/
    @Test
    @Order(11)
    public void FavoriteTest8() throws Exception{
        String token = login();
        mallClient.delete().uri("/favorites/3735448").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage());
    }

    /* 删除不存在的收藏
    * 根据通用错误处理，返回404，errno为504，errmsg自定义*/
    @Test
    @Order(12)
    public void FavoriteTest9() throws Exception{
        String token = login();
        mallClient.delete().uri("/favorites/3735448").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").exists();
    }

}
