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
public class AdTest {
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



    /* 查询广告的所有状态·*/
    @Test
    @Order(1)
    public void AdTest1() throws Exception{
        String token = login();
        byte [] retBody = mallClient.get().uri("/advertisement/states").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expect="{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"code\": 0,\n" +
                "            \"name\": \"待审核\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 4,\n" +
                "            \"name\": \"上架\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 6,\n" +
                "            \"name\": \"下架\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONAssert.assertEquals(expect, new String(retBody, "UTF-8"),false);
    }

    /* 获取当前广告列表
     * 数量应该小于等于8*/
    @Test
    @Order(2)
    public void AdTest2() throws Exception{
        String token = login();
        byte [] retBody = mallClient.get().uri("/advertisement/current").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult().getResponseBodyContent();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(retBody);
        List<JsonNode> list=root.findValues("id");
        assert(list.size()<=8);/*当前广告数量小于等于8*/
    }

    /*管理员设置默认广告*/
    @Test
    @Order(3)
    public void AdTest3() throws Exception{
        String token = adminLogin();
        mallClient.put().uri("/shops/0/advertisement/121/default").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功");
    }
}
