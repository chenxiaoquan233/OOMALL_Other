package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.dto.CartDto;
import cn.edu.xmu.oomall.other.dao.ShoppingCartDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 2 * @author: JiangXiao
 * 3 * @date: 2020/12/12 上午8:55
 * 4
 */
@Slf4j
@Component
@RocketMQMessageListener(topic="cart-topic",consumerGroup = "cart-group")
public class DeleteCartListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteCartListener.class);
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void onMessage(String s) {
        CartDto cart= JacksonUtil.toObj(s,CartDto.class);
        shoppingCartDao.deleteCartByCustomerAndSku(cart.getCustomerId(),cart.getSkuIdList());
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        log.debug(this.getClass().getName()+"is listening...");
    }
}
