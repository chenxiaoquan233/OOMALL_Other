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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;




@SpringBootTest(classes = TestApplication.class)
public class PassedPublicTest {
    //@Value("${public-test.managementgate}")
    private String managementGate="localhost:9999";

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
        byte[] ret = (ifAdmin?manageClient.post().uri("/adminusers/login"):mallClient.post().uri("/users/login"))
                .bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");
    }

    /**
     * 获得广告的所有状态
     *
     * @author 24320182203175 陈晓如
     */
    @Test
    public void advertiseTest3() throws Exception {
        String token = this.login();
        //String token = this.userLogin("8606245097","123456");
        byte[] responseString = manageClient.get().uri("/advertisement/states").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                //"  \"errmsg\": \"成功\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"code\": 0,\n" +
                "      \"name\": \"待审核\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 4,\n" +
                "      \"name\": \"上架\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 6,\n" +
                "      \"name\": \"下架\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 使用伪造token获得广告的所有状态
     *
     * @author 24320182203175 陈晓如
     */
    @Test
    public void advertiseTest2() throws Exception {
        byte[] ret = mallClient.get().uri("/advertisement/states")
                .header("authorization", "test")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 未登录获得广告的所有状态
     *
     * @author 24320182203175 陈晓如
     */
    @Test
    public void advertiseTest1() throws Exception {
        byte[] ret = mallClient.get().uri("/advertisement/states")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员设置默认广告
     * 操作的广告不存在
     * @throws Exception
     */
    @Test
    public void advertiseTest5() throws Exception{
        String token = this.login();
        byte[] responseString = manageClient.put().uri("/shops/0/advertisement/1/default")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();

//        String expectedResponse = "{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}";
//        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);

    }

    /**
     * 管理员设置默认广告
     * 上架态的广告从默认变为非默认
     * @throws Exception
     */
    @Test
    public void advertiseTest6() throws Exception{
        String token = this.login();
        byte[] responseString = manageClient.put().uri("/shops/{did}/advertisement/{id}/default",0,122).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        byte[] ret = manageClient.get().uri("/shops/{did}/timesegments/{id}/advertisement",0,2)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data.list[?(@.id=='122')].beDefault").isEqualTo(0)
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员设置默认广告
     * 下架态的广告从非默认变为默认
     * @throws Exception
     */
    @Test
    public void advertiseTest7() throws Exception{
        String token = this.login();
        byte[] responseString = manageClient.put().uri("/shops/0/advertisement/122/default").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        byte[] ret = manageClient.get().uri("/shops/{did}/timesegments/{id}/advertisement",0,2)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data.list[?(@.id=='122')].beDefault").isEqualTo(1)
                .returnResult()
                .getResponseBodyContent();
    }



}
