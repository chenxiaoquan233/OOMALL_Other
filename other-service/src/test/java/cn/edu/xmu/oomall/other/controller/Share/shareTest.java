package cn.edu.xmu.oomall.other.controller.Share;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.OtherServiceApplication;
import cn.edu.xmu.oomall.other.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.other.mapper.UpdateRebateMapper;
import cn.edu.xmu.oomall.other.service.factory.ShareActivityStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/13 下午3:41
 * 4
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class shareTest {
    @Test
    public void createNewStrategy(){
        ShareActivityStrategy shareActivityStrategy=new ShareActivityStrategy();
        Integer[] num ={0,10,50,100};
        Integer[] rate ={0,50,100,130};
        shareActivityStrategy.setNum(num);
        shareActivityStrategy.setRate(rate);
        String s= JacksonUtil.toJson(shareActivityStrategy);
        System.out.println(s);
        ShareActivityStrategy strategy=JacksonUtil.toObj(s,ShareActivityStrategy.class);
        num=strategy.getNum();
        for(int i=0;i<num.length;i++){
            System.out.println(num[i]);
        }
    }
    @Autowired
    UpdateRebateMapper updateRebateMapper;
    @Autowired
    CustomerPoMapper customerPoMapper;
    @Test
    public void testAddPoint(){
        updateRebateMapper.updateRebateByPrimaryKey(20000L,100L);
        updateRebateMapper.updateRebateByPrimaryKey(20000L,100L);

    }
}
