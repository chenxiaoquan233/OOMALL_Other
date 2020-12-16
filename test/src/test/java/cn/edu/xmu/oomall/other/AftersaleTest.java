package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.util.JacksonUtil;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**author XQChenversion 创建时间：2020/12/14 下午3:26
 */
@SpringBootTest(classes = TestApplication.class)
public class AftersaleTest {

    private static final Logger logger = LoggerFactory.getLogger(AftersaleTest.class);

    private final WebTestClient webClient;

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
     */
    @Test
    public void getAftersaleAllStates01() throws JSONException {
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
     */
    @Test
    public void createAftersale01() throws JSONException {
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

        JSONAssert.assertEquals(expect, response, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.id", (o1, o2) -> true),
                new Customization("data.serviceSn", (o1, o2) -> true)));
    }

    /**
     * 售后单类型错误
     */
    @Test
    public void createAftersale02() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale02");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单类型为空
     */
    @Test
    public void createAftersale03() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale03");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单数量为负数
     */
    @Test
    public void createAftersale04() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale04");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单数量为空
     */
    @Test
    public void createAftersale05() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale05");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单原因为空
     */
    @Test
    public void createAftersale06() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale06");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单regionId错误
     */
    @Test
    public void createAftersale07() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale07");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单regionId为空
     */
    @Test
    public void createAftersale08() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale08");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单详细地址为空
     */
    @Test
    public void createAftersale09() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale09");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单联系人为空
     */
    @Test
    public void createAftersale10() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale10");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单联系人电话为空
     */
    @Test
    public void createAftersale11() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale11");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单联系人电话格式错误
     */
    @Test
    public void createAftersale12() {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale12");

        webClient.post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * 售后单orderItem不存在
     */
    @Test
    public void createAftersale13() throws JSONException {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale13");

        String response = new String(Objects.requireNonNull(webClient
                .post()
                .uri("/orderItems/10000000/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/createAftersale13");

        JSONAssert.assertEquals(expect, response, true);
    }

    /**
     * 售后单regionId不存在
     */
    @Test
    public void createAftersale14() throws JSONException {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String input = JacksonUtil.parseSubnodeToString(testInput, "/createAftersale14");

        String response = new String(Objects.requireNonNull(webClient
                .post()
                .uri("/orderItems/1/aftersales")
                .bodyValue(input)
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/createAftersale14");

        JSONAssert.assertEquals(expect, response, true);
    }

    /**
     * 用户成功查询售后单
     */
    @Test
    public void getAllAftersale01() throws JSONException {
        String userToken = Stub.createToken(0L, -2L, 100000);

        String response = new String(Objects.requireNonNull(webClient
                .get()
                .uri("/aftersales?page=1&pageSize=3&beginTime=2019-01-01T00:00:00&endTime=2021-01-01T00:00:00&type=0&state=0")
                .header("authorization", userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/getAllAftersale01");

        JSONAssert.assertEquals(expect, response, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.total", (o1, o2) -> true),
                new Customization("data.pages", (o1, o2) -> true),
                new Customization("data.list", (o1, o2) -> true)));
    }

    //TODO 细化用户对售后单的查询，添加各种参数的错误测试、不存在的参数测试，以及其他内部逻辑的无效等价类测试

    /**
     * 管理员成功查询售后单
     */
    @Test
    public void adminGetAllAftersale01() throws JSONException {
        String adminToken = Stub.createToken(0L, 0L, 100000);

        String response = new String(Objects.requireNonNull(webClient
                .get()
                .uri("/shops/0/aftersales?page=1&pageSize=3&beginTime=2019-01-01T00:00:00&endTime=2021-01-01T00:00:00&type=0&state=0")
                .header("authorization", adminToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/getAllAftersale01");

        JSONAssert.assertEquals(expect, response, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.total", (o1, o2) -> true),
                new Customization("data.pages", (o1, o2) -> true),
                new Customization("data.list", (o1, o2) -> true)));
    }

    //TODO 细化管理员对售后单的查询，添加各种参数的错误测试、不存在的参数测试，以及其他内部逻辑的无效等价类测试

    /**
     * 用户成功根据ID查询售后单
     */
    @Test
    public void getAftersaleById01() throws JSONException {
        String adminToken = Stub.createToken(0L, -2L, 100000);

        String response = new String(Objects.requireNonNull(webClient
                .get()
                .uri("/aftersales/4")
                .header("authorization", adminToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/getAftersaleById01");

        JSONAssert.assertEquals(expect, response, true);
    }

    /**
     * 用户根据ID查询不属于自己的售后单
     */
    @Test
    public void getAftersaleById02() throws JSONException {
        String adminToken = Stub.createToken(0L, -2L, 100000);

        String response = new String(Objects.requireNonNull(webClient
                .get()
                .uri("/aftersales/2")
                .header("authorization", adminToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/getAftersaleById02");

        JSONAssert.assertEquals(expect, response, true);
    }

    /**
     * 用户查询售后单ID不存在
     */
    @Test
    public void getAftersaleById03() throws JSONException {
        String adminToken = Stub.createToken(0L, -2L, 100000);

        String response = new String(Objects.requireNonNull(webClient
                .get()
                .uri("/aftersales/20000")
                .header("authorization", adminToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBodyContent()));

        String expect = JacksonUtil.parseSubnodeToString(expectedOutput, "/getAftersaleById03");

        JSONAssert.assertEquals(expect, response, true);
    }
}