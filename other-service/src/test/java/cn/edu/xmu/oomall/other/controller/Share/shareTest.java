package cn.edu.xmu.oomall.other.controller.Share;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.controller.Share.ServiceStub.GoodsService;
import cn.edu.xmu.oomall.other.controller.Share.ServiceStub.OrderService;
import cn.edu.xmu.oomall.other.controller.ShareController;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.other.mapper.ShareActivityPoMapper;
import cn.edu.xmu.oomall.other.mapper.UpdateRebateMapper;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.oomall.other.model.po.ShareActivityPo;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityVo;
import cn.edu.xmu.oomall.other.model.vo.User.UserLoginVo;
import cn.edu.xmu.oomall.other.service.ShareService;
import cn.edu.xmu.oomall.other.service.factory.ShareActivityStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/13 下午3:41
 * 4
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class shareTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    ShareActivityPoMapper shareActivityPoMapper;
    @Autowired
    ShareService shareService;
    @Autowired
    ShareController shareController;
    @BeforeEach
    public String login(String userName, String password) throws Exception {
        UserLoginVo vo = new UserLoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);

        String requireJson = JacksonUtil.toJson(vo);
        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson)).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        return  JacksonUtil.parseString(response, "data");
    }
    @Test
    public void createJson(){
        ShareActivityPo po=new ShareActivityPo();
        po.setStrategy("{\"num\":[0,10,50,100],\"rate\":[0,50,100,130]}");
        po.setBeginTime(LocalDateTime.now());
        System.out.println(JacksonUtil.toJson(po));
    }
    @Test
    public void createNewActivity() throws Exception {
        String token= new JwtHelper().createToken(1L,1L,10000);
        String input="{\"beginTime\":[2020,12,13,20,17,33,217909000],\n\"endTime\":[2020,12,15,20,17,33,217909000],\n\"strategy\":\"{\\\"num\\\":[0,10,50,100],\\\"rate\\\":[0,50,100,130]}\"\n}";
        //System.out.println(shareService.goodsService.getShopBySKUId(1L).getId());
        String response = mvc.perform(post("/share/shops/1/goods/1/shareactivities")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(response);
    }
    @Test
    public void createNewStrategy(){
        ShareActivityStrategy shareActivityStrategy;
        shareActivityStrategy=new ShareActivityStrategy();
    }
    @Test
    public void redisTest(){
        //redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ShareActivityBo.class));
        ShareActivityBo shareActivityBo=null;
        redisTemplate.opsForHash().putIfAbsent("test","1",shareActivityBo);
        shareActivityBo=new ShareActivityBo(shareActivityPoMapper.selectByPrimaryKey(311264L));
        System.out.println(shareActivityBo.getId());
        redisTemplate.opsForHash().putIfAbsent("test","2",shareActivityBo);
        System.out.println(redisTemplate.opsForHash().get("test","1"));
        System.out.println(redisTemplate.opsForHash().get("test","2"));
        shareActivityBo= (ShareActivityBo) redisTemplate.opsForHash().get("test","1");
        if(shareActivityBo==null){
            System.out.println("Is null!!Yes!!");
        }
        System.out.println(redisTemplate.opsForHash().get("test","2"));
        shareActivityBo=(ShareActivityBo) redisTemplate.opsForHash().get("test","2");
        System.out.println(shareActivityBo.getId());
    }
    @Autowired
    UpdateRebateMapper updateRebateMapper;
    @Autowired
    CustomerPoMapper customerPoMapper;
    @Test
    public void testAddPoint(){
        updateRebateMapper.updateRebateByPrimaryKey(20000L,100L);
        System.out.println("point:"+customerPoMapper.selectByPrimaryKey(20000L).getPoint());
        updateRebateMapper.updateRebateByPrimaryKey(20000L,-120L);
        System.out.println("point:"+customerPoMapper.selectByPrimaryKey(20000L).getPoint());
    }
    @Autowired
    ShareDao shareDao;
    @Test
    public void testGetShareActivity(){
        ShareActivityBo shareActivityBo=shareDao.findNowOrNextFirstActivity(273L,null);
        System.out.println(JacksonUtil.toJson(shareActivityBo));
        shareActivityBo=shareDao.findNowOrNextFirstActivity(null,0L);
        System.out.println(JacksonUtil.toJson(shareActivityBo));
        shareActivityBo=shareDao.findNowOrNextFirstActivity(0L,1L);
        System.out.println(JacksonUtil.toJson(shareActivityBo));
    }
}
