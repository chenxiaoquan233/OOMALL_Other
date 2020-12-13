package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.dto.ShareDTO;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.mapper.BeSharePoMapper;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
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
@RocketMQMessageListener(topic="beShared-topic",consumerGroup = "beshare-group")
public class CreateBeShareListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private BeSharePoMapper beSharePoMapper;
    @Override
    public void onMessage(String s) {
        BeSharePo beSharePo=JacksonUtil.toObj(s,BeSharePo.class);
        beSharePoMapper.insertSelective(beSharePo);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        log.debug(this.getClass().getName()+" is listening...");
    }
}
