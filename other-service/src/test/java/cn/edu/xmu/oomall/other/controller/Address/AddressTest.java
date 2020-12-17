package cn.edu.xmu.oomall.other.controller.Address;


import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.Favorite.FavoriteTest;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressRetVo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressVo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AddressTest {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(AddressTest.class);

    /**
     *  normal post adress
     *  成功新增地址
     */
    @Test
    public String AddressTest1() throws Exception{
        String token = new JwtHelper().createToken(334L,-2L,20000);
        AddressVo addressVo = new AddressVo();
        addressVo.setRegionId(4440L);
        addressVo.setDetail("SiMing");
        addressVo.setConsignee("CaoJi");
        addressVo.setMobile("13959288888");

        String requireJson = JacksonUtil.toJson(addressVo);
        String response = this.mvc.perform(post("/addresses")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson)).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        return  JacksonUtil.parseString(response, "data");
    }


    /**
     *  address outlimit
     *  地址已超限（20）
     */
}
