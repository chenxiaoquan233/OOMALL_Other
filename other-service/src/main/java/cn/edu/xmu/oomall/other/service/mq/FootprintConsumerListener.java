package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.dao.FootprintDao;
import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
import cn.edu.xmu.oomall.other.model.po.FootPrintPo;
import cn.edu.xmu.oomall.other.model.vo.FootPrint.FootPrintVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



/**
 * 足迹消费者
 * @author Ji Cao
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "footprint-topic", consumerGroup = "footprint-group")
public class FootprintConsumerListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private FootprintDao footprintDao;

    private static final Logger logger = LoggerFactory.getLogger(FootprintConsumerListener.class);

    /**
     * 待修改
     * @param message
     */
    @Override
    public void onMessage(String message) {
        FootPrintVo footPrintVo = JacksonUtil.toObj(message, FootPrintVo.class);
        logger.debug("onMessage: got message footprint =" + footPrintVo);
        footprintDao.addFootprint(footPrintVo);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        logger.info("prepareStart: consumergroup =" + defaultMQPushConsumer.getConsumerGroup());
    }
}
