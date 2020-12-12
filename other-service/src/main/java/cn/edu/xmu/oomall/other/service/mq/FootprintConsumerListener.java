package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.dao.FootprintDao;
import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
import cn.edu.xmu.oomall.other.model.po.FootPrintPo;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 日志消费者
 * @author Ji Cao
 */
@Service
@RocketMQMessageListener(topic = "footprint-topic", consumerGroup = "footprint-group")
public class FootprintConsumerListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private FootprintDao footprintDao;

    private static final Logger logger = LoggerFactory.getLogger(FootprintConsumerListener.class);

    @Override
    public void onMessage(String message) {
        FootPrintPo footPrintPo = JacksonUtil.toObj(message,FootPrintPo.class);
        logger.debug("onMessage: got message footprint =" + footPrintPo);
        footprintDao.addFootprint(footPrintPo);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        logger.info("prepareStart: consumergroup =" + defaultMQPushConsumer.getConsumerGroup());
    }
}
