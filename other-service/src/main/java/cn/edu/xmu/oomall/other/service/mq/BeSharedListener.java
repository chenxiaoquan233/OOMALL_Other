package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.dto.BeSharedDTO;
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
@RocketMQMessageListener(topic="beShared-topic",consumerGroup = "share-group")
public class BeSharedListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private ShareDao shareDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void onMessage(String s) {
        BeSharedDTO beSharedDTO= JacksonUtil.toObj(s,BeSharedDTO.class);
        BeSharedDTO ret=shareDao.getFirstBeShared(beSharedDTO.getCustomId(), beSharedDTO.getSkuId(), beSharedDTO.getOrderItemId());
        String message=JacksonUtil.toJson(ret);
        rocketMQTemplate.sendOneWay("orderItem-topic",message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        log.debug(this.getClass().getName()+"is listening...");
    }
}
