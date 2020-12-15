package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.util.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author XQChen
 * @version 创建时间：2020/12/14 下午3:26
 */
@SpringBootTest(classes = TestApplication.class)
public class AftersaleTest {

    private static final Logger logger = LoggerFactory.getLogger(AftersaleTest.class);

    private WebTestClient webClient;

    private String testInput;
    private String expectedOutput;

    public AftersaleTest() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9999")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        try {
            this.testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/Aftersale.json")));
            this.expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/Aftersale.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 正确获取所有售后状态
     * @throws Exception
     */
    @Test
    public void getAftersaleAllStates01() throws Exception {
        String adminToken = Stub.createToken(0L, 0L, 100000);

        String response = new String(Objects.requireNonNull(webClient
                .get()
                .uri("/aftersales/states")
                .header("authorization", adminToken)
                .exchange()
                .expectHeader()
                .contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/getAftersaleAllStates01");

        logger.debug("getAftersaleAllStates01: response: -=" + response + "=-, expect: -=" + expect + "=-");

        JSONAssert.assertEquals(expect, response, true);
    }

    /**
     * 正确创建售后单
     * @throws Exception
     */
    @Test
    public void createAftersale01() throws Exception {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale01");

        String response = new String(Objects.requireNonNull(webClient
                .post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectHeader()
                .contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/createAftersale01");

        JSONAssert.assertEquals(expect, response, true);
    }

}