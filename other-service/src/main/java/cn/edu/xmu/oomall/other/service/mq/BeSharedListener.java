package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.dto.ShareDto;
import cn.edu.xmu.oomall.other.dao.ShareDao;
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
 * 4 用于更新下单成功后的beshare对象
 */
@Slf4j
@Component
@RocketMQMessageListener(topic="share-request",consumerGroup = "share-group")
public class BeSharedListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private ShareDao shareDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void onMessage(String s) {
        ShareDto shareDTO= JacksonUtil.toObj(s,ShareDto.class);
        ShareDto ret=shareDao.getFirstBeShared(shareDTO.getCustomerId(), shareDTO.getSkuId(), shareDTO.getOrderItemId());
        String message=JacksonUtil.toJson(ret);
        rocketMQTemplate.sendOneWay("share-response",message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        log.debug(this.getClass().getName()+" is listening...");
    }
}
