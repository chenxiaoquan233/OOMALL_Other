package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.alibaba.fastjson.JSONObject;
import com.google.common.net.HttpHeaders;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@SpringBootTest(classes = TestApplication.class)

public class AddressTest {

    private static final Logger logger = LoggerFactory.getLogger(AddressTest.class);

    private WebTestClient manageClient;
    private WebTestClient mallClient;
    private String token = "";
    private String testInput;
    private String expectedOutput;

    @BeforeEach
    public void setUp(){
        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9999")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,"application/json;charset=UTF-8")
                .build();
        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9999")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,"application/json;charset=UTF-8")
                .build();
        try {
            this.testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/Aftersale.json")));
            this.expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/Aftersale.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 买家新增地址
     * 手机号码为空
     * @throws Exception
     */
    @Test
    public void addAddressTest01()throws Exception{

        token = userLogin("36040122840","123456");

        String testInputVo = JacksonUtil.parseSubnodeToString(testInput,"/addAddressTest01");
        //System.out.print(testInputVo);

        String requireJson = "{\n" +
                " \"regionId\": 1,\n" +
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"\"\n" +
                "}";

        byte[] responseString = mallClient.post().uri("/addresses", 1)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.FIELD_NOTVALID.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 新增地址，参数错误，收件人为空
     * @throws Exception
     */
    @Test
    public void addAddressTest02()throws Exception{
        //uid 2
        token = userLogin("36040122840", "123456");
        String requireJson = "{\n" +
                " \"regionId\": 1,\n" +
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"\",\n" +
                " \"mobile\":  \"18990897878\"\n" +
                "}";

        byte[] responseString = mallClient.post().uri("/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.FIELD_NOTVALID.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }


    /**
     * 新增地址，参数错误，详情为空
     * @throws Exception
     */

    @Test
    public void addAddressTest03() throws Exception {
        token = userLogin("36040122840", "123456");
        String requireJson = "{\n" +
                " \"regionId\": 1,\n" +
                " \"detail\":  \"\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n" +
                "}";

        byte[] responseString = mallClient.post().uri("/addresses", 1)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.FIELD_NOTVALID.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     *新增地址，参数错误，地区id为空
     * @throws Exception
     */
    @Test
    public void addAddressTest04()throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson = "{\n" +
                " \"regionId\": null,\n" +
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\" \n" +
                "}";

        byte[] responseString = mallClient.post().uri("/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.FIELD_NOTVALID.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 新增地址 地区不存在
     * @throws Exception
     */
    @Test
    public void addAddressTest05()throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"regionId\": 11111,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 新增地址 地区已废弃
     * @throws Exception
     */
    @Test
    public void addAddressTest06()throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"regionId\": 6,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.REGION_OBSOLETE.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.REGION_OBSOLETE.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 新增地址，买家地址已经达到上限
     */
    @Test
    public void addAddress07() throws Exception{

        token = userLogin("36040122840", "123456");
        String requireJson="{\n" +
                "  \"consignee\": \"test\",\n" +
                "  \"detail\": \"test\",\n" +
                "  \"mobile\": \"12345678910\",\n" +
                "  \"regionId\": 1\n" +
                "}";
        byte[] responseString = mallClient.post().uri("/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.ADDRESS_OUTLIMIT.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * addAddress8
     * 新增地址
     * @throws Exception
     */
    @Test
    public void addAddress08() throws Exception{

        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"regionId\": 1,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("/addresses")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse= "{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"id\": 4,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"测试地址1\",\n" +
                "        \"consignee\": \"测试\",\n" +
                "        \"mobile\": \"18990897878\",\n" +
                "        \"beDefault\": false\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }

    /**
     * 设置默认地址 成功
     * @throws Exception
     */
    @Test
    public void updateDefaultAddress1() throws Exception {
        token = userLogin("36040122840", "123456");
        byte[] responseString = mallClient.put().uri("/addresses/6/default").header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult().getResponseBodyContent();
    }

    /**
     * 地址id不存在
     * 设置默认地址
     * @throws Exception
     */
    @Test
    public void updateDefaultAddress2() throws Exception {
        token = userLogin("36040122840", "123456");

        byte[] responseString = mallClient.put().uri("/addresses/100/default").header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult().getResponseBodyContent();
    }

    /**
     * 只有一个
     * 查询已有地址
     */
    @Test
    public void selectAddress1() throws Exception {
        token = userLogin("36040122840", "123456");
        byte[] responseString = mallClient.get().uri("/addresses?page=2&pageSize=10").header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult().getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"page\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"regionId\": 6,\n" +
                "        \"detail\": \"HuiAn\",\n" +
                "        \"consignee\": \"CJ\",\n" +
                "        \"mobile\": \"19859211300\",\n" +
                "        \"beDefault\": false,\n" +
                "        \"state\": 0\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);


    }


    /**
     * 买家成功修改自己的地址信息
     */
    @Test
    public void updateAddress1()throws  Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"regionId\": 1,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";
        byte[] response = mallClient.put().uri("addresses/4")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }
    /**
     * 买家修改他人的地址信息  失败
     */
    @Test
    public void updateAddress2()throws  Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"regionId\": 1,\n"+
                " \"detail\":  \"测试地址1\",\n" +
                " \"consignee\":  \"测试\",\n" +
                " \"mobile\":  \"18990897878\"\n"+
                "}";
        byte[] response = mallClient.put().uri("addresses/22")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NOT_ALLOW.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_NOT_ALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 删除他人地址 失败
     */
    @Test
    public void deleteAddress1() throws Exception{
        token = userLogin("36040122840", "123456");
        byte[] response = mallClient.delete().uri("addresses/22")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 删除不存在地址
     * @throws Exception
     */
    @Test
    public void deleteAddress2() throws Exception{
        token = userLogin("36040122840", "123456");
        byte[] response = mallClient.delete().uri("addresses/88")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 成功删除地址
     */
    @Test
    public void deleteAddress3() throws Exception{
        token = userLogin("36040122840", "123456");
        byte[] response = mallClient.delete().uri("addresses/7")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * addRegion1
     * 新增地区 父地区不存在
     */
    @Test
    public void addRegion1() throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"name\": \"fujian\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("/shops/0/regions/9999/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }
    /**addRegion2
     * 新增地区 父地区已废弃
     */
    @Test
    public void addRegion2() throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"name\": \"fujian\",\n" +
                " \"postalCode\":  \"100100\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("/shops/0/regions/6/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.REGION_OBSOLETE.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.REGION_OBSOLETE.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * addRegion3
     * 增地区
     */
    @Test
    public void addRegion3() throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"name\": \"YunNan\",\n" +
                " \"postalCode\":  \"671000\"\n"+
                "}";

        byte[] responseString = mallClient.post().uri("/shops/0/regions/1/subregions")
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

    }

    /**
     * 查询某个地区的所有上级地区，该地区为顶级地区（例如北京）,pid=0
     */
    @Test
    public void selectAncestorRegion1() throws Exception {
        token = userLogin("36040122840", "123456");

        byte[] responseString = mallClient.get().uri("/region/1/ancestor").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": [],\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 12. 查询某个地区的所有上级地区，该地区为1级地区
     * @author: Zeyao Feng
     * @date: Created in 2020/12/16 4:24
     */
    @Test
    public void selectAncestorRegion2() throws Exception {
        token = userLogin("36040122840", "123456");

        byte[] responseString = mallClient.get().uri("/region/2/ancestor").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"pid\": 0,\n" +
                "      \"name\": \"China\",\n" +
                "      \"postalCode\": 100000,\n" +
                "      \"state\": 0,\n" +
                "      \"gmtCreate\": \"2020-12-01T12:46:41\",\n" +
                "      \"gmtModified\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 查询某个地区的所有上级地区，该地区为二级地区
     */
    @Test
    public void selectAncestorRegion3() throws Exception {
        token = userLogin("36040122840", "123456");

        byte[] responseString = mallClient.get().uri("/region/3/ancestor").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"pid\": 1,\n" +
                "      \"name\": \"FuZhou\",\n" +
                "      \"postalCode\": 350000,\n" +
                "      \"state\": 0,\n" +
                "      \"gmtCreate\": \"2020-12-01T12:46:41\",\n" +
                "      \"gmtModified\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"pid\": null,\n" +
                "      \"name\": \"China\",\n" +
                "      \"postalCode\": 100000,\n" +
                "      \"state\": 0,\n" +
                "      \"gmtCreate\": \"2020-12-01T12:46:41\",\n" +
                "      \"gmtModified\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    @Test
    public void selectAncestorRegion4() throws Exception {
        token = userLogin("36040122840", "123456");

        byte[] responseString = mallClient.get().uri("/region/1599/ancestor").header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 151,\n" +
                "      \"pid\": 14,\n" +
                "      \"name\": \"厦门市\",\n" +
                "      \"postalCode\": null,\n" +
                "      \"state\": 0,\n" +
                "      \"gmtCreate\": \"2020-12-15T13:29:49\",\n" +
                "      \"gmtModified\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 14,\n" +
                "      \"pid\": 1,\n" +
                "      \"name\": \"福建省\",\n" +
                "      \"postalCode\": null,\n" +
                "      \"state\": 0,\n" +
                "      \"gmtCreate\": \"2020-12-15T13:29:49\",\n" +
                "      \"gmtModified\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"pid\": 0,\n" +
                "      \"name\": \"中国\",\n" +
                "      \"postalCode\": null,\n" +
                "      \"state\": 0,\n" +
                "      \"gmtCreate\": \"2020-12-15T13:29:49\",\n" +
                "      \"gmtModified\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 查询某个地区的所有上级地区，该地区不存在
     */
    @Test
    public void selectAncestorRegion5() throws Exception {
        token = userLogin("36040122840", "123456");

        byte[] responseString = mallClient.get().uri("/region/140700/ancestor").header("authorization",token).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 管理员修改某个地区
     * @throws Exception
     */
    @Test
    public void updateRegionTest1()throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"name\": \"Test\",\n" +
                " \"postalCode\":  \"671111\"\n"+
                "}";
        byte[] response = mallClient.put().uri("/shops/0/regions/4").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员修改某个地区 地区不存在
     * @throws Exception
     */
    @Test
    public void updateRegionTest2()throws Exception{
        token = userLogin("36040122840", "123456");
        String requireJson="{\n"+
                " \"name\": \"Test\",\n" +
                " \"postalCode\":  \"671111\"\n"+
                "}";
        byte[] response = mallClient.put().uri("/shops/99/regions/9999").header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }


    /**
     * 成功使地区无效
     * @throws Exception
     */
    @Test
    public void deleteRegion1()throws  Exception{
        token = userLogin("36040122840", "123456");
        byte[] response = mallClient.delete().uri("/shops/0/regions/6")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }
    /**
     * 使地区无效 地区不存在
     * @throws Exception
     */
    @Test
    public void deleteRegion2()throws  Exception{
        token = userLogin("36040122840", "123456");
        byte[] response = mallClient.delete().uri("/shops/0/regions/9999")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }
    /**
     * 用户登录
     *
     * @author Ji Cao
     */
    public String userLogin(String userName, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = mallClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JSONObject.parseObject(new String(responseString)).getString("data");
    }

    private String adminLogin(String userName, String password) throws Exception{
        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();

        byte[] ret = manageClient.post().uri("/privileges/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return  JacksonUtil.parseString(new String(ret, "UTF-8"), "data");

    }

}
