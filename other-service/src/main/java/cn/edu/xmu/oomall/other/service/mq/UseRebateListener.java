package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.dto.RebateDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author XQChen
 * @version 创建时间：2020/12/12 下午5:35
 */
@Slf4j
@Component
@RocketMQMessageListener(topic="rebate-topic",consumerGroup = "customer-group")
public class UseRebateListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(String s) {
        RebateDTO rebateDTO = JacksonUtil.toObj(s, RebateDTO.class);
        Integer used = userDao.useDebate(rebateDTO.getCustomerId(), rebateDTO.getNum());
        String message=JacksonUtil.toJson(used);
        rocketMQTemplate.sendOneWay("customer-topic",message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        log.debug(this.getClass().getName()+"is listening...");
    }
}
