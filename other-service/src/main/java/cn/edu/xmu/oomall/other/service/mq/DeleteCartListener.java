package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.dao.ShoppingCartDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/11 下午3:28
 * 4
 */
@Slf4j
@Component
@RocketMQMessageListener(topic="cart-topic",consumerGroup = "cart-group")
public class DeleteCartListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void onMessage(String message) {
        String [] s=message.split("/");
        shoppingCartDao.deleteCartByCustomerAndSku(Long.valueOf(s[0]),Long.valueOf(s[1]));
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        log.debug(this.getClass().getName()+"is listening...");
    }
}
